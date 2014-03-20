package D20140320.AnamorphicPrismAngle;

/**
 *
 * @author Hwaipy
 */
public class APAngle {

    private static double n = 1.5;
    private static double alpha = 34.14;

    public static void main(String[] args) {
//        drawMagAngleFigOptimal();
        drawMagAngleFigFixedMidAngle();
    }

    private static void drawMagAngleFigFixedMidAngle() {
        for (double midAngle = 0.; midAngle < 65.; midAngle += 0.1) {
            double a1 = findA1OverAll(midAngle);
            double a2 = a1 - midAngle + alpha;
            double mag = calc(a1, a2)[1];
            System.out.println(mag + "\t" + a1 + "\t" + a2 + "\t" + midAngle);
        }
    }

    private static void drawMagAngleFigOptimal() {
        for (double a1 = -10.; a1 < 20.; a1 += 0.1) {
            double a2 = findA2(a1);
            double mag = calc(a1, a2)[1];
            System.out.println(mag + "\t" + a1 + "\t" + a2);
        }
    }

    public static double findA1OverAll(double midAngle) {
        double minT = 180;
        double minA1 = 180;
        for (double a1 = -10.; a1 < 20.; a1 += 0.1) {
            double a2 = a1 - midAngle + alpha;
            double target = Math.abs(calc(a1, a2)[0]);
            if (target < minT) {
                minT = target;
                minA1 = a1;
            }
        }
        if (minT > 1) {
            throw new RuntimeException();
        }
        return minA1;
    }

    public static double findA2(double a1) {
        double minT = 180;
        double minA2 = 180;
        for (double a2 = -20.; a2 < 20.; a2 += 0.1) {
            double target = Math.abs(calc(a1, a2)[0]);
            if (target < minT) {
                minT = target;
                minA2 = a2;
            }
        }
        if (minT > 1) {
            System.out.println(minT);
            throw new RuntimeException();
        }
        return minA2;
    }

    public static double[] calc(double a1, double a2) {
        double t0 = 0;
        double alpha1 = a1 / 180 * Math.PI;
        double alpha2 = a2 / 180 * Math.PI;
        double surface1 = Math.PI / 2 - alpha1 - alpha / 180 * Math.PI;
        double surface2 = Math.PI / 2 - alpha1;
        double surface3 = Math.PI / 2 - alpha2 + alpha / 180 * Math.PI;
        double surface4 = Math.PI / 2 - alpha2;
//        System.out.println("Surfaces: \t" + (surface1 / Math.PI * 180) + "\t"
//                + (surface2 / Math.PI * 180) + "\t"
//                + (surface3 / Math.PI * 180) + "\t"
//                + (surface4 / Math.PI * 180));
        double t1[] = refrection(t0, surface1, 1, n);
        double t2[] = refrection(t1[0], surface2, n, 1);
        double t3[] = refrection(t2[0], surface3, 1, n);
        double t4[] = refrection(t3[0], surface4, n, 1);
//        System.out.println("Light: \t" + (t1 / Math.PI * 180) + "\t"
//                + (t2 / Math.PI * 180) + "\t"
//                + (t3 / Math.PI * 180) + "\t"
//                + (t4 / Math.PI * 180));
        double mag = t1[1] * t2[1] * t3[1];
        return new double[]{t4[0] / Math.PI * 180, mag};
    }

    private static double[] refrection(double input, double surface, double n1, double n2) {
//        System.out.println("In refrection: input = " + deg(input) + ", surface = " + deg(surface) + ", n1 = " + n1 + ", n2 = " + n2);
        double faRef = surface - Math.PI / 2;
//        System.out.println("faRef = " + deg(faRef));
        double in = input - faRef;
//        System.out.println("inAngle = " + deg(in));
        double out = Math.asin(Math.sin(in) * n1 / n2);
//        System.out.println("outAngle = " + deg(out));
        double angle = out + faRef;
        double mag = Math.cos(out) / Math.cos(in);
//        System.out.println("return = " + deg(ret));
        return new double[]{angle, mag};
    }

    private static double deg(double rad) {
        return rad / Math.PI * 180;
    }
}
