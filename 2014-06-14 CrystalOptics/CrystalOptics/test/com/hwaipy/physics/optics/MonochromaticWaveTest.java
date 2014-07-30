package com.hwaipy.physics.optics;

import com.hwaipy.physics.crystaloptics.MonochromaticWave;
import javax.measure.quantity.Frequency;
import javax.measure.quantity.Length;
import com.hwaipy.measure.unit.Units;
import org.jscience.physics.amount.Amount;
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
public class MonochromaticWaveTest {

    public MonochromaticWaveTest() {
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
     * Test of Construction and getWaveLength,getFrequency,getAngularFrequency.
     */
    @Test
    public void testGetWaveLength() {
        double exactWaveLength = 1550;
        double exactFrequency = 193.414489032;
        double exactAngularFrequency = 1215.25907568;
        double errorRate = 1e-10;
        Amount<Length> expectedWaveLength = Amount.valueOf(exactWaveLength, exactFrequency * errorRate, Units.NANOMETRE);
        Amount<Frequency> expectedFrequency = Amount.valueOf(exactFrequency, exactFrequency * errorRate, Units.TERAHERTZ);
        Amount<Frequency> expectedAngularFrequency = Amount.valueOf(exactAngularFrequency, exactAngularFrequency * errorRate, Units.TERAHERTZ);

        MonochromaticWave[] instances = new MonochromaticWave[]{
            MonochromaticWave.byWaveLength(expectedWaveLength),
            MonochromaticWave.byFrequency(expectedFrequency),
            MonochromaticWave.byAngularFrequency(expectedAngularFrequency)
        };
        for (MonochromaticWave instance : instances) {
            assertTrue(instance.getWaveLength().approximates(expectedWaveLength));
            assertTrue(instance.getFrequency().approximates(expectedFrequency));
            assertTrue(instance.getAngularFrequency().approximates(expectedAngularFrequency));
        }
    }
}
