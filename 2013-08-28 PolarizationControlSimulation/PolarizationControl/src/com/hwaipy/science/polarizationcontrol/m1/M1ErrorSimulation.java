package com.hwaipy.science.polarizationcontrol.m1;

import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import com.hwaipy.science.polarizationcontrol.device.HalfWavePlate;
import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import java.util.Random;

/**
 *
 * @author HwaipyLab
 */
public class M1ErrorSimulation {

    private final Random random;
    private double fiberTransformTheta1;
    private double fiberTransformTheta2;
    private double fiberTransformTheta3;

    public M1ErrorSimulation() {
        random = new Random();
    }

    public void generateRandomFiberTransform() {
        fiberTransformTheta1 = (random.nextDouble() - 0.5) * Math.PI;
        fiberTransformTheta2 = (random.nextDouble() - 0.5) * Math.PI;
        fiberTransformTheta3 = (random.nextDouble() - 0.5) * Math.PI;
    }

    private double[] thetas = {0, 90, 45, -45};
    private double overallMeasurementError = 0.00;
    private double[] measurementAccuracies = {0.00, 0.00, 0.00, 0.00};

    public double measurement(Polarization state, int thetaIndex, boolean withError) {
        double polTheta = thetas[thetaIndex];
        HalfWavePlate rotate = new HalfWavePlate(polTheta / 2 / 180 * Math.PI);
        Polarization finalState = state.transform(rotate);
        double m = finalState.getH();
        if (withError) {
            m *= 1 + measurementAccuracies[thetaIndex];
        }
        return m;
    }

    public M1SimulationResult calculate() {
        FiberTransform ft = FiberTransform.createReverse(fiberTransformTheta1 + Math.PI / 2, fiberTransformTheta2 + Math.PI / 2, fiberTransformTheta3 + Math.PI / 2);
//        FiberTransform afterError = FiberTransform.createRandomFiber(random);
//        MuellerMatrix afterError = new WavePlate(Math.PI * 0.1, Math.PI * 0.1);
        WavePlate qwp1 = new WavePlate(Math.PI / 2, 0);
        WavePlate qwp2 = new WavePlate(Math.PI / 2, 0);
        WavePlate hwp = new WavePlate(Math.PI, 0);

        Polarization measurementH1 = Polarization.H.transform(ft)
                .transform(qwp1).transform(qwp2).transform(hwp);
//        double mHH = measurementH1.getH();
//        double mHV = measurementH1.getV();
//        double mHD = measurementH1.getD();
//        double mHA = measurementH1.getA();
        double mHH = measurement(measurementH1, 0, true);
        double mHV = measurement(measurementH1, 1, true);
        double mHD = measurement(measurementH1, 2, true);
        double mHA = measurement(measurementH1, 3, true);
        Polarization measurementD1 = Polarization.D.transform(ft)
                .transform(qwp1).transform(qwp2).transform(hwp);
//        double mDH = measurementD1.getH();
//        double mDV = measurementD1.getV();
//        double mDD = measurementD1.getD();
//        double mDA = measurementD1.getA();
        double mDH = measurement(measurementD1, 0, true);
        double mDV = measurement(measurementD1, 1, true);
        double mDD = measurement(measurementD1, 2, true);
        double mDA = measurement(measurementD1, 3, true);
        qwp2.increase(-Math.PI / 4);
        hwp.increase(-Math.PI / 8);
        Polarization measurementH2 = Polarization.H.transform(ft)
                .transform(qwp1).transform(qwp2).transform(hwp);
        double mHL = measurement(measurementH2, 0, true);
        double mHR = measurement(measurementH2, 1, true);
        Polarization measurementD2 = Polarization.D.transform(ft)
                .transform(qwp1).transform(qwp2).transform(hwp);
        double mDL = measurement(measurementD2, 0, true);
        double mDR = measurement(measurementD2, 1, true);

        M1Process m1Process = null;
        double cH = 0;
        double cD = 0;
        try {
            m1Process = M1Process.calculate(new double[]{mHH, mHV, mHD, mHA, mHL, mHR, mDH, mDV, mDD, mDA, mDL, mDR});
        } catch (M1ProcessException ex) {
        }
        if (m1Process != null) {
            double[] result = m1Process.getResults();
            qwp1.setTheta(result[0]);
            qwp2.setTheta(result[1]);
            hwp.setTheta(result[2]);
            Polarization resultH = Polarization.H.transform(ft).transform(qwp1).transform(qwp2).transform(hwp);
            Polarization resultD = Polarization.D.transform(ft).transform(qwp1).transform(qwp2).transform(hwp);
//            cH = resultH.getH() / resultH.getV();
//            cD = resultD.getD() / resultD.getA();
            cH = measurement(resultH, 0, false) / measurement(resultH, 1, false);
            cD = measurement(resultD, 2, false) / measurement(resultD, 3, false);
        }
        return new M1SimulationResult(m1Process != null, fiberTransformTheta1, fiberTransformTheta2, fiberTransformTheta3, cH, cD);
    }

    public class M1SimulationResult {

        private final boolean success;
        private final double fiberTheta1;
        private final double fiberTheta2;
        private final double fiberTheta3;
        private final double cH;
        private final double cD;

        public M1SimulationResult(boolean success, double fiberTheta1, double fiberTheta2, double fiberTheta3, double cH, double cD) {
            this.success = success;
            this.fiberTheta1 = fiberTheta1;
            this.fiberTheta2 = fiberTheta2;
            this.fiberTheta3 = fiberTheta3;
            this.cH = cH;
            this.cD = cD;
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        int[] dBs = new int[151];
        for (int count = 0; count < 100000; count++) {
            M1ErrorSimulation s = new M1ErrorSimulation();
            s.generateRandomFiberTransform();
            s.measurementAccuracies = new double[]{
                //                0.051, 0.009, 0.018, 0.034};
                (random.nextDouble() - 0.5) * 0.10,
                (random.nextDouble() - 0.5) * 0.10,
                (random.nextDouble() - 0.5) * 0.10,
                (random.nextDouble() - 0.5) * 0.10};
            M1SimulationResult result = s.calculate();
            double r = Math.min(result.cH, result.cD);
            double dB = Math.log10(r) * 10;
            int dBValue = Double.isFinite(dB) ? (int) dB : 150;
            if (dBValue > 150) {
                dBValue = 150;
            } else if (dBValue < 0) {
                dBValue = 0;
            }
            dBs[dBValue]++;
        }
        double sum = 0;
        for (int i = 0; i < dBs.length; i++) {
//            System.out.println(i + "\t" + dBs[i]);
//            System.out.println(dBs[i]);
            sum += dBs[i];
            System.out.println(sum / 1000);
        }
    }
}
