package D20140729.OAMDrawer;

import org.jscience.mathematics.number.Complex;

/**
 *
 * @author Hwaipy
 */
public class Intensity implements Function {

    private final Function amplitude;

    public Intensity(Function amplitude) {
        this.amplitude = amplitude;
    }

    @Override
    public Complex valueOf(double x, double y) {
        Complex value = amplitude.valueOf(x, y);
        Complex conj = value.conjugate();
        return value.times(conj);
    }

}
