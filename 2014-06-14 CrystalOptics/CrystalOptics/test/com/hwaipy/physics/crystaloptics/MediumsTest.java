package com.hwaipy.physics.crystaloptics;

import com.hwaipy.measure.quantity.WaveNumber;
import com.hwaipy.measure.unit.Units;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Frequency;
import javax.measure.quantity.Velocity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import org.jscience.physics.amount.Amount;
import org.jscience.physics.amount.Constants;
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
public class MediumsTest {

    public MediumsTest() {
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
    public void testIndexAndGroupIndex() {
        //KTiOPO4
        assertEquals(25.56, Mediums.KTiOPO4.getAbbeNumberVd(Axis.X), 0.01);
        assertEquals(25.37, Mediums.KTiOPO4.getAbbeNumberVe(Axis.X), 0.01);
        assertEquals(23.66, Mediums.KTiOPO4.getAbbeNumberVd(Axis.Y), 0.01);
        assertEquals(23.46, Mediums.KTiOPO4.getAbbeNumberVe(Axis.Y), 0.01);
        assertEquals(19.38, Mediums.KTiOPO4.getAbbeNumberVd(Axis.Z), 0.01);
        assertEquals(19.26, Mediums.KTiOPO4.getAbbeNumberVe(Axis.Z), 0.01);
        testGroupIndex(Mediums.KTiOPO4);
        //KTiOPO4
        assertEquals(18.74, Mediums.LiNbO3.getAbbeNumberVd(Axis.Y), 0.01);
        assertEquals(18.64, Mediums.LiNbO3.getAbbeNumberVe(Axis.Y), 0.01);
        assertEquals(20.53, Mediums.LiNbO3.getAbbeNumberVd(Axis.Z), 0.01);
        assertEquals(20.40, Mediums.LiNbO3.getAbbeNumberVe(Axis.Z), 0.01);
        testGroupIndex(Mediums.LiNbO3);
    }

    private void testGroupIndex(Medium medium) {
        MonochromaticWave omiga = MonochromaticWave.byWaveLength(Amount.valueOf(500, Units.NANOMETRE));
        MonochromaticWave omigaP = MonochromaticWave.byWaveLength(Amount.valueOf(500.0001, Units.NANOMETRE));
        for (Axis axis : new Axis[]{Axis.X, Axis.Y, Axis.Z}) {
            Amount<Frequency> dOmiga = omiga.getAngularFrequency().minus(omigaP.getAngularFrequency());
            Amount<WaveNumber> k = omiga.getWaveNumber(medium, axis);
            Amount<WaveNumber> kP = omigaP.getWaveNumber(medium, axis);
            Amount<WaveNumber> dK = k.minus(kP);
            Amount<Velocity> groupVelocity = (Amount<Velocity>) dOmiga.divide(dK);
            Amount<Dimensionless> groupIndex = (Amount<Dimensionless>) Constants.c.divide(groupVelocity);
            assertEquals(groupIndex.doubleValue(Unit.ONE), medium.getGroupIndex(omiga, axis), 0.05);
        }
    }

    private class IndexAndGroupIndexResults {

        public IndexAndGroupIndexResults() {
        }
    }
}
