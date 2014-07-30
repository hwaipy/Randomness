package D20140729.OAMDrawer;

import org.jscience.mathematics.number.Complex;

/**
 *
 * @author Hwaipy
 */
public class XTilt implements Function {

    private final static double WAVELENGTH = 0.001;
    private final Function function;
    private final double alpha;

    public XTilt(Function function, double alpha) {
        this.function = function;
        this.alpha = alpha;
    }

    @Override
    public Complex valueOf(double x, double y) {
        double delta = x * Math.tan(alpha);
        double phi = delta / WAVELENGTH * 2 * Math.PI;
        return function.valueOf(x, y).times(Complex.valueOf(0, phi).exp());
    }
}
