package com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Hwaipy Lab
 */
public class StreamTimeEventList implements TimeEventList {

    private final LinkedList<TimeEvent> list = new LinkedList<>();
    private TimeEvent lastEvent = null;

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public TimeEvent get(int index) {
        return list.get(index);
    }

    @Override
    public void set(TimeEvent event, int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<TimeEvent> iterator() {
        return list.listIterator();
    }

    public void offer(TimeEvent event) {
        if (lastEvent != null) {
            if (lastEvent.getChannel() != event.getChannel()) {
                throw new IllegalArgumentException("Channel not match");
            }
            if (lastEvent.getTime() > event.getTime()) {
                //TODO Add a Warning here
                System.out.println("A Time Reverse.");
                return;
            }
        }
        list.add(event);
        lastEvent = event;
    }

    public void clear() {
        list.clear();
    }

    public void clearBefore(long time) {
        while (list.size() > 0) {
            TimeEvent first = list.peek();
            long eventTime = first.getTime();
            if (eventTime <= time) {
                list.poll();
                continue;
            } else {
                break;
            }
        }
    }
}
