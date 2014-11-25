package D20141123.Ryan_PRA_81_053805;

/**
 *
 * @author Hwaipy
 */
public class Integrater {

    private final Function function;

    public Integrater(Function function) {
        this.function = function;
    }

    public double integrate(double start, double end, int sample) {
        double stepLength = (end - start) / sample;
        double result = 0;
        for (int i = 0; i < sample; i++) {
            double x = start + stepLength * (i + 0.5);
            result += function.value(x);
        }
        return result * stepLength;
    }
}
