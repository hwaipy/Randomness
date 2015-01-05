package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

/**
 *
 * @author Hwaipy
 * @param <T>
 */
public interface MeasurementFactory<T extends Measurement> {

    public T createMeasurement();
}
