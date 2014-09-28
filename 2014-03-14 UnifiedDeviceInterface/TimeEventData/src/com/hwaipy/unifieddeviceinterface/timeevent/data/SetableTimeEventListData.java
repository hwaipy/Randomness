package com.hwaipy.unifieddeviceinterface.timeevent.data;

/**
 *
 * @author Hwaipy
 */
public interface SetableTimeEventListData extends TimeEventListData {

    public void set(TimeEventData event, int index);
}
