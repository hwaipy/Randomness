package D20141123.Ryan_PRA_81_053805;

/**
 *
 * @author Hwaipy
 */
public class GaussianAmplitudeFunction implements Function {

    private static final double COEFFICIENT_FWHM = 1 / (Math.sqrt(Math.log(2) * 2) * 2);
    private static final double COEFFICIENT_FWHM_AMP = COEFFICIENT_FWHM * Math.sqrt(2);
    private final double center;
    private final double FWHM;
    private final double sigmaI;
    private final double sigmaA;
    private final double A;

    public GaussianAmplitudeFunction(double center, double FWHM) {
        this.center = center;
        this.FWHM = FWHM;
        this.sigmaI = COEFFICIENT_FWHM * FWHM;
        this.sigmaA = COEFFICIENT_FWHM_AMP * FWHM;
        this.A = Math.sqrt(1 / (sigmaI * Math.sqrt(2 * Math.PI)));
    }

    @Override
    public double value(double x) {
        return A * Math.exp(-Math.pow((x - center) / sigmaA, 2) / 2);
    }
}
