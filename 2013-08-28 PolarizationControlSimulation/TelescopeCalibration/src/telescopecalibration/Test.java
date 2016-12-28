package telescopecalibration;

import com.hwaipy.science.polarizationcontrol.m1.M1Process;
import com.hwaipy.science.polarizationcontrol.m1.M1ProcessException;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;
import simulationconsole.entanglement.HalfWavePlate;
import simulationconsole.entanglement.QuarterWavePlate;

/**
 *
 * @author Hwaipy
 */
public class Test {

  public static void main(String[] args) throws M1ProcessException {
    boolean isMT = true;

    double[] zeros = isMT ? new double[]{90.05, 146.58, 99.99} : new double[]{125.42, 174.72, 137.86};
    double[] comp = isMT ? new double[]{80.1, 127.5, 61.4} : new double[]{115.3, -89.2, 138.2};
//    double[] zeros = new double[]{30, 72, 11};
//    double[] comp = new double[]{-9, 44.98, 55.4};
    double[] compLogic = new double[3];
    for (int i = 0; i < 3; i++) {
      compLogic[i] = comp[i] - zeros[i];
      System.out.println(compLogic[i]);
      compLogic[i] = compLogic[i] / 180.0 * Math.PI;
    }
    System.out.println();
    QuarterWavePlate qwp1 = QuarterWavePlate.create(compLogic[0]);
    QuarterWavePlate qwp2 = QuarterWavePlate.create(compLogic[1]);
    HalfWavePlate hwp = HalfWavePlate.create(compLogic[2]);
    Matrix<Complex> compMatrix = hwp.getMatrix().times(qwp2.getMatrix()).times(qwp1.getMatrix());
    Matrix<Complex> fibreMatrix = compMatrix.inverse();

    double mirrorPhase = -0.44;//MT
//    double mirrorPhase = -0.92;//JF
//    double mirrorPhase = 2.7;//Simu
//    Matrix<Complex> mirror = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE, Complex.ZERO}, {Complex.ZERO, Complex.valueOf(Math.cos(mirrorPhase), Math.sin(mirrorPhase))}});
//    hwp = HalfWavePlate.create(compLogic[2] + 0.0 / 180 * Math.PI);
//    qwp2 = QuarterWavePlate.create(compLogic[1] + 0.0 / 180 * Math.PI);

//    Matrix<Complex> input = getLinearPolState(0);
//    Matrix<Complex> output = transform(input, fibreMatrix, qwp1.getMatrix(), qwp2.getMatrix(), hwp.getMatrix(), mirror);
//    System.out.println(measure(output, 0) / measure(output, Math.PI / 2));
//    System.out.println(measure(output, Math.PI / 4) / measure(output, -Math.PI / 4));
//    System.out.println();
//    qwp1 = QuarterWavePlate.create(Math.PI / 4);
//    qwp2 = QuarterWavePlate.create(Math.PI / 4);
//    hwp = HalfWavePlate.create((5.5 + 90) / 180 * Math.PI);
//    compMatrix = qwp2.getMatrix().times(hwp.getMatrix()).times(qwp1.getMatrix());
//    Complex firstItem = compMatrix.get(0, 0);
//    double c = Math.sqrt(firstItem.conjugate().times(firstItem).getReal());
//    compMatrix = compMatrix.times(firstItem.conjugate().times(1.0 / c));
//    System.out.println(compMatrix);
//    double p = -2.758;
//    System.out.println(Math.cos(p));
//    System.out.println(Math.sin(p));
    for (int i = 90; i <= 90; i++) {
      double mp = i / 180.0 * Math.PI;
      ComplexMatrix mirror = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE, Complex.ZERO}, {Complex.ZERO, Complex.valueOf(Math.cos(mp), Math.sin(mp))}});
      System.out.println(mirror);
      Matrix<Complex> Unew = mirror.inverse().times(compMatrix);
      double[] measurements = calculateMeasurements(Unew.inverse());
      double[] aow = calculateAngleOfWP(measurements);
      System.out.println(compMatrix);
      System.out.println(Unew);
      System.out.print(i + "\t");
      for (int qi = 0; qi < 3; qi++) {
        System.out.print((aow[qi]) / Math.PI * 180 + zeros[qi]);
        System.out.print("\t");
      }
      System.out.println();
      Matrix<Complex> newCompMatrix = HalfWavePlate.create(aow[2]).getMatrix().times(QuarterWavePlate.create(aow[1]).getMatrix()).times(QuarterWavePlate.create(aow[0]).getMatrix());
      System.out.println(newCompMatrix.times(compMatrix.inverse()));
//
//      input = getLinearPolState(Math.PI / 4);
//      mp = -mp;
//      mirror = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE, Complex.ZERO}, {Complex.ZERO, Complex.valueOf(Math.cos(mp), Math.sin(mp))}});
//      output = transform(input, fibreMatrix, QuarterWavePlate.create(aow[0]).getMatrix(), QuarterWavePlate.create(aow[1]).getMatrix(), HalfWavePlate.create(aow[2]).getMatrix(), mirror);
//      System.out.println(measure(output, 0) / measure(output, Math.PI / 2));
//      System.out.println(measure(output, Math.PI / 4) / measure(output, -Math.PI / 4));
//      System.out.println();
    }
  }

  private static double[] calculateAngleOfWP(double[] v12) throws M1ProcessException {
    M1Process m1 = M1Process.calculate(v12);
    return m1.getResults();
  }

  private static double[] calculateMeasurements(Matrix<Complex> U) {
    Matrix<Complex> mH = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE, Complex.ZERO}});
    Matrix<Complex> mD = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2)), Complex.ONE.divide(Math.sqrt(2))}});
    Matrix<Complex> phiHAfter = U.times(mH.transpose());
    Matrix<Complex> phiDAfter = U.times(mD.transpose());
    Matrix<Complex> MQ_0 = QuarterWavePlate.create(0).getMatrix();
    Matrix<Complex> MQ_N45 = QuarterWavePlate.create(-Math.PI / 4).getMatrix();
    Matrix<Complex> MH_0 = HalfWavePlate.create(0).getMatrix();
    Matrix<Complex> MH_22_5 = HalfWavePlate.create(Math.PI / 8).getMatrix();
    Matrix<Complex> MH_N22_5 = HalfWavePlate.create(-Math.PI / 8).getMatrix();
    Matrix<Complex> phiMHHV = MH_0.times(MQ_0).times(MQ_0).times(phiHAfter);
    Matrix<Complex> phiMHDA = MH_22_5.times(MQ_N45).times(MQ_N45).times(phiHAfter);
    Matrix<Complex> phiMHLR = MH_N22_5.times(MQ_N45).times(MQ_0).times(phiHAfter);
    Matrix<Complex> phiMDHV = MH_0.times(MQ_0).times(MQ_0).times(phiDAfter);
    Matrix<Complex> phiMDDA = MH_22_5.times(MQ_N45).times(MQ_N45).times(phiDAfter);
    Matrix<Complex> phiMDLR = MH_N22_5.times(MQ_N45).times(MQ_0).times(phiDAfter);
    double mHH = Math.pow(phiMHHV.get(0, 0).magnitude(), 2);
    double mHV = Math.pow(phiMHHV.get(1, 0).magnitude(), 2);
    double mHD = Math.pow(phiMHDA.get(0, 0).magnitude(), 2);
    double mHA = Math.pow(phiMHDA.get(1, 0).magnitude(), 2);
    double mHL = Math.pow(phiMHLR.get(0, 0).magnitude(), 2);
    double mHR = Math.pow(phiMHLR.get(1, 0).magnitude(), 2);
    double mDH = Math.pow(phiMDHV.get(0, 0).magnitude(), 2);
    double mDV = Math.pow(phiMDHV.get(1, 0).magnitude(), 2);
    double mDD = Math.pow(phiMDDA.get(0, 0).magnitude(), 2);
    double mDA = Math.pow(phiMDDA.get(1, 0).magnitude(), 2);
    double mDL = Math.pow(phiMDLR.get(0, 0).magnitude(), 2);
    double mDR = Math.pow(phiMDLR.get(1, 0).magnitude(), 2);
    return new double[]{mHH, mHV, mHD, mHA, mHL, mHR, mDH, mDV, mDD, mDA, mDL, mDR};
  }

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
