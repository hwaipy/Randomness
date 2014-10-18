package D20141016.GroupVelocityWithFilter;

/**
 *
 * @author Hwaipy
 */
public class PhaseMatchFunction extends CorrelationFunction {

    private final double lengthOfCrystal;
    private final double period;

    public PhaseMatchFunction(double lengthOfCrystalInMM, double periodInUM) {
        this.lengthOfCrystal = lengthOfCrystalInMM / 1000;
        this.period = periodInUM / 1e6;
    }

    @Override
    public double correlationValue(double lamdaSignal, double lamdaIdler) {
        double lamdaPump = 1 / (1 / lamdaSignal + 1 / lamdaIdler);
        double kPump = Light.k(lamdaPump / 1e9, true);
        double kSignal = Light.k(lamdaSignal / 1e9, true);
        double kIdler = Light.k(lamdaIdler / 1e9, false);
        double arg = lengthOfCrystal / 2 * (kPump - kSignal - kIdler - 2 * Math.PI / period);
//            double arg = lengthOfCrystal / 2 * (kPump - kSignal - kIdler - 2 * Math.PI / 3.4377532602689514e-6);
//            double arg = lengthOfCrystal / 2 * (kPump - kSignal - kIdler - 2 * Math.PI / 10.035e-6);
//            double arg = lengthOfCrystal / 2 * (kPump - kSignal - kIdler + 2 * Math.PI / 46.1e-6);
        double result = Math.sin(arg) / arg;
        return result;
    }
}
