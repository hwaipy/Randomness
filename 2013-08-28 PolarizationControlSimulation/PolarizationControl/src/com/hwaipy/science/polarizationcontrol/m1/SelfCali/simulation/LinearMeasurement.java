package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

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
    }

    @Override
    public double measurement(double power) {
        return power * (1 + Delta) + delta;
    }
}
