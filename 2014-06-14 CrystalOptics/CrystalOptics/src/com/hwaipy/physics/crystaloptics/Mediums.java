package com.hwaipy.physics.crystaloptics;

import com.hwaipy.physics.crystaloptics.refractivemodel.RefractiveModel;
import com.hwaipy.physics.crystaloptics.refractivemodel.VacuumRefractiveModel;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hwaipy
 */
public class Mediums {

    private static final Map<String, Medium> MEDIUMS = new HashMap<>();
    public static Medium VACUUM = newMedium("vacuum",
            VacuumRefractiveModel.INSTANCE,
            VacuumRefractiveModel.INSTANCE,
            VacuumRefractiveModel.INSTANCE);

    private static Medium newMedium(String name, RefractiveModel refractiveModelX,
            RefractiveModel refractiveModelY, RefractiveModel refractiveModelZ) {
        Medium medium = new Medium(refractiveModelX, refractiveModelY, refractiveModelZ);
        MEDIUMS.put(name, medium);
        return medium;
    }
}
