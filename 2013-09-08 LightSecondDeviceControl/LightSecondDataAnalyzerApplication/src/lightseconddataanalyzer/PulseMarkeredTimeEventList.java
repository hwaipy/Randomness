package lightseconddataanalyzer;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class PulseMarkeredTimeEventList implements TimeEventList {

    private final TimeEventList list;
    private final ArrayList<TimeSlice> timeSlices = new ArrayList<>();
    private final boolean level;

    public PulseMarkeredTimeEventList(TimeEventList list, TimeEventList marker, boolean level) {
        this.list = list;
        this.level = level;
        parseMarkerList(marker);
    }

    @Override
    public int size() {
        Iterator<TimeEvent> iterator = iterator();
        int count = 0;
        while (iterator.hasNext()) {
            TimeEvent timeEvent = iterator.next();
            count++;
        }
        return count;
    }

    @Override
    public TimeEvent get(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void set(TimeEvent event, int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<TimeEvent> iterator() {
        return new TimeEventIterator();
    }

    private class TimeEventIterator implements Iterator<TimeEvent> {

        private TimeEvent next = null;
        private int timeSliceIndex = 0;
        private int signalIndex = 0;

        public TimeEventIterator() {
            loadNext();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public TimeEvent next() {
            TimeEvent event = next;
            loadNext();
            return event;
        }
        int c = 0;

        private void loadNext() {
            while (true) {
                if (timeSliceIndex >= timeSlices.size() || signalIndex >= list.size()) {
                    next = null;
                    return;
                }
                TimeSlice slice = timeSlices.get(timeSliceIndex);
                long startTime = slice.getStart();
                long endTime = slice.getEnd();
                long time = list.get(signalIndex).getTime();
                if (time < startTime) {
                    signalIndex++;
                } else if (time > endTime) {
                    timeSliceIndex++;
//                    System.out.println(c + "\t@ slice from " + timeSlices.get(timeSliceIndex - 1).getStart() / 1000000000. + " to " + timeSlices.get(timeSliceIndex - 1).getEnd() / 1000000000.);
                    c = 0;
                } else {
                    signalIndex++;
                    c++;
                    next = list.get(signalIndex);
                    return;
                }
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    private int markerListIndex = 0;

    private void parseMarkerList(TimeEventList markerList) {
        TimeEvent marker = getNextMarker(markerList);
        long startTime = marker.getTime();
        boolean currentLevel = marker.getChannel() == markerChannel;
        while (true) {
            TimeEvent nextMarker = getNextMarker(markerList);
            if (nextMarker == null) {
                break;
            }
            long nextTime = nextMarker.getTime();
            boolean nextLevel = nextMarker.getChannel() == markerChannel;
            TimeSlice timeSlice = new TimeSlice(startTime + 1 * 1000000000l, nextTime, currentLevel);
//            System.out.println(currentLevel + "\t" + timeSlice.start / 1000000000. + "\t" + timeSlice.end / 1000000000.);
            if (currentLevel) {
                timeSlices.add(timeSlice);
            }
            startTime = nextTime;
            currentLevel = nextLevel;
        }
    }

    private TimeEvent getNextMarker(TimeEventList markerList) {
        if (markerListIndex >= markerList.size()) {
            return null;
        }
        TimeEvent next = markerList.get(markerListIndex);
        long time = next.getTime();
        markerListIndex++;
        long stop = time + 1000000000;
        while (markerListIndex < markerList.size()) {
            TimeEvent nextEvent = markerList.get(markerListIndex);
            long nextTime = nextEvent.getTime();
            if (nextTime < stop) {
                markerListIndex++;
            } else {
                break;
            }
        }
        return next;
    }

    private class TimeSlice {

        private final long start;
        private final long end;
        private final boolean level;

        public TimeSlice(long start, long end, boolean level) {
            this.start = start;
            this.end = end;
            this.level = level;
        }

        public long getStart() {
            return start;
        }

        public long getEnd() {
            return end;
        }

        public boolean isLevel() {
            return level;
        }
    }
}
