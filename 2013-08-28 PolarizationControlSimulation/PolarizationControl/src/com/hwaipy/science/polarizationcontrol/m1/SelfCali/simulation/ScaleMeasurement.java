package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

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
}
