package com.hwaipy.unifieddeviceInterface;

import java.util.EventObject;

/**
 *
 * @author Hwaipy
 * @param <T>
 */
public class DataUpdateEvent<T> extends EventObject {

    private final T data;

    public DataUpdateEvent(Object source, T data) {
        super(source);
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
