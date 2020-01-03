package com.iflytek.tps.foun.util;

import com.google.common.collect.Maps;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

public final class JAXBUtils {
    private final static Map<String, JAXBContext> jaxbContextMap = Maps.newConcurrentMap();

    private JAXBUtils() {
    }

    public static <T> String marshall(T t) {
        StringBuilder xml = new StringBuilder();
        try {
            String key = t.getClass().getName();
            JAXBContext jaxbContext = jaxbContextMap.get(key);
            if (jaxbContext == null) {
                jaxbContext = JAXBContext.newInstance(t.getClass());
                jaxbContextMap.put(t.getClass().getName(), jaxbContext);
            }
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            DocumentResult dr = new DocumentResult();
            jaxbMarshaller.marshal(t, dr);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setExpandEmptyElements(true);
            StringWriter stringWriter = new StringWriter();
            XMLWriter writer = new XMLWriter(stringWriter, format);
            writer.write(dr.getDocument());
            xml.append(stringWriter.getBuffer());
        } catch (Exception e) {
            throw new RuntimeException("jaxb marshall error.....", e);
        }

        return xml.toString();
    }

    public static <T> T unMarshall(String xml, Class<T> clazz, ValidationEventHandler handler) {
        try {
            return (T) (getUnmarshaller(clazz, handler).unmarshal(new StringReader(xml)));
        } catch (Exception e) {
            throw new RuntimeException("jaxb unmarshal error.....", e);
        }
    }

    public static <T> T unMarshall(InputStream is, Class<T> clazz, ValidationEventHandler handler) {
        try {
            return (T) (getUnmarshaller(clazz, handler).unmarshal(is));
        } catch (Exception e) {
            throw new RuntimeException("jaxb unmarshal error.....", e);
        }
    }

    private static <T> Unmarshaller getUnmarshaller(Class<T> clazz, ValidationEventHandler handler) throws Exception {
        String key = clazz.getName();
        JAXBContext jaxbContext = jaxbContextMap.get(key);
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(clazz);
            jaxbContextMap.put(clazz.getName(), jaxbContext);
        }
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        if (handler != null) {
            jaxbUnmarshaller.setEventHandler(handler);
        }
        return jaxbUnmarshaller;
    }
}