package com.hwaipy.rrdps;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class Tagger {

    private final TimeEventList syncList;
    private final TimeEventList apdList;
    private final long gate;

    public Tagger(TimeEventList syncList, TimeEventList apdList, long gate) {
        this.syncList = syncList;
        this.apdList = apdList;
        this.gate = gate;
    }

    public ArrayList<Entry> tag() {
        ArrayList<Entry> result = new ArrayList<>();
        Iterator<TimeEvent> syncIterator = syncList.iterator();
        Iterator<TimeEvent> apdIterator = apdList.iterator();
        TimeEvent syncEvent = syncIterator.next();
        long syncTime = syncEvent.getTime();
        int roundIndex = 0;
        TimeEvent event = apdIterator.next();
        while (true) {
            if ((syncEvent == null) && syncIterator.hasNext()) {
                syncEvent = syncIterator.next();
                syncTime = syncEvent.getTime();
                roundIndex++;
            }
            if ((event == null) && apdIterator.hasNext()) {
                event = apdIterator.next();
            }
            if (syncEvent == null || event == null) {
                break;
            }
            long time = event.getTime();
            if (time < syncTime - 300000) {
                event = null;
            } else if (time > syncTime + 300000) {
                syncEvent = null;
            } else {
                DecodingRandom random = ((ExtandedTimeEvent<DecodingRandom>) syncEvent).getProperty();
                int pulseIndex = doTag(event.getTime(), syncTime, random.getDelay1());
                if (pulseIndex >= 0) {
                    result.add(new Entry(roundIndex, pulseIndex, event.getChannel() - 2));
                }
                event = null;
            }
        }
        return result;
    }

    private int doTag(long apdTime, long syncTime, int delayPulse) {
        long time = apdTime - delayPulse * 2000;
        int deltaTime = (int) (time - syncTime);
        int pulseIndex = -1;
        int index = deltaTime / 2000;
        if ((deltaTime <= index * 2000 + gate / 2) && (deltaTime >= index * 2000 - gate / 2)) {
            pulseIndex = index;
        } else if ((deltaTime >= (index + 1) * 2000 - gate / 2) && (deltaTime <= (index + 1) * 2000 + gate / 2)) {
            pulseIndex = index + 1;
        }
        if (pulseIndex > 127) {
            pulseIndex = -1;
        }
        return pulseIndex;
    }

    public class Entry {

        private final int roundIndex;
        private final int pulseIndex;
        private final int code;

        private Entry(int roundIndex, int pulseIndex, int code) {
            this.roundIndex = roundIndex;
            this.pulseIndex = pulseIndex;
            this.code = code;
        }

        public int getRoundIndex() {
            return roundIndex;
        }

        public int getPulseIndex() {
            return pulseIndex;
        }

        public int getCode() {
            return code;
        }
    }
}
