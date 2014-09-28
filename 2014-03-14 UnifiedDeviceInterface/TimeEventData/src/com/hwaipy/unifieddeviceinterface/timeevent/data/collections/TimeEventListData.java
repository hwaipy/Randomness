package com.hwaipy.unifieddeviceinterface.timeevent.data.collections;

import com.hwaipy.unifieddeviceInterface.Data;
import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;

/**
 *
 * @author Hwaipy
 */
public interface TimeEventListData extends Iterable<TimeEventData>, Data {

    public int size();

    public TimeEventData get(int index);
}
