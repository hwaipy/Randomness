package com.hwaipy.utilities.system;

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
public class WeakReferenceMapUtilitiesTest {

    public WeakReferenceMapUtilitiesTest() {
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
    public void testWeakAndPutGet() {
        Object strongReference = new Object();
        Object key = new Object();
        Object value = new Object();
        WeakReferenceMapUtilities.put(strongReference, key, value);
        for (int i = 0; i < 10000; i++) {
            int[] testArray = new int[250000];
            WeakReferenceMapUtilities.put(new Object(), "testArray", testArray);
        }
        Object get = WeakReferenceMapUtilities.get(strongReference, key);
        assertSame(get, value);
    }
}
