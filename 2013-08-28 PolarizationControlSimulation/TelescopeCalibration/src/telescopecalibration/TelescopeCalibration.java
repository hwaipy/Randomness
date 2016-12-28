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
public class TelescopeCalibration {

  private static final double PA = 0.0001;
  private static final double PB = 0.0001;
  private static final double PC = 0.419646965349516;
  private static final double RV = 0.0001;
  private static final double RH = 0.0001;

  public static void main(String[] args) throws M1ProcessException {
    TelescopeCalibration tc = new TelescopeCalibration();
    ComplexMatrix UT = tc.makeTelescopeU();
    System.out.println(UT);

    double[] v12 = tc.calculateMeasurements(UT);
//    System.out.println(Arrays.toString(v12));
    double[] awps = tc.calculateAngleOfWP(v12);
    Matrix<Complex> UWP = tc.calculateUWP(awps);

    System.out.println(UWP);

    Matrix<Complex> UFinal = UWP.times(UT);

    System.out.println(UFinal);

    double uniPha = -0.695;
    Matrix<Complex> uU = UFinal.times(Complex.valueOf(Math.cos(uniPha), Math.sin(uniPha)));
//    System.out.println(uU);
//    System.out.println(UT);
//    System.out.println(Arrays.toString(awps));
//    System.out.println(UFinal);
//    System.out.println(Arrays.toString(tc.calculateMeasurements(ComplexMatrix.valueOf(UFinal))));
//
//    System.out.println(Arrays.toString(awps));
//    System.out.println("QWP1 = " + (awps[0] / Math.PI * 180));
//    System.out.println("QWP2 = " + (awps[1] / Math.PI * 180));
//    System.out.println("HWP  = " + (awps[2] / Math.PI * 180));
//    System.out.println();
    System.out.println("QWP1 @ " + (awps[0] / Math.PI * 180 - 15.1));
    System.out.println("QWP2 @ " + (awps[1] / Math.PI * 180 + 51.3));
    System.out.println("HWP  @ " + (awps[2] / Math.PI * 180 - 15.3));
//    System.out.println();
//    System.out.println();
//    System.out.println(UWP);
//
//    double uniPha = -0.695;
//    Matrix<Complex> uU = UWP.times(Complex.valueOf(Math.cos(uniPha), Math.sin(uniPha)));
//    System.out.println(")))");
//    System.out.println(uU);
//    System.out.println(uU.get(1, 1).getReal() / uU.get(1, 1).getImaginary());
//
//    System.out.println("-----");
//    double p = 4.893;
//    System.out.println(Complex.valueOf(Math.cos(p), Math.sin(p)));
//    System.out.println(1 / Math.tan(p));
  }

  private ComplexMatrix makeTelescopeU() {
    ComplexMatrix UA = makePhaseU(PA);
    ComplexMatrix UB = makePhaseU(PB);
    ComplexMatrix UC = makePhaseU(PC);
    ComplexMatrix URV = makeRotateU(RV);
    ComplexMatrix URH = makeRotateU(RH);
    return UC.times(URH).times(UB).times(URV).times(UA);
  }

  private ComplexMatrix makePhaseU(double phase) {
    return ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE, Complex.ZERO},
    {Complex.ZERO, Complex.valueOf(Math.cos(phase), Math.sin(phase))}});
  }

  private ComplexMatrix makeRotateU(double theta) {
    Complex c = Complex.valueOf(Math.cos(theta), 0);
    Complex s = Complex.valueOf(Math.sin(theta), 0);
    return ComplexMatrix.valueOf(new Complex[][]{{c, s}, {s.opposite(), c}});
  }

  private double[] calculateMeasurements(ComplexMatrix U) {
    ComplexMatrix mH = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE, Complex.ZERO}});
//    ComplexMatrix mV = ComplexMatrix.valueOf(new Complex[][]{{Complex.ZERO, Complex.ONE}});
    ComplexMatrix mD = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2)), Complex.ONE.divide(Math.sqrt(2))}});
//    ComplexMatrix mA = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2)), Complex.ONE.divide(-Math.sqrt(2))}});
//    ComplexMatrix mL = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2)), Complex.I.divide(Math.sqrt(2))}});
//    ComplexMatrix mR = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2)), Complex.I.divide(-Math.sqrt(2))}});
    ComplexMatrix phiHAfter = U.times(mH.transpose());
    ComplexMatrix phiDAfter = U.times(mD.transpose());
//    double mHH = Math.pow(mH.times(phiHAfter).get(0, 0).magnitude(), 2);
//    double mHV = Math.pow(mV.times(phiHAfter).get(0, 0).magnitude(), 2);
//    double mHD = Math.pow(mD.times(phiHAfter).get(0, 0).magnitude(), 2);
//    double mHA = Math.pow(mA.times(phiHAfter).get(0, 0).magnitude(), 2);
//    double mHL = Math.pow(mL.times(phiHAfter).get(0, 0).magnitude(), 2);
//    double mHR = Math.pow(mR.times(phiHAfter).get(0, 0).magnitude(), 2);
//    double mDH = Math.pow(mH.times(phiDAfter).get(0, 0).magnitude(), 2);
//    double mDV = Math.pow(mV.times(phiDAfter).get(0, 0).magnitude(), 2);
//    double mDD = Math.pow(mD.times(phiDAfter).get(0, 0).magnitude(), 2);
//    double mDA = Math.pow(mA.times(phiDAfter).get(0, 0).magnitude(), 2);
//    double mDL = Math.pow(mL.times(phiDAfter).get(0, 0).magnitude(), 2);
//    double mDR = Math.pow(mR.times(phiDAfter).get(0, 0).magnitude(), 2);
//    return new double[]{mHH, mHV, mHD, mHA, mHL, mHR, mDH, mDV, mDD, mDA, mDL, mDR};

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

  private double[] calculateAngleOfWP(double[] v12) throws M1ProcessException {
    M1Process m1 = M1Process.calculate(v12);
    return m1.getResults();
  }

  private Matrix<Complex> calculateUWP(double[] awps) {
    Matrix<Complex> Q1 = QuarterWavePlate.create(awps[0]).getMatrix();
    Matrix<Complex> Q2 = QuarterWavePlate.create(awps[1]).getMatrix();
    Matrix<Complex> H = HalfWavePlate.create(awps[2]).getMatrix();
    return H.times(Q2).times(Q1);
  }
}
