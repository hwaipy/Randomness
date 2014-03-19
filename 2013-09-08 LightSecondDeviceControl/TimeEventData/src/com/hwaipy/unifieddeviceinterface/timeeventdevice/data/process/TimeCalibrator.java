package com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class TimeCalibrator {

    private final Iterable<Coincidence> mapping;
    private final TimeEventList list;

    public TimeCalibrator(Iterable<Coincidence> mapping, TimeEventList list) {
        this.mapping = mapping;
        this.list = list;
    }

    public void calibrate() {
        Iterator<Coincidence> iterator = mapping.iterator();
        Coincidence coincidenceStart = iterator.next();
        long timeStart1 = coincidenceStart.getEvent1().getTime();
        long timeStart2 = coincidenceStart.getEvent2().getTime();
        int index = 0;
        while (list.get(index).getTime() < timeStart2 && index < list.size()) {
            list.set(TimeEvent.ERROR_EVENT, index);
            index++;
        }
        while (iterator.hasNext() && index < list.size()) {
            Coincidence coincidenceEnd = iterator.next();
            long timeEnd1 = coincidenceEnd.getEvent1().getTime();
            long timeEnd2 = coincidenceEnd.getEvent2().getTime();
            while (index < list.size()) {
                TimeEvent event = list.get(index);
                long time = event.getTime();
                if (time < timeEnd2) {
                    time = (long) (((double) (time - timeStart2)) / (timeEnd2 - timeStart2) * (timeEnd1 - timeStart1) + timeStart1);
                    list.set(new TimeEvent(time, event.getChannel()), index);
                    index++;
                    if (index >= list.size()) {
                        break;
                    }
                } else {
                    break;
                }
            }
            timeStart1 = timeEnd1;
            timeStart2 = timeEnd2;
        }
        while (index < list.size()) {
            list.set(TimeEvent.ERROR_EVENT, index);
            index++;
        }
    }

    public void calibrateOverall() {
        Iterator<Coincidence> iterator = mapping.iterator();
        Coincidence startCoincidence = iterator.next();
        Coincidence endCoincidence = startCoincidence;
        while (iterator.hasNext()) {
            endCoincidence = iterator.next();
        }
        long timeStart1 = startCoincidence.getEvent1().getTime();
        long timeStart2 = startCoincidence.getEvent2().getTime();
        long timeEnd1 = endCoincidence.getEvent1().getTime();
        long timeEnd2 = endCoincidence.getEvent2().getTime();
        System.out.println(timeStart1);
        System.out.println(timeStart2);
        System.out.println(timeEnd1);
        System.out.println(timeEnd2);
        int index = 0;
        while (index < list.size() && list.get(index).getTime() < timeStart2) {
            list.set(TimeEvent.ERROR_EVENT, index);
            index++;
        }
        while (index < list.size()) {
            TimeEvent event = list.get(index);
            long time = event.getTime();
            if (time < timeEnd2) {
                time = (long) (((double) (time - timeStart2)) / (timeEnd2 - timeStart2) * (timeEnd1 - timeStart1) + timeStart1);
                list.set(new TimeEvent(time, event.getChannel()), index);
                index++;
                if (index >= list.size()) {
                    break;
                }
            } else {
                break;
            }
        }
        while (index < list.size()) {
            list.set(TimeEvent.ERROR_EVENT, index);
            index++;
        }
    }

    private class BufferedIterator {

        private final Iterator<TimeEvent> iterator;
        private TimeEvent event;

        public BufferedIterator(Iterator<TimeEvent> iterator) {
            this.iterator = iterator;
            if (iterator.hasNext()) {
                event = iterator.next();
            } else {
                event = null;
            }
        }

        public boolean hasNext() {
            return event != null;
        }

        public TimeEvent get() {
            return event;
        }

        public TimeEvent next() {
            if (iterator.hasNext()) {
                event = iterator.next();
            } else {
                event = null;
            }
            return event;
        }
    }

    public static void calibrate(Iterable<Coincidence> mapping, TimeEventList list) {
        new TimeCalibrator(mapping, list).calibrate();
    }

    public static void calibrateOverall(Iterable<Coincidence> mapping, TimeEventList list) {
        new TimeCalibrator(mapping, list).calibrateOverall();
    }
}
