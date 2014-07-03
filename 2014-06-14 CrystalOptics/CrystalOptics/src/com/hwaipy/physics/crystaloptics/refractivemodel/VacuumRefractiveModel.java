package com.hwaipy.physics.crystaloptics.refractivemodel;

/**
 *
 * @author Hwaipy
 */
public class VacuumRefractiveModel implements RefractiveModel {

    @Override
    public double getIndex(double waveLength) {
        return 1;
    }

    @Override
    public double getGroupIndex(double waveLength) {
        return 1;
    }
    public static VacuumRefractiveModel INSTANCE = new VacuumRefractiveModel();
}
