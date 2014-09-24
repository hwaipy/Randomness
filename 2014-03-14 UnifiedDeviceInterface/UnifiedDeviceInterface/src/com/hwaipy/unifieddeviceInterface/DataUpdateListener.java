package com.hwaipy.unifieddeviceInterface;

import java.util.EventListener;

/**
 *
 * @author Hwaipy
 * @param <T>
 */
public interface DataUpdateListener<T> extends EventListener {

    public void dataUpdated(DataUpdateEvent<T> event);
}
