package com.hwaipy.unifieddeviceinterface.timeevent.data.process;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventListData;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class RecursionCoincidenceMatcher implements Iterable<Coincidence> {

    private final TimeEventListData list1;
    private final TimeEventListData list2;
    private long gate;
    private long initDelay;
    private final ArrayList<Coincidence> coincidences = new ArrayList<>();
    private boolean find = false;
    private int coincidenceCount = 0;

    public RecursionCoincidenceMatcher(TimeEventListData list1, TimeEventListData list2, long gate, long initDelay) {
        this.list1 = list1;
        this.list2 = list2;
        this.gate = gate;
        this.initDelay = initDelay;
    }

    public RecursionCoincidenceMatcher(TimeEventListData list1, TimeEventListData list2, long gate, int firstPair) {
        this.list1 = list1;
        this.list2 = list2;
        this.gate = gate;
        if (firstPair >= 0) {
            initDelay = list2.get(firstPair).getTime() - list1.get(0).getTime();
        } else {
            initDelay = list2.get(0).getTime() - list1.get(-firstPair).getTime();
        }
    }

    public long getGate() {
        return gate;
    }

    public void setGate(long gate) {
        this.gate = gate;
        find = false;
    }

    public long getInitDelay() {
        return initDelay;
    }

    public void setInitDelay(long initDelay) {
        this.initDelay = initDelay;
        find = false;
    }

    public int find() {
        long delay = initDelay;
        coincidences.clear();
        Iterator<TimeEventData> iterator1 = list1.iterator();
        Iterator<TimeEventData> iterator2 = list2.iterator();
        TimeEventData event1 = iterator1.hasNext() ? iterator1.next() : null;
        TimeEventData event2 = iterator2.hasNext() ? iterator2.next() : null;
        while (event1 != null && event2 != null) {
            long time1 = event1.getTime();
            long time2 = event2.getTime() - delay;
            if (time1 < time2 - gate) {
                event1 = iterator1.hasNext() ? iterator1.next() : null;
            } else if (time2 < time1 - gate) {
                event2 = iterator2.hasNext() ? iterator2.next() : null;
            } else {
                coincidences.add(new Coincidence(event1, event2, delay));
                delay = event2.getTime() - event1.getTime();
                event1 = iterator1.hasNext() ? iterator1.next() : null;
                event2 = iterator2.hasNext() ? iterator2.next() : null;
            }
        }
        coincidenceCount = coincidences.size();
        find = true;
        return coincidenceCount;
    }

    public int getCoincidenceCount() {
        if (!find) {
            throw new RuntimeException();
        }
        return coincidenceCount;
    }

    @Override
    public Iterator<Coincidence> iterator() {
        if (!find) {
            throw new RuntimeException();
        }
        return coincidences.iterator();
    }

    public int size() {
        if (!find) {
            throw new RuntimeException();
        }
        return coincidences.size();
    }

    public Coincidence get(int index) {
        if (!find) {
            throw new RuntimeException();
        }
        return coincidences.get(index);
    }
}
