package simulationconsole.entanglement;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;

/**
 *
 * @author Hwaipy
 */
public class HalfWavePlate extends WavePlate {

    @Override
    protected Matrix<Complex> calculateMatrix(double theta) {
        double theta2 = theta * 2;
        double cosTheta2 = Math.cos(theta2);
        double sinTheta2 = Math.sin(theta2);
        return ComplexMatrix.valueOf(new Complex[][]{
            {Complex.valueOf(cosTheta2, 0), Complex.valueOf(sinTheta2, 0)},
            {Complex.valueOf(sinTheta2, 0), Complex.valueOf(-cosTheta2, 0)}});
    }

    public static HalfWavePlate create(double theta) {
        HalfWavePlate halfWavePlate = new HalfWavePlate();
        halfWavePlate.setTheta(theta);
        return halfWavePlate;
    }
}
