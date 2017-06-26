package com.jsonde.client.cli;

import com.jsonde.api.configuration.AgentConfigurationMessage;
import com.jsonde.api.configuration.ClassFilterDto;
import com.jsonde.client.Client;
import com.jsonde.client.domain.MethodCall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CliClient {

    private static final int TIMEOUT = 50;

    public Map<String, MethodCall> connect(String databaseFileName, String address, int port, String applicationPackage){

        ArrayList<ClassFilterDto> filters = new ArrayList<ClassFilterDto>();
        filters.add(new ClassFilterDto(false,"*"));
        filters.add(new ClassFilterDto(true, applicationPackage));
        return connect(filters,databaseFileName, address, port);
    }

    public Map<String, MethodCall> connect(List<ClassFilterDto> filters, String databaseFileName, String address, int port){
        Client client = new Client(databaseFileName, address, port);
        client.start();

        AgentConfigurationMessage configurationMessage = new AgentConfigurationMessage();
        configurationMessage.setClassFilters(filters);

        client.sendMessage(configurationMessage);

        MethodListener methodListener = new MethodListener();
        client.addMethodCallListener(methodListener);

        while (client.isOnline() && !Thread.interrupted()){
            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
        return methodListener.getMethodCalls();
    }

}
