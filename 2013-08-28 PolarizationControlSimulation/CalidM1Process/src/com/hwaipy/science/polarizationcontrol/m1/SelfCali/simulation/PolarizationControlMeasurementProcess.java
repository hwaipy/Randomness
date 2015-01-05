package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

import com.hwaipy.science.polarizationcontrol.device.Polarization;

/**
 *
 * @author Hwaipy
 */
public interface PolarizationControlMeasurementProcess {

    public double[] measurement(Polarization p);
}
