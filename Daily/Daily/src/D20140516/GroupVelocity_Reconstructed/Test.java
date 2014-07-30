package D20140516.GroupVelocity_Reconstructed;

/**
 *
 * @author Hwaipy
 */
public class Test {

    public static void main(String[] args) {
//        ngAndNWithLamda();
        double kPump = Light.k(405. / 1e9, false);
        double kSignal = Light.k(810. / 1e9, false);
        double kIdler = Light.k(810. / 1e9, false);
        double omg = 2 * Math.PI / (kPump - kSignal - kIdler);
        System.out.println(omg);
    }

    public static void diffNgsWithDifferentPumpWhenPPFixed() {
        for (double lamdaPump = 780; lamdaPump < 781; lamdaPump += 0.1) {
            double[] paraLamdas = QuasiPhaseMatch.getParaLamdas(lamdaPump / 1e9);
            double lamdaSignal = paraLamdas[0];
            double lamdaIdle = paraLamdas[1];
            double ngPump = GroupVelocity.indexGroupVelocity(lamdaPump / 1e9, true);
            double ngSignal = GroupVelocity.indexGroupVelocity(lamdaSignal, true);
            double ngIdle = GroupVelocity.indexGroupVelocity(lamdaIdle, false);
            System.out.println(lamdaPump + "\t" + paraLamdas[0] * 1e9 + "\t" + paraLamdas[1] * 1e9
                    + "\t" + (ngPump - ngSignal) + "\t" + (ngPump - ngIdle));
        }
    }

    public static void lamdaPumpSignalIdleWithFixedPP() {
        for (double lamdaPump = 330; lamdaPump < 401; lamdaPump += 0.1) {
            double[] paraLamdas = QuasiPhaseMatch.getParaLamdas(lamdaPump / 1e9);
            System.out.println(lamdaPump + "\t" + paraLamdas[0] * 1e9 + "\t" + paraLamdas[1] * 1e9);
        }
    }

    public static void diffNgBetweenPumpAndDegenerateSignal() {
        for (int lamda = 300; lamda < 1000; lamda++) {
            double ngPump = GroupVelocity.indexGroupVelocity(lamda / 1e9, true);
            double ngIdle = GroupVelocity.indexGroupVelocity(2 * lamda / 1e9, true);
            System.out.println(lamda + "\t" + (ngPump - ngIdle));
        }
    }

    public static void diffNgBetweenPumpAndDegenerateIdle() {
        for (int lamda = 300; lamda < 1000; lamda++) {
            double ngPump = GroupVelocity.indexGroupVelocity(lamda / 1e9, true);
            double ngIdle = GroupVelocity.indexGroupVelocity(2 * lamda / 1e9, false);
            System.out.println(lamda + "\t" + (ngPump - ngIdle));
        }
    }

    public static void ngAndNWithLamda() {
        for (int lamda = 300; lamda < 2000; lamda++) {
            System.out.println(lamda + "\t" + GroupVelocity.indexGroupVelocity(lamda / 1e9, true)
                    + "\t" + GroupVelocity.indexGroupVelocity(lamda / 1e9, false)
                    + "\t" + Light.index(lamda / 1e9, true)
                    + "\t" + Light.index(lamda / 1e9, false));
        }
    }
}
