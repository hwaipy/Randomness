package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.entanglement;

/**
 *
 * @author Hwaipy
 */
public interface EntanglementPolarizationControlMeasurementProcess {

    public double[] measurement(EntanglementModel model);

    public void generateNewMeasurement();
}
