package com.hwaipy.systemutilities;

import java.nio.file.Path;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 封装系统属性。属性从多个地方载入，依次为：内存动态，
 * PROPERTIES_FILE_PATH(utilitiesproperties.xml)，和系统preferences。
 * 对属性的修改修改体现在内存动态中。
 *
 * @author Hwaipy
 */
public class Properties {

    private static final String PROPERTIES_FILE_PATH = "utilitiesproperties.xml";
//    private static final Preferences[] PREFERENCESES;

    static {
        try {
            Path preferencesFilePath = java.nio.file.Paths.get(PROPERTIES_FILE_PATH);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(preferencesFilePath.toFile());
            Element rootElement = document.getRootElement();
            Iterator elementIterator = rootElement.elementIterator("preference");
            while (elementIterator.hasNext()) {
                Element element = (Element) elementIterator.next();
                System.out.println(1);
            }
        } catch (DocumentException ex) {
            System.out.println("ex");
        }
    }

    public static String getProperty(String key) {
        String property = System.getProperty(key);
        return property;
    }

    public static String getProperty(String key, String def) {
        String property = System.getProperty(key);
        return property == null ? def : property;
    }

    public static void main(String[] args) {
        System.out.println(1123);
    }

}
