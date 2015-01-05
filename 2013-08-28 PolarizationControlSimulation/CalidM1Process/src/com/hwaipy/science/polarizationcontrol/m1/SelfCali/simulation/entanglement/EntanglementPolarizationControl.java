package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.entanglement;

import com.hwaipy.science.polarizationcontrol.m1.M1Process;
import com.hwaipy.science.polarizationcontrol.m1.M1ProcessException;
import java.util.Arrays;

/**
 *
 * @author Hwaipy
 */
public class EntanglementPolarizationControl {

    private final EntanglementPolarizationControlMeasurementProcess measurementProcess;

    public EntanglementPolarizationControl(EntanglementPolarizationControlMeasurementProcess measurementProcess) {
        this.measurementProcess = measurementProcess;
    }

    public double control(EntanglementModel model) {
        double[] m = measurementProcess.measurement(model);
//        System.out.println(Arrays.toString(m));

        M1Process m1Process = null;
        try {
            m1Process = M1Process.calculate(m);
        } catch (M1ProcessException ex) {
            ex.printStackTrace(System.err);
        }
        if (m1Process != null) {
            double[] result = m1Process.getResults();
//            System.out.println(Arrays.toString(result));

            model.set(2, 1, result[0]);
            model.set(2, 2, result[1]);
            model.set(2, 3, result[2]);
            double[][] coincidences = model.readCoindicences();
            double cHV = (coincidences[0][1] + coincidences[1][0]) / (coincidences[0][0] + coincidences[1][1]);
            double cDA = (coincidences[2][3] + coincidences[3][2]) / (coincidences[2][2] + coincidences[3][3]);
            return Math.min(cHV, cDA);
        }
        return 1;
    }

    public void generateNewMeasurement() {
        measurementProcess.generateNewMeasurement();
    }
}
