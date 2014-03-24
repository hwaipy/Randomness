package com.hwaipy.systemutilities;

/**
 *
 * @author Hwaipy
 */
public class Properties {

    public static String getProperty(String key) {
        String property = System.getProperty(key);
        return property;
    }

    public static String getProperty(String key, String def) {
        String property = System.getProperty(key);
        return property == null ? def : property;
    }
}
