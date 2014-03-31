package com.hwaipy.science.polarizationcontrol.m1;

import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HwaipyLab
 */
public class AccuracyEstimate {

    private static final double theta1 = new Random().nextDouble() * Math.PI;
    private static final double theta2 = new Random().nextDouble() * Math.PI;
    private static final double theta3 = new Random().nextDouble() * Math.PI;
    private static final double delayPrecisionQwp1 = 1. / 200;
    private static final double delayPrecisionQwp2 = 1. / 200;
    private static final double delayPrecisionHwp = 1. / 200;

    public static void main(String[] args) {
        FiberTransform ft = FiberTransform.createReverse(theta1 + Math.PI / 2, theta2 + Math.PI / 2, theta3 + Math.PI / 2);
        WavePlate qwp1 = new WavePlate(Math.PI * 2 * (0.25 + delayPrecisionQwp1), 0);
        WavePlate qwp2 = new WavePlate(Math.PI * 2 * (0.25 + delayPrecisionQwp2), 0);
        WavePlate hwp = new WavePlate(Math.PI * 2 * (0.5 + delayPrecisionHwp), 0);

        Polarization measurementH1 = Polarization.H.transform(ft)
                .transform(qwp1).transform(qwp2).transform(hwp);
        double mHH = measurementH1.getH();
        double mHV = measurementH1.getV();
        double mHD = measurementH1.getD();
        double mHA = measurementH1.getA();
        Polarization measurementD1 = Polarization.D.transform(ft)
                .transform(qwp1).transform(qwp2).transform(hwp);
        double mDH = measurementD1.getH();
        double mDV = measurementD1.getV();
        double mDD = measurementD1.getD();
        double mDA = measurementD1.getA();
        qwp2.increase(-Math.PI / 4);
        hwp.increase(-Math.PI / 8);
        Polarization measurementH2 = Polarization.H.transform(ft)
                .transform(qwp1).transform(qwp2).transform(hwp);
        double mHL = measurementH2.getH();
        double mHR = measurementH2.getV();
        Polarization measurementD2 = Polarization.D.transform(ft)
                .transform(qwp1).transform(qwp2).transform(hwp);
        double mDL = measurementD2.getH();
        double mDR = measurementD2.getV();

        M1Process m1Process = null;
        try {
            m1Process = M1Process.calculate(new double[]{mHH, mHV, mHD, mHA, mHL, mHR, mDH, mDV, mDD, mDA, mDL, mDR});
        } catch (M1ProcessException ex) {
            Logger.getLogger(AccuracyEstimate.class.getName()).log(Level.SEVERE, null, ex);
        }
        double[] result = m1Process.getResults();
        System.out.println("Theta1 = " + result[0] / Math.PI * 180);
        System.out.println("Theta2 = " + result[1] / Math.PI * 180);
        System.out.println("Theta3 = " + result[2] / Math.PI * 180);

        qwp1.setTheta(result[0]);
        qwp2.setTheta(result[1]);
        hwp.setTheta(result[2]);
        Polarization resultH = Polarization.H.transform(ft).transform(qwp1).transform(qwp2).transform(hwp);
        Polarization resultD = Polarization.D.transform(ft).transform(qwp1).transform(qwp2).transform(hwp);
        System.out.println(resultH.getH() / resultH.getV());
        System.out.println(resultD.getD() / resultD.getA());
    }
}
