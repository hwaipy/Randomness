package telescopecalibration;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;

/**
 * JF + QWP 0.11 3.11 non 0.60 2.55
 *
 * MT + QWP 1.39 36 non 0.64 12.8
 *
 * @author Hwaipy
 */
public class StateCalculator {

  public static void main(String[] args) {
    mainMT(args);
  }

  public static void mainMT(String[] args) {
    double a = Math.sqrt(0.39);
    double b = Math.sqrt(1 - a * a);
    double phi = -10 / 180. * Math.PI;
    Complex alpha = Complex.valueOf(a, 0);
    Complex beta = Complex.valueOf(b * Math.cos(phi), b * Math.sin(phi));
    ComplexMatrix s = ComplexMatrix.valueOf(new Complex[][]{{alpha}, {beta}});
    System.out.println("HV: " + (measure(s, 0 / 180. * Math.PI) / measure(s, 90 / 180. * Math.PI)));
    System.out.println("DA: " + (measure(s, 45 / 180. * Math.PI) / measure(s, -45 / 180. * Math.PI)));
    System.out.println("LR: " + measureRL(s));
  }

  public static void mainJF(String[] args) {
    double a = Math.sqrt(0.375);
    double b = Math.sqrt(0.625);
    double phi = 56 / 180. * Math.PI;
    Complex alpha = Complex.valueOf(a, 0);
    Complex beta = Complex.valueOf(b * Math.cos(phi), b * Math.sin(phi));
    ComplexMatrix s = ComplexMatrix.valueOf(new Complex[][]{{alpha}, {beta}});
    System.out.println("HV: " + (measure(s, 0 / 180. * Math.PI) / measure(s, 90 / 180. * Math.PI)));
    System.out.println("DA: " + (measure(s, 45 / 180. * Math.PI) / measure(s, -45 / 180. * Math.PI)));
    System.out.println("LR: " + measureRL(s));
  }

  private static double measure(Matrix<Complex> state, double measureTheta) {
    if (state.getNumberOfRows() != 2 || state.getNumberOfColumns() != 1) {
      throw new RuntimeException();
    }
    Complex r = getLinearPolState(measureTheta).transpose().times(state).get(0, 0);
    return Math.pow(r.magnitude(), 2);
  }

  private static double measureRL(Matrix<Complex> state) {
    if (state.getNumberOfRows() != 2 || state.getNumberOfColumns() != 1) {
      throw new RuntimeException();
    }
    double sqrt2d = 1 / Math.sqrt(2);
    ComplexMatrix L = ComplexMatrix.valueOf(new Complex[][]{{Complex.valueOf(sqrt2d, 0)}, {Complex.valueOf(0, sqrt2d)}});
    Complex r = L.transpose().times(state).get(0, 0);
    double mR = Math.pow(r.magnitude(), 2);
    return (mR) / (1 - mR);
  }

  private static Matrix<Complex> getLinearPolState(double theta) {
    return ComplexMatrix.valueOf(new Complex[][]{{Complex.valueOf(Math.cos(theta), 0)}, {Complex.valueOf(Math.sin(theta), 0)}});
  }
}
