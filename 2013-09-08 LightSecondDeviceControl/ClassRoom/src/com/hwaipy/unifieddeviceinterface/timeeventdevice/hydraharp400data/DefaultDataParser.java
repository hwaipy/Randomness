package com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;

/**
 *
 * @author Hwaipy Lab
 */
public class DefaultDataParser extends DataParser {

    @Override
    protected void parse(TimeEventSegment timeEventSegment) {
        int channelCount = timeEventSegment.getChannelCount();
        System.out.print(timeEventSegment.getEventCount() + "\t");
        for (int i = 0; i < channelCount; i++) {
            System.out.print(timeEventSegment.getEventCount(i) + "\t");
        }
        System.out.println();
    }
}
