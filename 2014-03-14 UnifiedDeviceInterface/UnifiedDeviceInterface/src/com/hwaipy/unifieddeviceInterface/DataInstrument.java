package com.hwaipy.unifieddeviceInterface;

import com.hwaipy.utilities.system.WeakReferenceMapUtilities;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Hwaipy
 */
public interface DataInstrument extends Instrument {

    public default void addDataUpdateListener(DataUpdateListener listener) {
        synchronized (this) {
            EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
            if (eventListenerList == null) {
                eventListenerList = new EventListenerList();
                WeakReferenceMapUtilities.put(this, EventListenerList.class, eventListenerList);
            }
            eventListenerList.add(DataUpdateListener.class, listener);
        }
    }

    public default void removeDataUpdateListener(DataUpdateListener listener) {
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            eventListenerList.remove(DataUpdateListener.class, listener);
        }
    }
}
