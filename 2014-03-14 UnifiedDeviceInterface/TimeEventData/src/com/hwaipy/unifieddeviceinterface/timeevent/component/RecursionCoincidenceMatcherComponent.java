package com.hwaipy.unifieddeviceinterface.timeevent.component;

import com.hwaipy.unifieddeviceInterface.Component;
import com.hwaipy.unifieddeviceInterface.ComponentInformation;
import com.hwaipy.unifieddeviceInterface.Data;
import com.hwaipy.unifieddeviceInterface.DataComponent;
import com.hwaipy.unifieddeviceInterface.components.AbstractDataComponent;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventClusterData;

/**
 *
 * @author Hwaipy
 */
public class RecursionCoincidenceMatcherComponent extends AbstractDataComponent implements Component, DataComponent {

    private final int channel1;
    private final int channel2;
    private TimeEventClusterData data1 = null;
    private TimeEventClusterData data2 = null;

    public RecursionCoincidenceMatcherComponent(int channel1, int channel2) {
        this.channel1 = channel1;
        this.channel2 = channel2;
    }

    @Override
    public void startComponent() {
    }

    @Override
    public void stopComponent() {
    }

    @Override
    public ComponentInformation getInformation() {
        return null;
    }

    @Override
    public void dataUpdate(Data data) {
        throw new RuntimeException();
    }

    public void dataUpdate(Data data, int index) {
        if (data instanceof TimeEventClusterData) {
            if (index == 0) {
                data1 = (TimeEventClusterData) data;
            } else if (index == 1) {
                data2 = (TimeEventClusterData) data;
            } else {
                throw new RuntimeException();
            }
        } else {
            throw new RuntimeException();
        }
        dataUpdate();
    }

    private void dataUpdate() {
        if (data1 != null && data2 != null) {
        }
    }
}
