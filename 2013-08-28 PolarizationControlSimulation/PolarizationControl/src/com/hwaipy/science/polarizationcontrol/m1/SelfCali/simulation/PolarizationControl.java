package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import com.hwaipy.science.polarizationcontrol.device.HalfWavePlate;
import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import com.hwaipy.science.polarizationcontrol.m1.M1Process;
import com.hwaipy.science.polarizationcontrol.m1.M1ProcessException;

/**
 *
 * @author Hwaipy
 */
public class PolarizationControl {

    private final PolarizationControlMeasurementProcess measurementProcess;
    private final WavePlate qwp1 = new WavePlate(Math.PI / 2, 0);
    private final WavePlate qwp2 = new WavePlate(Math.PI / 2, 0);
    private final WavePlate hwp = new WavePlate(Math.PI, 0);

    public PolarizationControl(PolarizationControlMeasurementProcess measurementProcess) {
        this.measurementProcess = measurementProcess;
    }

    public double control(FiberTransform fiberTransform) {
        Polarization pH = Polarization.H.transform(fiberTransform);
        Polarization pD = Polarization.D.transform(fiberTransform);
        double[] mH = measurementProcess.measurement(pH);
        double[] mD = measurementProcess.measurement(pD);

        M1Process m1Process = null;
        double cH = 0;
        double cD = 0;
        try {
            m1Process = M1Process.calculate(new double[]{
                mH[0], mH[1], mH[2], mH[3], mH[4], mH[5], mD[0], mD[1], mD[2], mD[3], mD[4], mD[5]});
        } catch (M1ProcessException ex) {
            ex.printStackTrace(System.err);
        }
        if (m1Process != null) {
            double[] result = m1Process.getResults();
            qwp1.setTheta(result[0]);
            qwp2.setTheta(result[1]);
            hwp.setTheta(result[2]);
            Polarization resultH = Polarization.H.transform(fiberTransform).transform(qwp1).transform(qwp2).transform(hwp);
            Polarization resultD = Polarization.D.transform(fiberTransform).transform(qwp1).transform(qwp2).transform(hwp);
            cH = measurement(resultH, 0) / measurement(resultH, 1);
            cD = measurement(resultD, 2) / measurement(resultD, 3);
        }
        return Math.min(cH, cD);
    }

    private double measurement(Polarization state, int thetaIndex) {
        double[] thetas = {0, 90, 45, -45};
        double polTheta = thetas[thetaIndex];
        HalfWavePlate rotate = new HalfWavePlate(polTheta / 2 / 180 * Math.PI);
        Polarization finalState = state.transform(rotate);
        return finalState.getH();
    }
}
