package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

import com.hwaipy.science.polarizationcontrol.device.HalfWavePlate;
import com.hwaipy.science.polarizationcontrol.device.Polarization;

/**
 *
 * @author Hwaipy
 */
public abstract class AbstractMeasurementProcess implements PolarizationControlMeasurementProcess {

    private final MeasurementFactory measurementFactory;
    private final Measurement[] measurements = new Measurement[4];

    public AbstractMeasurementProcess(MeasurementFactory measurementFactory) {
        this.measurementFactory = measurementFactory;
    }

    public void generateNewMeasurement() {
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = measurementFactory.createMeasurement();
        }
    }

    public double measurement(Polarization state, int thetaIndex) {
        double trueValue = getTrueValue(state, thetaIndex);
        double v = measurements[thetaIndex].measurement(trueValue);
        return v;
    }

    private double getTrueValue(Polarization state, int thetaIndex) {
        double[] thetas = {0, 90, 45, -45};
        double polTheta = thetas[thetaIndex];
        HalfWavePlate rotate = new HalfWavePlate(polTheta / 2 / 180 * Math.PI);
        Polarization finalState = state.transform(rotate);
        double m = finalState.getH();
        return m;
    }
}
