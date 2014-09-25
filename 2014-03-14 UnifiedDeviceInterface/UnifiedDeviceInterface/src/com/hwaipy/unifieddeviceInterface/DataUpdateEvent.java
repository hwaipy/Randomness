package com.hwaipy.unifieddeviceInterface;

import java.util.EventObject;

/**
 *
 * @author Hwaipy
 */
public class DataUpdateEvent extends EventObject {

    private final Object data;

    public DataUpdateEvent(Object source, Object data) {
        super(source);
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
