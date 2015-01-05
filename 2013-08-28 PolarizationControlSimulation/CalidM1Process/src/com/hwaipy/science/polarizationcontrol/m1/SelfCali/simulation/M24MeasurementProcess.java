package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;

/**
 *
 * @author Hwaipy
 */
public class M24MeasurementProcess extends AbstractMeasurementProcess {

    private final WavePlate qwp1 = new WavePlate(Math.PI / 2, 0);
    private final WavePlate qwp2 = new WavePlate(Math.PI / 2, 0);
    private final WavePlate hwp = new WavePlate(Math.PI, 0);

    public M24MeasurementProcess(MeasurementFactory measurementFactory) {
        super(measurementFactory);
    }

    @Override
    public double[] measurement(Polarization p) {
        double[] result = new double[6];

        qwp1.setTheta(Math.PI / 4);
        qwp2.setTheta(0);
        hwp.setTheta(0);
        Polarization m1 = p.transform(qwp1).transform(qwp2).transform(hwp);
        result[4] += measurement(m1, 0);
        result[5] += measurement(m1, 1);
        result[1] += measurement(m1, 2);
        result[0] += measurement(m1, 3);

        hwp.increase(Math.PI / 8);
        Polarization m2 = p.transform(qwp1).transform(qwp2).transform(hwp);
        result[0] += measurement(m2, 0);
        result[1] += measurement(m2, 1);
        result[4] += measurement(m2, 2);
        result[5] += measurement(m2, 3);

        qwp1.increase(-Math.PI / 4);
        Polarization m3 = p.transform(qwp1).transform(qwp2).transform(hwp);
        result[3] += measurement(m3, 0);
        result[2] += measurement(m3, 1);
        result[0] += measurement(m3, 2);
        result[1] += measurement(m3, 3);

        qwp2.increase(-Math.PI / 4);
        Polarization m4 = p.transform(qwp1).transform(qwp2).transform(hwp);
        result[5] += measurement(m4, 0);
        result[4] += measurement(m4, 1);
        result[3] += measurement(m4, 2);
        result[2] += measurement(m4, 3);

        hwp.increase(Math.PI / 8);
        Polarization m5 = p.transform(qwp1).transform(qwp2).transform(hwp);
        result[2] += measurement(m5, 0);
        result[3] += measurement(m5, 1);
        result[5] += measurement(m5, 2);
        result[4] += measurement(m5, 3);

        qwp2.increase(-Math.PI / 4);
        Polarization m6 = p.transform(qwp1).transform(qwp2).transform(hwp);
        result[1] += measurement(m6, 0);
        result[0] += measurement(m6, 1);
        result[2] += measurement(m6, 2);
        result[3] += measurement(m6, 3);

        for (int i = 0; i < 6; i++) {
            result[i] /= 4;
        }
        return result;
    }
}
