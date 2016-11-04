package telescopecalibration;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.Matrix;
import simulationconsole.entanglement.HalfWavePlate;
import simulationconsole.entanglement.QuarterWavePlate;

/**
 *
 * @author Hwaipy
 */
public class PhaseCalc {

  public static void main(String[] args) {
//    Matrix<Complex> Q1 = QuarterWavePlate.create((29.9 + 15.1) / 180. * Math.PI).getMatrix();
//    Matrix<Complex> Q2 = QuarterWavePlate.create((84.272 - 51.3) / 180. * Math.PI).getMatrix();
//    Matrix<Complex> H = HalfWavePlate.create((23.7 + 15.3) / 180. * Math.PI).getMatrix();

    Matrix<Complex> Q1 = QuarterWavePlate.create((-7.8 + 15.1) / 180. * Math.PI).getMatrix();
    Matrix<Complex> Q2 = QuarterWavePlate.create((118.2 - 51.3) / 180. * Math.PI).getMatrix();
    Matrix<Complex> H = HalfWavePlate.create((-56.3 + 15.3) / 180. * Math.PI).getMatrix();

    Matrix<Complex> U = Util.norm(H.times(Q2).times(Q1));
    System.out.println(U);
    System.out.println("-----");
    double p = Util.phase(U.get(1, 1));
    System.out.println(U.get(1, 1));
    System.out.println(p);
    System.out.println(Complex.valueOf(Math.cos(p), Math.sin(p)));
    p = 3.6459928074161545;// - 2 * Math.PI;
    System.out.println(p);
    System.out.println(Complex.valueOf(Math.cos(p), Math.sin(p)));
    System.out.println(p - (-1.786449209171316 - 0.4196469653495) - 2 * Math.PI);
  }
}
