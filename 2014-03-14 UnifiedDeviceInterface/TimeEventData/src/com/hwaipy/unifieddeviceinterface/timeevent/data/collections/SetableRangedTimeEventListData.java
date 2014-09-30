package com.hwaipy.unifieddeviceinterface.timeevent.data.collections;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class SetableRangedTimeEventListData implements SetableTimeEventListData {

    private final SetableTimeEventListData originalList;
    private final int offset;
    private final int size;

    public SetableRangedTimeEventListData(SetableTimeEventListData originalList, long start, long end) {
        this.originalList = originalList;
        int os = 0;
        int sz = 0;
        Iterator<TimeEventData> iterator = originalList.iterator();
        while (iterator.hasNext()) {
            long time = iterator.next().getTime();
            if (time < start) {
                os++;
            } else if (time < end) {
                sz++;
            }
        }
        offset = os;
        size = sz;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public TimeEventData get(int index) {
        return originalList.get(index + offset);
    }

    @Override
    public void set(TimeEventData event, int index) {
        originalList.set(event, index + offset);
    }

    @Override
    public Iterator<TimeEventData> iterator() {
        return new Iterator<TimeEventData>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public TimeEventData next() {
                TimeEventData e = get(index);
                index++;
                return e;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
}
