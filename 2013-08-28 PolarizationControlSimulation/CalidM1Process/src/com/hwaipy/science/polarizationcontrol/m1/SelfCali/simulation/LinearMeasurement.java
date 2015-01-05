package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class LinearMeasurement implements Measurement {

    private final double Delta;
    private final double delta;

    public LinearMeasurement(double Delta, double delta) {
        this.Delta = Delta;
        this.delta = delta;
        if (delta < 0) {
            throw new RuntimeException();
        }
    }

    @Override
    public double measurement(double power) {
        return power * (1 + Delta) + delta;
    }

    public static MeasurementFactory<LinearMeasurement> getRandomFactory(final double maxDelta, final double maxdelta) {
        return new MeasurementFactory<LinearMeasurement>() {
            private final Random random = new Random(0);

            @Override
            public LinearMeasurement createMeasurement() {
                double Delta = (2 * random.nextDouble() - 1) * maxDelta;
                double delta = random.nextDouble() * maxdelta;
                return new LinearMeasurement(Delta, delta);
            }
        };
    }
}
