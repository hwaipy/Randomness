package D20140729.OAMDrawer;

import org.jscience.mathematics.number.Complex;

/**
 *
 * @author Hwaipy
 */
public class LGModeFunction extends PolarFunction {

    private final int p;
    private final int l;
    private final double w;
    private final double amp;

    public LGModeFunction(int p, int l, double w, double amp) {
        this.p = p;
        this.l = l;
        this.w = w;
        this.amp = amp;
    }

    @Override
    protected Complex valueOfPolar(double r, double theta) {
        double lPower = Math.pow(Math.sqrt(2.0) * r / w, l);
        double gaussian = Math.exp(-r * r / w / w);
        Complex helical = Complex.valueOf(0, l * theta).exp();
        Complex c = Complex.valueOf(lPower * gaussian, 0).times(helical).times(amp);
        return c;
    }
}
