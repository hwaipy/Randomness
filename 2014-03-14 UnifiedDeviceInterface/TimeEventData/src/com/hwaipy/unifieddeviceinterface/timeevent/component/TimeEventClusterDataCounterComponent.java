package com.hwaipy.unifieddeviceinterface.timeevent.component;

import com.hwaipy.unifieddeviceInterface.ComponentInformation;
import com.hwaipy.unifieddeviceInterface.Data;
import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.components.AbstractDataComponent;
import com.hwaipy.unifieddeviceInterface.components.CounterComponent;
import com.hwaipy.unifieddeviceInterface.data.CounterData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventClusterData;

/**
 *
 * @author Hwaipy
 */
public class TimeEventClusterDataCounterComponent extends AbstractDataComponent implements CounterComponent {

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
        if (data instanceof TimeEventClusterData) {
            TimeEventClusterData timeEventClusterData = (TimeEventClusterData) data;
            int channelCount = timeEventClusterData.getChannelCount();
            long[] counts = new long[channelCount];
            for (int i = 0; i < channelCount; i++) {
                counts[i] = timeEventClusterData.getEventCount(i);
            }
            CounterData counterData = new CounterData(counts);
            fireDataUpdateEvent(new DataUpdateEvent(this, counterData));
        } else {
            throw new IllegalArgumentException("TimeEventClusterData only.");
        }
    }

    @Override
    public int getChannelCount() {
        //TODO Stupid
        return 8;
    }

    @Override
    public void setIntegrateTime(long time) {
    }

    @Override
    public long getIntegrateTime() {
        return Long.MAX_VALUE;
    }
}
