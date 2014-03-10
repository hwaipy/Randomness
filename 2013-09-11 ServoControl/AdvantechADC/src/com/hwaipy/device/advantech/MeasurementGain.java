package com.hwaipy.device.advantech;

import com.hwaipy.device.advantech.jna.GainListStructure;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Hwaipy Lab
 */
public class MeasurementGain {

    private final int code;
    private final double maxGainValue;
    private final double minGainValue;
    private final String description;

    MeasurementGain(GainListStructure gainListStructure) {
        code = gainListStructure.usGainCde;
        maxGainValue = gainListStructure.fMaxGainVal;
        minGainValue = gainListStructure.fMinGainVal;
        description = Util.createStringFromBytesArray(gainListStructure.szGainStr);
    }

    public int getCode() {
        return code;
    }

    public double getMaxGainValue() {
        return maxGainValue;
    }

    public double getMinGainValue() {
        return minGainValue;
    }

    public String getDescription() {
        return description;
    }

    static MeasurementGain createMeasurementGain(GainListStructure gainListStructure) {
        MeasurementGain measurementGain = new MeasurementGain(gainListStructure);
        if (measurementGain.getDescription() == null || measurementGain.getDescription().isEmpty()) {
            return null;
        }
        return measurementGain;
    }

    static Collection<MeasurementGain> createMeasurementGains(GainListStructure[] gainListStructures) {
        LinkedList<MeasurementGain> measurementGains = new LinkedList<>();
        for (GainListStructure gainListStructure : gainListStructures) {
            MeasurementGain measurementGain = createMeasurementGain(gainListStructure);
            if (measurementGain != null) {
                measurementGains.add(measurementGain);
            }
        }
        return Collections.unmodifiableList(measurementGains);
    }
}
