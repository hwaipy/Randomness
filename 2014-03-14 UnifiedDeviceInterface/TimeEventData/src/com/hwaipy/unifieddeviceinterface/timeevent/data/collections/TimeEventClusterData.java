package com.hwaipy.unifieddeviceinterface.timeevent.data.collections;

/**
 *
 * @author Hwaipy
 */
public interface TimeEventClusterData {

    public int getEventCount();

    public int getChannelCount();

    public int getEventCount(int channel);

    public TimeEventListData getEventListData(int channel);
}
