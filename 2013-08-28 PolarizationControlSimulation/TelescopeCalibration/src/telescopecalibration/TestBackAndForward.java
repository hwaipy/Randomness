package telescopecalibration;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;
import simulationconsole.entanglement.HalfWavePlate;

/**
 *
 * @author Hwaipy
 */
public class TestBackAndForward {

  public static void main(String[] args) {
    double rH = 0.4;
    double rV = 0.6;
    M1Simulation s = new M1Simulation();
    M1Simulation.M1SimulationResult result = s.calculate(rH, rV);
    double a1 = result.angles[0];
    double a2 = result.angles[1];
    double a3 = result.angles[2];
    System.out.println(a1);
    System.out.println(a2);
    System.out.println(a3);
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

//  public static Matrix<Complex> createTeleU(double PA, double PB, double PC, double RV, double RH) {
//    return transform(
////            HalfWavePlate.create(Math.PI / 4),
//            WavePlate(PA, 0),
//            new Rotate(RV),
//            new WavePlate(PB, 0),
//            new Rotate(RH),
//            new WavePlate(PC, 0)
//    //,                new WavePlate(Math.PI, 0)
//    ).getMatrix()
//);
}
