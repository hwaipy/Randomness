package simulationconsole.entanglement;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;

/**
 *
 * @author Hwaipy
 */
public class QuarterWavePlate extends WavePlate {

    @Override
    protected Matrix<Complex> calculateMatrix(double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        double cos2Theta = cosTheta * cosTheta;
        double sin2Theta = sinTheta * sinTheta;
        double cosSinTheta = cosTheta * sinTheta;
        return ComplexMatrix.valueOf(new Complex[][]{
            {Complex.valueOf(cos2Theta, sin2Theta), Complex.valueOf(cosSinTheta, -cosSinTheta)},
            {Complex.valueOf(cosSinTheta, -cosSinTheta), Complex.valueOf(sin2Theta, cos2Theta)}});
    }

    public static QuarterWavePlate create(double theta) {
        QuarterWavePlate quarterWavePlate = new QuarterWavePlate();
        quarterWavePlate.setTheta(theta);
        return quarterWavePlate;
    }
}
