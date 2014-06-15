package javax.measure.unit;

import javax.measure.quantity.Length;
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
public class UnitsTest {

    public UnitsTest() {
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
    public void testNanpMetre() {
        double expected = 1550;
        Amount<Length> length = Amount.valueOf(1.55e-6, SI.METRE);
        double result = length.doubleValue(Units.NANOMETRE);
        assertEquals(expected, result, 0.000001);
    }
}
