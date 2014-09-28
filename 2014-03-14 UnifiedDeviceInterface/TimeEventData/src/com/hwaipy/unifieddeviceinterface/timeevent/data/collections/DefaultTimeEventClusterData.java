package com.hwaipy.unifieddeviceinterface.timeevent.data.collections;

/**
 *
 * @author Hwaipy
 */
public class DefaultTimeEventClusterData implements TimeEventClusterData {

    protected final TimeEventListData[] timeEventLists;

    public DefaultTimeEventClusterData(TimeEventListData[] timeEventLists) {
        this.timeEventLists = timeEventLists;
    }

    @Override
    public int getEventCount() {
        int eventCount = 0;
        for (TimeEventListData timeEventList : timeEventLists) {
            eventCount += timeEventList.size();
        }
        return eventCount;
    }

    @Override
    public int getChannelCount() {
        return timeEventLists.length;
    }

    @Override
    public int getEventCount(int channel) {
        return timeEventLists[channel].size();
    }

    @Override
    public TimeEventListData getEventListData(int channel) {
        return timeEventLists[channel];
    }
}
