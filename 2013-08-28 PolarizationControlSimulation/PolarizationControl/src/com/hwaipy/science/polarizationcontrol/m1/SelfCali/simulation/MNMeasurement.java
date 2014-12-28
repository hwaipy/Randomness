package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

/**
 *
 * @author Hwaipy
 */
public class MNMeasurement implements Measurement {

    private static final double M = 0.06;
    private static final double N = 8e-5;
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
}
