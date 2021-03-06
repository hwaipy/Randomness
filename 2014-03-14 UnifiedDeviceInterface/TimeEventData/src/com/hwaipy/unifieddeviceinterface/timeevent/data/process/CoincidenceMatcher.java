package com.hwaipy.unifieddeviceinterface.timeevent.data.process;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventListData;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Hwaipy
 */
public class CoincidenceMatcher {

    private final TimeEventListData list1;
    private final TimeEventListData list2;
    private long gate;
    private long delay;
    private final LinkedList<Coincidence> coincidences = new LinkedList<>();
    private boolean find = false;
    private int coincicenceCount = 0;

    public CoincidenceMatcher(TimeEventListData list1, TimeEventListData list2, long gate, long delay) {
        this.list1 = list1;
        this.list2 = list2;
        this.gate = gate;
        this.delay = delay;
    }

    public long getGate() {
        return gate;
    }

    public void setGate(long gate) {
        this.gate = gate;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public int find() {
        coincidences.clear();
        Iterator<TimeEventData> iterator1 = list1.iterator();
        Iterator<TimeEventData> iterator2 = list2.iterator();
        TimeEventData event1 = iterator1.hasNext() ? iterator1.next() : null;
        TimeEventData event2 = iterator2.hasNext() ? iterator2.next() : null;
        int coincident = 0;
        while (event1 != null && event2 != null) {
            long time1 = event1.getTime();
            long time2 = event2.getTime() - delay;
            if (time1 < time2 - gate) {
                event1 = iterator1.hasNext() ? iterator1.next() : null;
            } else if (time2 < time1 - gate) {
                event2 = iterator2.hasNext() ? iterator2.next() : null;
            } else {
                coincident++;
                coincidences.add(new Coincidence(event1, event2, delay));
                event1 = iterator1.hasNext() ? iterator1.next() : null;
                event2 = iterator2.hasNext() ? iterator2.next() : null;
            }
        }
        coincicenceCount = coincident;
        find = true;
        return coincident;
    }

    public int count() {
        Iterator<TimeEventData> iterator1 = list1.iterator();
        Iterator<TimeEventData> iterator2 = list2.iterator();
        TimeEventData event1 = iterator1.hasNext() ? iterator1.next() : null;
        TimeEventData event2 = iterator2.hasNext() ? iterator2.next() : null;
        int coincident = 0;
        while (event1 != null && event2 != null) {
            long time1 = event1.getTime();
            long time2 = event2.getTime() - delay;
            if (time1 < time2 - gate) {
                event1 = iterator1.hasNext() ? iterator1.next() : null;
            } else if (time2 < time1 - gate) {
                event2 = iterator2.hasNext() ? iterator2.next() : null;
            } else {
                coincident++;
                event1 = iterator1.hasNext() ? iterator1.next() : null;
                event2 = iterator2.hasNext() ? iterator2.next() : null;
            }
        }
        return coincident;
    }

    public int getCoincicenceCount() {
        if (!find) {
            throw new RuntimeException();
        }
        return coincicenceCount;
    }

    public Iterator<Coincidence> iterator() {
        if (!find) {
            throw new RuntimeException();
        }
        return coincidences.iterator();
    }
}
