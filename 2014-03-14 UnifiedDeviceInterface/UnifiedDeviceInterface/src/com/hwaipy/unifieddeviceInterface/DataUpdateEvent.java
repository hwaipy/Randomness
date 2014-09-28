package com.hwaipy.unifieddeviceInterface;

import java.util.EventObject;

/**
 *
 * @author Hwaipy
 */
public class DataUpdateEvent extends EventObject {

    private final Data data;

    public DataUpdateEvent(Object source, Data data) {
        super(source);
        this.data = data;
    }

    public Data getData() {
        return data;
    }
}
