package com.hwaipy.unifieddeviceInterface.components;

import com.hwaipy.unifieddeviceInterface.Component;
import com.hwaipy.unifieddeviceInterface.DataComponent;
import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.DataUpdateListener;
import com.hwaipy.utilities.system.WeakReferenceMapUtilities;
import java.util.Arrays;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Hwaipy
 */
public abstract class AbstractDataComponent implements Component, DataComponent {

    protected void fireDataUpdateEvent(DataUpdateEvent event) {
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            Arrays.stream(eventListenerList.getListeners(DataUpdateListener.class))
                    .forEach(listener -> listener.dataUpdated(event));
        }
    }
}
