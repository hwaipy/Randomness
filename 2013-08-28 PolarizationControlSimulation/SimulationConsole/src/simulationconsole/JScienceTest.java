package simulationconsole;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.Matrix;
import simulationconsole.entanglement.HalfWavePlate;
import simulationconsole.entanglement.QuarterWavePlate;
import simulationconsole.entanglement.WavePlate;

/**
 *
 * @author Hwaipy
 */
public class JScienceTest {

    public static void main(String[] args) {
//        Float64Matrix m1 = Float64Matrix.valueOf(new double[][]{{1, 2,}, {3, 4}});
//        Float64Matrix m2 = Float64Matrix.valueOf(new double[][]{{4, 2,}, {-1, 0}});
//        Float64Matrix m = m1.tensor(m2);
        WavePlate h1 = QuarterWavePlate.create(0.1);
        WavePlate h2 = QuarterWavePlate.create(0.1 - Math.PI / 2);
        Matrix<Complex> m = h2.getMatrix().times(h1.getMatrix());
        for (int r = 0; r < m.getNumberOfRows(); r++) {
            for (int c = 0; c < m.getNumberOfColumns(); c++) {
                System.out.print(m.get(r, c) + "\t");
            }
            System.out.println();
        }
    }
}
