package com.hwaipy.unifieddeviceInterface.data;

import com.hwaipy.unifieddeviceInterface.Data;

/**
 *
 * @author Hwaipy
 */
public class CounterData implements Data {

    private final long[] counts;

    public CounterData(long[] counts) {
        this.counts = counts;
    }

    public int getChannelCount() {
        return counts.length;
    }

    public long getCount(int channel) {
        return counts[channel];
    }
}
