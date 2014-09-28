package com.hwaipy.unifieddeviceinterface.timeevent.data;

/**
 *
 * @author Hwaipy
 */
public interface EditableTimeEventListData extends SetableTimeEventListData {

    public TimeEventData remove(int index);

    public void add(TimeEventData event, int index);
}
