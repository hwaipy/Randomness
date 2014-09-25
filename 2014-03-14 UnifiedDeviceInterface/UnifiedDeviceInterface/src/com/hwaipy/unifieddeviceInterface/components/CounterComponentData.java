package com.hwaipy.unifieddeviceInterface.components;

/**
 *
 * @author Hwaipy
 */
public class CounterComponentData {

    private final long[] counts;

    public CounterComponentData(long[] counts) {
        this.counts = counts;
    }

    public int getChannelCount() {
        return counts.length;
    }

    public long getCount(int channel) {
        return counts[channel];
    }
}
