package telescopecalibration;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;
import simulationconsole.entanglement.HalfWavePlate;
import simulationconsole.entanglement.QuarterWavePlate;

/**
 *
 * @author Hwaipy
 */
public class TestBackAndForward {

  public static void main(String[] args) {
    boolean isMT = true;

    double[] zeros = isMT ? new double[]{90.05, 146.58, 99.99} : new double[]{125.42, 174.72, 137.86};
    double[] comp = isMT ? new double[]{80.1, 127.5+19+45, 61.4} : new double[]{115.3, -89.2, 138.2};
    double[] compLogic = new double[3];
    for (int i = 0; i < 3; i++) {
      compLogic[i] = comp[i] - zeros[i];
      System.out.println(compLogic[i]);
    }
    System.out.println();

    QuarterWavePlate qwp1 = QuarterWavePlate.create(compLogic[0]);
    QuarterWavePlate qwp2 = QuarterWavePlate.create(compLogic[1]);
    HalfWavePlate hwp = HalfWavePlate.create(compLogic[2]);

    Matrix<Complex> input = getLinearPolState(0);
//    Matrix<Complex> output = transform(input, reflect, HalfWavePlate.create(-compLogic[2]).getMatrix(), QuarterWavePlate.create(-compLogic[1]).getMatrix(), reflect, QuarterWavePlate.create(compLogic[1]).getMatrix(), HalfWavePlate.create(-compLogic[2]).getMatrix());
//    Matrix<Complex> output = transform(input, reflect, QuarterWavePlate.create(-compLogic[1]).getMatrix(), reflect, QuarterWavePlate.create(compLogic[1]).getMatrix());
    Matrix<Complex> output = transform(input, reflect, QuarterWavePlate.create(compLogic[1]).getMatrix());
    System.out.println(output);
    System.out.println();
    System.out.println(measure(output, 0) / measure(output, Math.PI / 2));
    System.out.println(measure(output, Math.PI / 4) / measure(output, -Math.PI / 4));
  }
  private static Matrix<Complex> reflect = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE, Complex.ZERO}, {Complex.ZERO, Complex.valueOf(-1, 0)}});

  private static Matrix<Complex> getLinearPolState(double theta) {
    return ComplexMatrix.valueOf(new Complex[][]{{Complex.valueOf(Math.cos(theta), 0)}, {Complex.valueOf(Math.sin(theta), 0)}});
  }

  private static double measure(Matrix<Complex> result) {
    if (result.getNumberOfRows() != 1 || result.getNumberOfColumns() != 1) {
      throw new RuntimeException();
    }
    Complex r = result.get(0, 0);
    return Math.pow(r.magnitude(), 2);
  }

  private static double measure(Matrix<Complex> state, double measureTheta) {
    if (state.getNumberOfRows() != 2 || state.getNumberOfColumns() != 1) {
      throw new RuntimeException();
    }
    Complex r = getLinearPolState(measureTheta).transpose().times(state).get(0, 0);
    return Math.pow(r.magnitude(), 2);
  }

  private static Matrix<Complex> transform(Matrix<Complex> input, Matrix<Complex>... Us) {
    Matrix<Complex> state = input;
    for (Matrix<Complex> U : Us) {
      state = U.times(state);
    }
    return state;
  }
}
