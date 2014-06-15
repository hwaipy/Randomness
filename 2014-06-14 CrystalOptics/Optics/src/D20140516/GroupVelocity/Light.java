package D20140516.GroupVelocity;

/**
 *
 * @author Hwaipy
 */
public class Light {

    private static final double A1 = 2.12725;
    private static final double B1 = 1.18431;
    private static final double C1 = 5.14852 / 100;
    private static final double D1 = 0.6603;
    private static final double E1 = 100.00507;
    private static final double F1 = 9.68956 / 1000;
    private static final double A2 = 2.09930;
    private static final double B2 = 0.922683;
    private static final double C2 = 0.0467695;
    private static final double D2 = 0.0138408;

    public static double index(double lamda, boolean isO) {
        lamda *= 1000000;
        if (isO) {
            double lamda2 = lamda * lamda;
            double result = A2 + B2 / (1 - C2 / lamda2) - D2 * lamda2;
            return Math.sqrt(result);
        } else {
            double lamda2 = lamda * lamda;
            double result = A1 + B1 / (1 - C1 / lamda2) + D1 / (1 - E1 / lamda2) - F1 * lamda2;
            return Math.sqrt(result);
        }
    }

    public static double omiga(double lamda) {
        return 2 * Math.PI * 3e8 / lamda;
    }

    public static double k(double lamda, boolean isO) {
        double index = index(lamda, isO);
        return 2 * Math.PI * index / lamda;
    }
}
