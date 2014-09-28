package com.hwaipy.unifieddeviceinterface.timeevent.data;

import com.hwaipy.unifieddeviceInterface.Data;

/**
 *
 * @author Hwaipy
 */
public interface TimeEventListData extends Iterable<TimeEventData>, Data {

    public int size();

    public TimeEventData get(int index);
}
