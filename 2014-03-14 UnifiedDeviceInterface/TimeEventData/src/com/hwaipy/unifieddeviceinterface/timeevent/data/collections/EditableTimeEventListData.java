package com.hwaipy.unifieddeviceinterface.timeevent.data.collections;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;

/**
 *
 * @author Hwaipy
 */
public interface EditableTimeEventListData extends SetableTimeEventListData {

    public TimeEventData remove(int index);

    public void add(TimeEventData event, int index);
}
