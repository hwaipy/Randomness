package D20141123.Ryan_PRA_81_053805;

import org.jscience.mathematics.number.Complex;

/**
 *
 * @author Hwaipy
 */
public class ComplexIntegrater {

    private final ComplexFunction function;

    public ComplexIntegrater(ComplexFunction function) {
        this.function = function;
    }

    public Complex integrate(double start, double end, int sample) {
        double stepLength = (end - start) / sample;
        Complex result = Complex.ZERO;
        for (int i = 0; i < sample; i++) {
            double x = start + stepLength * (i + 0.5);
            result = result.plus(function.value(x));
        }
        return result.times(stepLength);
    }
}
