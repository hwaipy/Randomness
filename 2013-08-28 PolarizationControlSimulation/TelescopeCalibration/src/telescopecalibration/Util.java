package telescopecalibration;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.Matrix;

/**
 *
 * @author Hwaipy
 */
public class Util {

  public static Matrix<Complex> norm(Matrix<Complex> m) {
    return m.times(m.get(0, 0).inverse());
  }

  public static double phase(Complex c) {
//    System.out.println(";;;");
//    System.out.println(c);
    double real = c.getReal();
    double imag = c.getImaginary();
    if (real == 0) {
      return imag >= 0 ? Math.PI / 2 : -Math.PI / 2;
    } else if (imag == 0) {
      return real >= 0 ? 0 : Math.PI;
    }
    double tan = c.getImaginary() / c.getReal();
//    System.out.println(tan);
    double p = Math.atan(tan);
//    System.out.println(p);
    if (real < 0) {
      if (imag > 0) {
        p = Math.PI - p;
      } else {
        p = -Math.PI - p;
      }
    }
    return p;
  }
}
