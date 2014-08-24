package D20140729.PhaseLocking;

/**
 *
 * @author Hwaipy
 */
public class FourMeasurement {

    private final double p0;
    private final double p1;
    private final double p2;
    private final double p3;

    /**
     *
     * @param p0 0
     * @param p1 Pi
     * @param p2 Pi/2
     * @param p3 -Pi/2
     */
    public FourMeasurement(double p0, double p1, double p2, double p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public double phi() {
        return phi4Value();
//        return phi2Value();
//        return phi2ValueP();
    }

    private double phi4ValueWithLinear() {
        double phiInit = phi4Value();

        return 0;
    }

    private double phi4Value() {
        double tanPhi = (p2 - p3) / (p1 - p0);
        double phi = Math.atan(tanPhi);
        if (p1 < p0) {
            phi += Math.PI;
        }
        return phi;
    }

    private double phi2Value() {
        double cosPhi = 1 - p0 * 2;
        double sinPhi = p2 * 2 - 1;
        double phi1 = Math.acos(cosPhi);
        if (sinPhi < 0) {
            phi1 = 2 * Math.PI - phi1;
        }
        double phi2 = Math.asin(sinPhi);
        if (cosPhi < 0) {
            phi2 = Math.PI - phi2;
        }
//        System.out.println(phi1 + ", " + phi2);
        if (phi1 - phi2 > 3) {
            phi2 += Math.PI * 2;
        } else if (phi1 - phi2 < -3) {
            phi1 += Math.PI * 2;
        }
//        System.out.println(phi1 + ", " + phi2);

        return (phi1 + phi2) / 2;
    }

    private double phi2ValueP() {
        double cosPhi = 1 - p0 * 2;
        double sinPhi = p2 * 2 - 1;
        double tanPhi = sinPhi / cosPhi;

        double phi = Math.atan(tanPhi);
        if (cosPhi < 0) {
            phi += Math.PI;
        }
        return phi;
    }
}
