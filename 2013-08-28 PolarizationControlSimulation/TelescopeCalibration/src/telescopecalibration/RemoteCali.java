package telescopecalibration;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.Matrix;
import simulationconsole.entanglement.HalfWavePlate;
import simulationconsole.entanglement.QuarterWavePlate;
import simulationconsole.entanglement.WavePlate;

/**
 *
 * @author Hwaipy
 */
public class RemoteCali {

  public static void main(String[] args) {
//    Matrix<Complex> m1 = getMatrix(new double[]{84.3, 135.1, 67.6}, true);
//    Matrix<Complex> m2 = getMatrix(new double[]{80.1, 127.5, 61.4}, true);
    Matrix<Complex> m1 = getMatrix(new double[]{118.4, -82.0, 140.6}, false);
    Matrix<Complex> m2 = getMatrix(new double[]{115.3, -89.2, 138.2}, false);

    System.out.println(m1);
    System.out.println(m2);
    Matrix<Complex> trans = getMatrixTrans(m1, m2);
    System.out.println(trans);
  }

  private static Matrix<Complex> getMatrix(double[] readAngles, boolean isMT) {
    double[] zerosMT = new double[]{90.05, 146.58, 99.99};
    double[] zerosJF = new double[]{125.42, 174.72, 137.86};
    double[] angles = new double[3];
    for (int i = 0; i < 3; i++) {
      angles[i] = readAngles[i] - (isMT ? zerosMT[i] : zerosJF[i]);
    }
    WavePlate qwp1 = QuarterWavePlate.create(angles[0]);
    WavePlate qwp2 = QuarterWavePlate.create(angles[1]);
    WavePlate hwp = HalfWavePlate.create(angles[2]);
    return qwp1.getMatrix().times(qwp2.getMatrix()).times(hwp.getMatrix());
  }

  private static Matrix<Complex> getMatrixTrans(Matrix<Complex> m1, Matrix<Complex> m2) {
    Matrix<Complex> m = m1.inverse().times(m2);
    Complex firstItem = m.get(0, 0);
    double c = Math.sqrt(firstItem.conjugate().times(firstItem).getReal());
    return m.times(firstItem.conjugate().times(1.0 / c));
  }
}
