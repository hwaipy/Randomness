package com.hwaipy.physics.crystaloptics;

import com.hwaipy.physics.crystaloptics.refractivemodel.RefractiveModel;
import javax.measure.unit.Units;

/**
 *
 * @author Hwaipy
 */
public class Medium {

    private final RefractiveModel[] refractiveModels;

    public Medium(RefractiveModel refractiveModelX, RefractiveModel refractiveModelY, RefractiveModel refractiveModelZ) {
        this.refractiveModels = new RefractiveModel[]{refractiveModelX, refractiveModelY, refractiveModelZ};
    }

    public double getIndex(MonochromaticWave monochromaticWave, Axis axis) {
        RefractiveModel model = refractiveModels[axis.ordinal()];
        return model.getIndex(monochromaticWave.getWaveLength().doubleValue(Units.MICROMETRE));
    }

    public double getGroupIndex(MonochromaticWave monochromaticWave, Axis axis) {
        RefractiveModel model = refractiveModels[axis.ordinal()];
        return model.getGroupIndex(monochromaticWave.getWaveLength().doubleValue(Units.MICROMETRE));
    }
}
