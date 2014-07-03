package D20140516.GroupVelocity;

/**
 *
 * @author Hwaipy
 */
public class GroupVelocity {

    public static double groupVelocity(double lamda, boolean isO) {
        double dLamda = 1e-12;
        double omiga1 = Light.omiga(lamda);
        double omiga2 = Light.omiga(lamda + dLamda);
        double k1 = Light.k(lamda, isO);
        double k2 = Light.k(lamda + dLamda, isO);
        return (omiga1 - omiga2) / (k1 - k2);
    }

    public static double indexGroupVelocity(double lamda, boolean isO) {
        return 3e8 / groupVelocity(lamda, isO);
    }
}
