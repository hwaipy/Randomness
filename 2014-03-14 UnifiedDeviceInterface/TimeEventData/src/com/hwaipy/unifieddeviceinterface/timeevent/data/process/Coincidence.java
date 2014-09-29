package com.hwaipy.unifieddeviceinterface.timeevent.data.process;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;

/**
 *
 * @author Hwaipy
 */
public class Coincidence {

    private final TimeEventData event1;
    private final TimeEventData event2;
    private final long delay;

    public Coincidence(TimeEventData event1, TimeEventData event2, long delay) {
        this.event1 = event1;
        this.event2 = event2;
        this.delay = delay;
    }

    public long getTimeDifferent() {
        return event1.getTime() - event2.getTime() + delay;
    }

    public long getAbsTimeDefferent() {
        long td = getTimeDifferent();
        return td > 0 ? td : -td;
    }

    public TimeEventData getEvent1() {
        return event1;
    }

    public TimeEventData getEvent2() {
        return event2;
    }

    public long getDelay() {
        return delay;
    }
}
