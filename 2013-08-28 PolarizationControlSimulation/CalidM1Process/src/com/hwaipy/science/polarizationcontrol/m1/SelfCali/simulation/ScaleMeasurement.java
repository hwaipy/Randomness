package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class ScaleMeasurement implements Measurement {

    private final double Delta;

    public ScaleMeasurement(double Delta) {
        this.Delta = Delta;
    }

    @Override
    public double measurement(double power) {
        return power * (1 + Delta);
    }

    public static MeasurementFactory<ScaleMeasurement> getRandomFactory(final double maxDelta) {
        return new MeasurementFactory<ScaleMeasurement>() {
            private final Random random = new Random();

            @Override
            public ScaleMeasurement createMeasurement() {
                double Delta = (2 * random.nextDouble() - 1) * maxDelta;
                return new ScaleMeasurement(Delta);
            }
        };
    }
}
