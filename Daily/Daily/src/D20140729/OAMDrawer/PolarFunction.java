package D20140729.OAMDrawer;

import org.jscience.mathematics.number.Complex;

/**
 *
 * @author Hwaipy
 */
public abstract class PolarFunction implements Function {

    @Override
    public Complex valueOf(double x, double y) {
        double r = Math.sqrt(x * x + y * y);
        double theta = Math.atan(y / x);
        if (x < 0) {
            theta += Math.PI;
        }
        return valueOfPolar(r, theta);
    }

    protected abstract Complex valueOfPolar(double r, double theta);
}
