package com.hwaipy.utilities.secure;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
public class MessageDigestUtilitiesTest {

    public MessageDigestUtilitiesTest() {
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
     * Test of hash method, with "MD5" and "SHA1".
     *
     * @throws java.io.UnsupportedEncodingException
     * @throws java.security.NoSuchAlgorithmException
     */
    @Test
    public void testHashStringByteArray() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String[] algorithms = {"MD5", "SHA1"};
        byte[] data = "com.hwaipy.utilities.secure".getBytes("UTF-8");
        byte[][] expResults = {{53, -123, -3, -43, 40, 89, 4, 21, 53, 92, 74, 63, -46, 37, 68, 31},
        {-127, 43, -1, -124, 123, -33, -10, 76, -26, -72, -125, 109, -104, 52, -82, 107, 20, 115, 14, -4}};
        for (int i = 0; i < algorithms.length; i++) {
            String algorithm = algorithms[i];
            byte[] result = MessageDigestUtilities.hash(algorithm, data);
            byte[] expResult = expResults[i];
            assertArrayEquals(expResult, result);
        }
    }

    /**
     * Test of hash method, with "MD5" and "SHA1".
     *
     * @throws java.io.UnsupportedEncodingException
     * @throws java.security.NoSuchAlgorithmException
     */
    @Test
    public void testHashtoStringStringByteArray() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String[] algorithms = {"MD5", "SHA1"};
        byte[] data = "com.hwaipy.utilities.secure".getBytes("UTF-8");
        String[] expResults = {"3585FDD528590415355C4A3FD225441F", "812BFF847BDFF64CE6B8836D9834AE6B14730EFC"};
        for (int i = 0; i < algorithms.length; i++) {
            String algorithm = algorithms[i];
            String result = MessageDigestUtilities.hashToString(algorithm, data);
            assertEquals(expResults[i], result);
        }
    }

}
