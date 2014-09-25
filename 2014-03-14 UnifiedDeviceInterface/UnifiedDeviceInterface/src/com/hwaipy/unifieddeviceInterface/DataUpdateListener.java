package com.hwaipy.unifieddeviceInterface;

import java.util.EventListener;

/**
 *
 * @author Hwaipy
 */
public interface DataUpdateListener extends EventListener {

    public default void dataUpdated(DataUpdateEvent event) {
    }
}
