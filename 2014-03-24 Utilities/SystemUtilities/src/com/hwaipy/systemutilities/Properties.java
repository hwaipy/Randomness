package com.hwaipy.systemutilities;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 封装系统属性。属性从多个地方载入，依次为： 内存， PROPERTIES_FILE_PATH(utilitiesproperties.xml)
 * 系统preferences 对属性的修改修改体现在内存中。
 *
 * @author Hwaipy
 */
public class Properties {

    private static final String PROPERTIES_FILE_PATH = "utilitiesproperties.xml";
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(Properties.class);
    private static final Map<String, String> PROPERTIES = new HashMap<>();
    private static final Map<String, String> PROPERTIES_FILE = new HashMap<>();

    static {
        try {
            Path preferencesFilePath = java.nio.file.Paths.get(PROPERTIES_FILE_PATH);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(preferencesFilePath.toFile());
            Element rootElement = document.getRootElement();
            if ("properties".equals(rootElement.getName())) {
                Iterator elementIterator = rootElement.elementIterator("property");
                while (elementIterator.hasNext()) {
                    Element element = (Element) elementIterator.next();
                    Attribute attributeKey = element.attribute("key");
                    Attribute attributeValue = element.attribute("value");
                    if (attributeKey != null && attributeValue != null) {
                        String key = attributeKey.getValue();
                        String value = attributeValue.getValue();
                        if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) {
                            PROPERTIES_FILE.put(key, value);
                        }
                    }
                }
            }
        } catch (DocumentException ex) {
        }
    }

    public static String getProperty(String key) {
        System.out.println("get pro "+key);
        String property = PROPERTIES.get(key);
        if (property == null) {
            property = PROPERTIES_FILE.get(key);
        }
        if (property == null) {
            property = PREFERENCES.get(key, null);
        }
        return property;
    }

    public static String getProperty(String key, String def) {
        String property = getProperty(key);
        return property == null ? def : property;
    }

    public static String setProperty(String key, String value) {
        return PROPERTIES.put(key, value);
    }

    public static String removeProperty(String key) {
        return PROPERTIES.remove(key);
    }
}
