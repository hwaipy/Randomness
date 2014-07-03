package com.hwaipy.physics.crystaloptics.refractivemodel;

/**
 *
 * @author Hwaipy
 */
public interface RefractiveModel {

    /**
     *
     * @param waveLength in MicroMetre
     * @return
     */
    public double getIndex(double waveLength);

    /**
     *
     * @param waveLength in MicroMetre
     * @return
     */
    public double getGroupIndex(double waveLength);
}
