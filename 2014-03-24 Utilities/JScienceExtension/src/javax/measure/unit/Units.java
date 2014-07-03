package javax.measure.unit;

import javax.measure.quantity.Frequency;
import javax.measure.quantity.Length;
import static javax.measure.unit.SI.*;

/**
 * <p>
 * This class contains useful units which are not defined in JScience..</p>
 *
 * @author hwaipy
 * @see javax.measure.unit.SI
 * @see javax.measure.unit.NonSI
 */
public class Units {

    public static final Unit<Length> MICROMETRE = METRE.divide(1e6);
    public static final Unit<Length> NANOMETRE = METRE.divide(1e9);
    public static final Unit<Frequency> GIGAHERTZ = HERTZ.times(1e9);
    public static final Unit<Frequency> TERAHERTZ = HERTZ.times(1e12);
}
