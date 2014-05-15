package com.hwaipy.utilities.system;

import java.util.prefs.Preferences;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hwaipy
 */
public class PropertiesTest {

    public PropertiesTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getProperty method.测试default参数的正确性
     */
    @Test
    public void testGetPropertyDefault() {
        String key = "nonexistent";
        String def = "default";
        String result1 = PropertiesUtilities.getProperty(key);
        String result2 = PropertiesUtilities.getProperty(key, def);
        assertEquals(null, result1);
        assertEquals(def, result2);
    }

    /**
     * Test of setProperty & removeProperties method. 测试对属性的设置和移除是否能生效
     */
    @Test
    public void testSetPropertyAndRemoveProperty() {
        String key = "testSetKey";
        String value1 = "testSetValue1";
        String value2 = "testSetValue2";
        String result1 = PropertiesUtilities.getProperty(key);
        String result2 = PropertiesUtilities.setProperty(key, value1);
        String result3 = PropertiesUtilities.getProperty(key);
        String result4 = PropertiesUtilities.setProperty(key, value2);
        String result5 = PropertiesUtilities.getProperty(key);
        String result6 = PropertiesUtilities.removeProperty(key);
        String result7 = PropertiesUtilities.getProperty(key);
        String result8 = PropertiesUtilities.removeProperty(key);
        assertEquals(null, result1);
        assertEquals(null, result2);
        assertEquals(value1, result3);
        assertEquals(value1, result4);
        assertEquals(value2, result5);
        assertEquals(value2, result6);
        assertEquals(null, result7);
        assertEquals(null, result8);
    }

    /**
     * Test of getProperty method.测试从属性文件中的读取是否正确
     */
    @Test
    public void testGetPropertyInFile() {
        String key = "testPropertiesFileKey";
        String expResult = "testPropertiesFileValue";
        String result = PropertiesUtilities.getProperty(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of getProperty method.测试从Preferences中的读取是否正确
     */
    @Test
    public void testGetPropertyInPreferences() {
        String key = "testPreferencesKey";
        String value = "testPreferencesValue";
        Preferences preferences = Preferences.userNodeForPackage(PropertiesUtilities.class);
        String result1 = PropertiesUtilities.getProperty(key);
        preferences.put(key, value);
        String result2 = PropertiesUtilities.getProperty(key);
        preferences.remove(key);
        assertEquals(null, result1);
        assertEquals(value, result2);
    }

    /**
     * Test of getProperty method.测试读取顺序
     */
    @Test
    public void testGetPropertyOrder() {
        String memoryKey = "testMemoryKey";
        String memoryValue = "testMemoryValue";
        String fileKey = "testPropertiesFileKey";
        String fileValue = "testPropertiesFileValue";
        String preferencesKey = "testPreferencesKey";
        String preferencesValue = "testPreferencesValue";
        String result1 = PropertiesUtilities.getProperty(fileKey);
        Preferences preferences = Preferences.userNodeForPackage(PropertiesUtilities.class);
        preferences.put(preferencesKey, preferencesValue);
        String result2 = PropertiesUtilities.getProperty(fileKey);
        PropertiesUtilities.setProperty(memoryKey, memoryValue);
        String result3 = PropertiesUtilities.getProperty(memoryKey);
        PropertiesUtilities.removeProperty(memoryKey);
        preferences.remove(preferencesKey);
        assertEquals(fileValue, result1);
        assertEquals(fileValue, result2);
        assertEquals(memoryValue, result3);
    }
}
