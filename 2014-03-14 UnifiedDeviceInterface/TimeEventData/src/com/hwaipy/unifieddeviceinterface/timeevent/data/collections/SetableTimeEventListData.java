package com.hwaipy.unifieddeviceinterface.timeevent.data.collections;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;

/**
 *
 * @author Hwaipy
 */
public interface SetableTimeEventListData extends TimeEventListData {

    public void set(TimeEventData event, int index);
}
