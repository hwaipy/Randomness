package com.hwaipy.unifieddeviceinterface.timeevent.data.collections;

import com.hwaipy.unifieddeviceInterface.Data;

/**
 *
 * @author Hwaipy
 */
public interface TimeEventClusterData extends Data {

    public int getEventCount();

    public int getChannelCount();

    public int getEventCount(int channel);

    public TimeEventListData getEventListData(int channel);
}
