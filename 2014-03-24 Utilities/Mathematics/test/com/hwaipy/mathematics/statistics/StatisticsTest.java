package com.hwaipy.mathematics.statistics;

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
public class StatisticsTest {

    double[] data = new double[]{0.5849559139209182, 0.7266241258589807,
        0.6841607904832993, 0.3424199146050829, 0.20373672689179845,
        0.5158952727133753, 0.6537766942811472, 0.682064440856668,
        0.8955240087263167, 0.045741122707960824, 0.9956309388579009,
        0.32162120230058333};
    double accuracy = 0.000000000001;

    public StatisticsTest() {
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
    public void testSum() {
        double result = Statistics.sum(data);
        double expectResult = 6.652151152204030;
        assertEquals(expectResult, result, accuracy);
    }

    @Test
    public void testMean() {
        double result = Statistics.mean(data);
        double expectResult = 0.55434592935034;
        assertEquals(expectResult, result, accuracy);
    }

    @Test
    public void testQuadraticSum() {
        double result = Statistics.quadraticSum(data);
        double expectResult = 4.554552545115330;
        assertEquals(expectResult, result, accuracy);
    }
}
