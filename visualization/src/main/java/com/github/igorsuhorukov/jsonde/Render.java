package com.github.igorsuhorukov.jsonde;


import net.sf.sdedit.config.Configuration;
import net.sf.sdedit.diagram.Diagram;
import net.sf.sdedit.editor.DiagramLoader;
import net.sf.sdedit.text.TextHandler;
import net.sf.sdedit.ui.ImagePaintDevice;
import net.sf.sdedit.ui.components.configuration.Bean;
import net.sf.sdedit.util.Pair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Render {

    public static final String ENCODING = "UTF-8";

    private Render() {
        throw new UnsupportedOperationException("utility class");
    }

    public static byte[] createImage(String diagram){
        try {
            InputStream inputStream = new ByteArrayInputStream(diagram.getBytes(ENCODING));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Pair loadedDia = DiagramLoader.load(inputStream, ENCODING);
            TextHandler diagramDataProvider = new TextHandler((String)loadedDia.getFirst());
            Configuration configuration = (Configuration) ((Bean) loadedDia.getSecond()).getDataObject();
            ImagePaintDevice imagePaintDevice = new ImagePaintDevice();
            new Diagram(configuration, diagramDataProvider, imagePaintDevice).generate();
            imagePaintDevice.writeToStream(outputStream);
            outputStream.flush();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RenderingException(e);
        }
    }
}
