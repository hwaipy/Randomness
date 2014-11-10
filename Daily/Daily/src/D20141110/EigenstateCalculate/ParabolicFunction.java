package D20141110.EigenstateCalculate;

/**
 *
 * @author Hwaipy
 */
public class ParabolicFunction implements Function {

    private final double a;

    public ParabolicFunction(double a) {
        this.a = a;
    }

    @Override
    public double valueOf(double x) {
        return x * x * a;
    }
}
