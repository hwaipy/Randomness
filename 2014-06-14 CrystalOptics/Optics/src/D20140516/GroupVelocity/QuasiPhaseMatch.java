package D20140516.GroupVelocity;

/**
 *
 * @author Hwaipy
 */
public class QuasiPhaseMatch {

//    public static final double pp = 7.882795740522679E-6;
    public static final double pp = 42E-6;

    public static double[] getParaLamdas(double lamdaPump) {
        double kPump = Light.k(lamdaPump, true);
        double kPP = 2 * Math.PI / pp;
        double minPhaseMismatch = Double.MAX_VALUE;
        double matchestLamdaSignal = 0;
        for (double lamdaSignal = 600e-9; lamdaSignal < 1.5e-6; lamdaSignal += 1e-10) {
            double lamdaIdle = 1 / (1 / lamdaPump - 1 / lamdaSignal);
            double kSignal = Light.k(lamdaSignal, true);
            double kIdle = Light.k(lamdaIdle, false);
            double phaseMismatch = Math.abs(kPump - kSignal - kIdle - kPP);
//            System.out.println(lamdaSignal + "\t" + lamdaIdle + "\t" + phaseMismatch);
            if (phaseMismatch < minPhaseMismatch) {
                minPhaseMismatch = phaseMismatch;
                matchestLamdaSignal = lamdaSignal;
            }
        }
        return new double[]{matchestLamdaSignal, 1 / (1 / lamdaPump - 1 / matchestLamdaSignal)};
    }
}
