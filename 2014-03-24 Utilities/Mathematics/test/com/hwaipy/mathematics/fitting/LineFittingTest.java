package com.hwaipy.mathematics.fitting;

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
public class LineFittingTest {

    public LineFittingTest() {
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
    public void testLineFittingConstructor() {
        double[] dataA = new double[10];
        double[] dataB = new double[11];
        boolean hasException = false;
        try {
            LineFitting lineFitting = new LineFitting(dataA, dataB);
        } catch (IllegalArgumentException e) {
            hasException = true;
        }
        assertTrue(hasException);
        hasException = false;
        try {
            LineFitting lineFitting = new LineFitting(dataB, dataA);
        } catch (IllegalArgumentException e) {
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    public void testLineFittingResult() {
        double exceptSlope = 1.0026;
        double exceptIntercept = -0.028927;
        double slopeAccuracy = 0.0001;
        double interceptAccuracy = 0.000001;
        double[] dataX = new double[]{0.06315732832455127, 1.0804533453904692,
            2.0501569492694838, 3.099804314712272, 4.086013610951646,
            5.026487954600662, 6.085027535879363, 7.009939643270033,
            8.048267955026047, 9.02307733244092, 10.03878872185929,
            11.088833101005246};
        double[] dataY = new double[]{0.09008526438784845, 1.0311544853795016,
            2.0033635099706624, 3.017590082204988, 4.041185814226147,
            5.098895679546854, 6.022783863084132, 7.061744520885898,
            8.006661309574955, 9.063196057828465, 10.009012721537722,
            11.081920495293405};
        LineFitting lineFitting = new LineFitting(dataX, dataY);
        double slope = lineFitting.getSlope();
        double intercept = lineFitting.getIntercept();
        assertEquals(exceptSlope, slope, slopeAccuracy);
        assertEquals(exceptIntercept, intercept, interceptAccuracy);
    }

    @Test
    public void testRandomApplication() {
        double exceptSlope = 1.0001;
        double exceptIntercept = 1.9381e12;
        double slopeAccuracy = 0.0001;
        double interceptAccuracy = 0.0001e12;
        double[] dataX = new double[]{51628612237256., 51628622237233.,
            51628632237208., 51628642237208., 51628652237233., 51628662237256.,
            51628672237256., 51628682237233., 51628692237233., 51628702237233.};
        double[] dataY = new double[]{3571904350776., 3571914350784.,
            3571924350780., 3571934350784., 3571944350776., 3571954350776.,
            3571964350792., 3571974350760., 3571984350776., 3571994350768.};
        LineFitting lineFitting = new LineFitting(dataX, dataY);
        double slope = lineFitting.getSlope();
        double intercept = lineFitting.getIntercept();
        assertEquals(exceptSlope, slope, slopeAccuracy);
        assertEquals(exceptIntercept, intercept, interceptAccuracy);
    }
}
