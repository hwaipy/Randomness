package com.hwaipy.unifieddeviceinterface.timeevent.data.process;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.SetableTimeEventListData;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class TimeCalibrator {

    private final Iterable<Coincidence> mapping;
    private final SetableTimeEventListData list;

    public TimeCalibrator(Iterable<Coincidence> mapping, SetableTimeEventListData list) {
        this.mapping = mapping;
        this.list = list;
    }

    public void calibrate() {
//        System.out.println("Calibrate begin");
        Iterator<Coincidence> iterator = mapping.iterator();
        boolean isError = false;
        if (!iterator.hasNext()) {
//            System.out.println("Direct out");
            return;
        }
        Coincidence coincidenceStart = iterator.next();
        long timeStart1 = coincidenceStart.getEvent1().getTime();
        long timeStart2 = coincidenceStart.getEvent2().getTime();
//        System.out.println("timeStart1 -> " + timeStart1 / 1000000000000.);
//        System.out.println("timeStart2 -> " + timeStart2 / 1000000000000.);
        int index = 0;
        while (index < list.size() && list.get(index).getTime() < timeStart2) {
//            System.out.println();
            list.set(TimeEventData.ERROR_EVENT, index);
            index++;
        }
        while (iterator.hasNext() && index < list.size()) {
            Coincidence coincidenceEnd = iterator.next();
            long timeEnd1 = coincidenceEnd.getEvent1().getTime();
            long timeEnd2 = coincidenceEnd.getEvent2().getTime();
            isError = (timeEnd1 - timeStart1) < 10000000;
            while (index < list.size()) {
                TimeEventData event = list.get(index);
                long time = event.getTime();
                if (time < timeEnd2) {
                    time = (long) (((double) (time - timeStart2)) / (timeEnd2 - timeStart2) * (timeEnd1 - timeStart1) + timeStart1);
                    if (isError) {
                        list.set(TimeEventData.ERROR_EVENT, index);
                    } else {
                        list.set(new TimeEventData(time, event.getChannel()), index);
                    }
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
            list.set(TimeEventData.ERROR_EVENT, index);
            index++;
        }
    }

    public static void calibrate(Iterable<Coincidence> mapping, SetableTimeEventListData list) {
        new TimeCalibrator(mapping, list).calibrate();
    }
}
