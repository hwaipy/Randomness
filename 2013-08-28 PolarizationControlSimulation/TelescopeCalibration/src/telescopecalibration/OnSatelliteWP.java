/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telescopecalibration;

import com.hwaipy.science.polarizationcontrol.m1.M1Process;
import com.hwaipy.science.polarizationcontrol.m1.M1ProcessException;
import java.util.Arrays;
import java.util.Random;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;
import simulationconsole.entanglement.HalfWavePlate;
import simulationconsole.entanglement.QuarterWavePlate;

/**
 *
 * @author hwaipy
 */
public class OnSatelliteWP {

    private static final boolean isMT = true;

    private static final double[] zeros = isMT ? new double[]{90.05, 146.58, 99.99} : new double[]{125.42, 174.72, 137.86};
//        double[] comp = isMT ? new double[]{80.1, 127.5, 61.4} : new double[]{115.3, -89.2, 138.2};//2016.10.2
    private static final double[] comp = isMT ? new double[]{78.968, 127.045, 60.765} : new double[]{111.716, -95.458, 136.858};//2017.3.1
    private static final double[] compLogic = new double[3];
    private final Matrix<Complex> fibreMatrix;

    public static final void main(String[] args) throws M1ProcessException {
        new OnSatelliteWP().process();
    }

    public OnSatelliteWP() {
        System.out.println("Initial State for HVDA measurement: ");
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
        fibreMatrix = compMatrix.inverse();
    }

    public void process() throws M1ProcessException {
        double[] aoWP = calcWPForExtraQWP();

        while (true) {
            double[] newWP = searchRandom(aoWP);
            if (newWP != null) {
                double[] distances = new double[3];
                double distance = 0;
                for (int i = 0; i < distances.length; i++) {
                    distances[i] = distance(newWP[i], compLogic[i]);
                    distance += distances[i];
                }
                double[] newWPDeg = new double[3];
                double[] newWPScale = new double[3];
                for (int i = 0; i < newWP.length; i++) {
                    newWPDeg[i] = newWP[i] / Math.PI * 180;
                    newWPScale[i] = newWPDeg[i] + zeros[i];
                }
                System.out.println(distance + "\t" + Arrays.toString(distances) + "\t" + Arrays.toString(newWP) + "\t" + Arrays.toString(newWPDeg) + "\t" + Arrays.toString(newWPScale));
            }
        }

        //        System.out.println(HalfWavePlate.create(newWP[2]).getMatrix()
//                .times(QuarterWavePlate.create(newWP[1]).getMatrix())
//                .times(QuarterWavePlate.create(newWP[0]).getMatrix())
//                .times(fibreMatrix).times(Complex.valueOf(-1, 0)));
//
//        System.out.println();
//
//        for (int i = 0; i < 3; i++) {
//            aoWP[i] += zeros[i] / 180 * Math.PI;
//        }
//
//        for (double d : aoWP) {
//            System.out.println(d / Math.PI * 180);
//        }
//        System.out.println("-----");
//        for (int i = 0; i < 3; i++) {
//            System.out.println(aoWP[i] / Math.PI * 180 - comp[i]);
//        }
    }

    private double[] calcWPForExtraQWP() throws M1ProcessException {
        QuarterWavePlate extQWP = QuarterWavePlate.create(Math.PI / 4);
        double[] measurements = calculateMeasurements(fibreMatrix.times(extQWP.getMatrix()));
//        double[] measurements = calculateMeasurements(fibreMatrix);
        return calculateAngleOfWP(measurements);
    }

    private double[] search(double[] target, double[] current) {
        AnnealingTest p = new AnnealingTest(0.99999, 4048, current, target);
        AnnealingTest.Result result = p.process();
        if (!result.success) {
            return null;
        }
        return result.angles;
    }

    private double[] searchRandom(double[] target) {
        Random random = new Random();
        double[] start = random.doubles(3, -Math.PI, Math.PI).toArray();
        return search(target, start);
    }

    private double[] calculateMeasurements(Matrix<Complex> U) {
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

    private double[] calculateAngleOfWP(double[] v12) throws M1ProcessException {
        M1Process m1 = M1Process.calculate(v12);
        return m1.getResults();
    }

    private double distance(double a, double b) {
        double d = Math.abs(a - b);
        while (d >= Math.PI * 2) {
            d -= Math.PI * 2;
        }
        if (d > Math.PI) {
            d = Math.PI * 2 - d;
        }
        return d;
    }
}
