package com.hwaipy.unifieddeviceinterface.timeevent.data.io;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;

/**
 *
 * @author Hwaipy
 */
public class TimeEventSerializer {

    //TODO Channel可能超过8个，因此对Storage的处理需要改进
    public long serialize(TimeEventData timeEvent) {
        long s = timeEvent.getTime();
        long c = timeEvent.getChannel();
        if (s == Long.MAX_VALUE) {
            throw new RuntimeException();
//            return 24 | c;
        } else if (s == Long.MIN_VALUE) {
            throw new RuntimeException();
//            return 8 | c;
        } else {
            s <<= 4;
            return s | c;
        }
    }

    public TimeEventData deserialize(long data) {
        int channel = (int) data & 7;
        long time = data & 0xFFFFFFFFFFFFFFF8L;
        if (time == 24) {
            time = Long.MAX_VALUE;
        } else if (time == 8) {
            time = Long.MIN_VALUE;
        } else {
            time >>= 4;
        }
        return new TimeEventData(time, channel);
    }
}
