package com.hwaipy.utilities.format;

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
public class NumberFormatUtilitiesTest {

    public NumberFormatUtilitiesTest() {
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

    @Test
    public void testToHex() {
        byte[] bytes = new byte[]{1, 100, 45, 4, 55, -23, -128, -127, 127, 0};
        String expResult = "01642D0437E980817F00";
        String result = NumberFormatUtilities.toHex(bytes);
        assertEquals(expResult, result);
    }
}
