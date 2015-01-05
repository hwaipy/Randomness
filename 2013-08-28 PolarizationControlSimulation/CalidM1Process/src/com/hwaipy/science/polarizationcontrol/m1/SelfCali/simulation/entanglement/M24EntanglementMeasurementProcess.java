package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.entanglement;

import com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.*;

/**
 *
 * @author Hwaipy
 */
public class M24EntanglementMeasurementProcess extends AbstractEntanglementMeasurementProcess {

    public M24EntanglementMeasurementProcess(MeasurementFactory measurementFactory) {
        super(measurementFactory);
    }

    @Override
    public double[] measurement(EntanglementModel model) {
        double[] result = new double[12];

        model.set(2, 1, Math.PI / 4);
        model.set(2, 2, 0);
        model.set(2, 3, 0);
        double[][] m1 = directMeasurement(model);
        result[4] += m1[0][1] + m1[1][0];
        result[5] += m1[0][0] + m1[1][1];
        result[1] += m1[0][3] + m1[1][2];
        result[0] += m1[0][2] + m1[1][3];
        result[10] += m1[2][1] + m1[3][0];
        result[11] += m1[2][0] + m1[3][1];
        result[7] += m1[2][3] + m1[3][2];
        result[6] += m1[2][2] + m1[3][3];

        model.set(2, 3, Math.PI / 8);
        double[][] m2 = directMeasurement(model);
        result[0] += m2[0][1] + m2[1][0];
        result[1] += m2[0][0] + m2[1][1];
        result[4] += m2[0][3] + m2[1][2];
        result[5] += m2[0][2] + m2[1][3];
        result[6] += m2[2][1] + m2[3][0];
        result[7] += m2[2][0] + m2[3][1];
        result[10] += m2[2][3] + m2[3][2];
        result[11] += m2[2][2] + m2[3][3];

        model.set(2, 1, 0);
        double[][] m3 = directMeasurement(model);
        result[3] += m3[0][1] + m3[1][0];
        result[2] += m3[0][0] + m3[1][1];
        result[0] += m3[0][3] + m3[1][2];
        result[1] += m3[0][2] + m3[1][3];
        result[9] += m3[2][1] + m3[3][0];
        result[8] += m3[2][0] + m3[3][1];
        result[6] += m3[2][3] + m3[3][2];
        result[7] += m3[2][2] + m3[3][3];

        model.set(2, 2, -Math.PI / 4);
        double[][] m4 = directMeasurement(model);
        result[5] += m4[0][1] + m4[1][0];
        result[4] += m4[0][0] + m4[1][1];
        result[3] += m4[0][3] + m4[1][2];
        result[2] += m4[0][2] + m4[1][3];
        result[11] += m4[2][1] + m4[3][0];
        result[10] += m4[2][0] + m4[3][1];
        result[9] += m4[2][3] + m4[3][2];
        result[8] += m4[2][2] + m4[3][3];

        model.set(2, 3, Math.PI / 4);
        double[][] m5 = directMeasurement(model);
        result[2] += m5[0][1] + m5[1][0];
        result[3] += m5[0][0] + m5[1][1];
        result[5] += m5[0][3] + m5[1][2];
        result[4] += m5[0][2] + m5[1][3];
        result[8] += m5[2][1] + m5[3][0];
        result[9] += m5[2][0] + m5[3][1];
        result[11] += m5[2][3] + m5[3][2];
        result[10] += m5[2][2] + m5[3][3];

        model.set(2, 2, -Math.PI / 2);
        double[][] m6 = directMeasurement(model);
        result[1] += m6[0][1] + m6[1][0];
        result[0] += m6[0][0] + m6[1][1];
        result[2] += m6[0][3] + m6[1][2];
        result[3] += m6[0][2] + m6[1][3];
        result[7] += m6[2][1] + m6[3][0];
        result[6] += m6[2][0] + m6[3][1];
        result[8] += m6[2][3] + m6[3][2];
        result[9] += m6[2][2] + m6[3][3];

        for (int i = 0; i < 12; i++) {
            result[i] /= 4;
        }
        return result;
    }
}
