package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class MNMeasurement implements Measurement {

    private static final double M = 0.06;
    private static final double N = 8e-5 * 500;
    private final double DeltaM;
    private final double DeltaN;

    public MNMeasurement(double DeltaM, double DeltaN) {
        this.DeltaM = DeltaM;
        this.DeltaN = DeltaN;
    }

    @Override
    public double measurement(double power) {
        return M * power / (M * (1 + DeltaM) + N * DeltaN * power);
    }

    public static MeasurementFactory<MNMeasurement> getRandomFactory(final double maxDeltaM, final double maxDeltaN) {
        return new MeasurementFactory<MNMeasurement>() {
            private final Random random = new Random();

            @Override
            public MNMeasurement createMeasurement() {
                double DeltaM = (2 * random.nextDouble() - 1) * maxDeltaM;
                double DeltaN = (2 * random.nextDouble() - 1) * maxDeltaN;
                return new MNMeasurement(DeltaM, DeltaN);
            }
        };
    }
}
