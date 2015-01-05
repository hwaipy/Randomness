package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;

/**
 *
 * @author Hwaipy
 */
public class M6MeasurementProcess extends AbstractMeasurementProcess {

    private final WavePlate qwp1 = new WavePlate(Math.PI / 2, 0);
    private final WavePlate qwp2 = new WavePlate(Math.PI / 2, 0);
    private final WavePlate hwp = new WavePlate(Math.PI, 0);

    public M6MeasurementProcess(MeasurementFactory measurementFactory) {
        super(measurementFactory);
    }

    @Override
    public double[] measurement(Polarization p) {
        double[] result = new double[6];
        qwp1.setTheta(0);
        qwp2.setTheta(0);
        hwp.setTheta(0);
        Polarization m1 = p.transform(qwp1).transform(qwp2).transform(hwp);
        for (int i = 0; i < 4; i++) {
            result[i] = measurement(m1, i);
        }

        qwp2.increase(-Math.PI / 4);
        hwp.increase(-Math.PI / 8);

        Polarization m2 = p.transform(qwp1).transform(qwp2).transform(hwp);
        for (int i = 0; i < 2; i++) {
            result[i + 4] = measurement(m2, i);
        }
        return result;
    }
}
