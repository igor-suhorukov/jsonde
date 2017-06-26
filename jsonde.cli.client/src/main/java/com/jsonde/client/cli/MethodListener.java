package com.jsonde.client.cli;

import com.jsonde.client.MethodCallListener;
import com.jsonde.client.dao.DaoException;
import com.jsonde.client.dao.DaoFactory;
import com.jsonde.client.domain.Method;
import com.jsonde.client.domain.MethodCall;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodListener implements MethodCallListener {

    private ConcurrentHashMap<String, MethodCall> methodCalls = new ConcurrentHashMap<String, MethodCall>();

    public void onMethodCall(MethodCall methodCall) {
        methodCalls.put(getMethodName(methodCall), methodCall);
    }

    public Map<String, MethodCall> getMethodCalls() {
        return Collections.unmodifiableMap(methodCalls);
    }

    public static String getMethodName(MethodCall methodCall){
        try {
            Method method = DaoFactory.getMethodDao().get(methodCall.getMethodId() );

            String methodName = method.getName();

            String className =
                    null == methodCall.getActualClassId() ?
                            DaoFactory.getClazzDao().get(method.getClassId()).getName() :
                            DaoFactory.getClazzDao().get(methodCall.getActualClassId()).getName();

            return className + "#" + methodName;
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
}

