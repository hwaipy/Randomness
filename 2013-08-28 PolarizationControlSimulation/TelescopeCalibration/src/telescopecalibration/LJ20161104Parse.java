package telescopecalibration;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;

/**
 *
 * @author Hwaipy
 */
public class LJ20161104Parse {

  public static void main(String[] args) {
    System.out.println("Goes");
  }

  static class Config {

    private double laser = 0;
    private Matrix<Complex> satelliteFibre = null;
    private double[] satelliteWatePlateAnglesDeg = new double[3];
    private double satelliteRotateDeg = 0;
    private double[] gourndTelescopePhasesArc = new double[]{0, 0, 0};
    private double[] gourndTelescopeAnglesDeg = new double[2];
    private double[] groundWavePlateAngleDeg = new double[3];

    private void calculate() {
      _laserPol = getLinearPolState(laser);
      transform(_laserPol, satelliteFibre);
    }
    private Matrix<Complex> _laserPol;
  }

  private static Matrix<Complex> getLinearPolState(double theta) {
    return ComplexMatrix.valueOf(new Complex[][]{{Complex.valueOf(Math.cos(theta), 0)}, {Complex.valueOf(Math.sin(theta), 0)}});
  }

  private static Matrix<Complex> transform(Matrix<Complex> input, Matrix<Complex>... Us) {
    Matrix<Complex> state = input;
    for (Matrix<Complex> U : Us) {
      state = U.times(state);
    }
    return state;
  }
}
