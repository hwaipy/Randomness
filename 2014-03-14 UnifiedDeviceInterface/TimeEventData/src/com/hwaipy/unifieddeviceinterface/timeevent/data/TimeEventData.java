package com.hwaipy.unifieddeviceinterface.timeevent.data;

/**
 *
 * @author Hwaipy
 */
public class TimeEventData {

//    public static final TimeEventData ERROR_EVENT = new TimeEventData(-1, -1);
    private final long time;
    private final int channel;

    public TimeEventData(long time, int channel) {
        this.time = time;
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }

    public long getTime() {
        return time;
    }

    public double getTimeInSecond() {
        return time / 1000000000000.;
    }

    @Override
    public String toString() {
        return "TimeEventData(Channel: " + channel + ", Time: " + time + ")";
    }
}
