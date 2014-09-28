package com.hwaipy.unifieddeviceInterface;

import com.hwaipy.utilities.system.WeakReferenceMapUtilities;
import java.util.Arrays;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Hwaipy
 */
public interface Instrument {

    public default void open() {
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            InstrumentEvent instrumentOpeningEvent = new InstrumentEvent(this);
            Arrays.stream(eventListenerList.getListeners(InstrumentListener.class))
                    .forEach(listener -> listener.instrumentOpening(instrumentOpeningEvent));
        }
        openInstrument();
        if (eventListenerList != null) {
            InstrumentEvent instrumentOpenedEvent = new InstrumentEvent(this);
            Arrays.stream(eventListenerList.getListeners(InstrumentListener.class))
                    .forEach(listener -> listener.instrumentOpened(instrumentOpenedEvent));
        }
    }

    public void openInstrument();

    public default void close() {
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            InstrumentEvent instrumentClosingEvent = new InstrumentEvent(this);
            Arrays.stream(eventListenerList.getListeners(InstrumentListener.class))
                    .forEach(listener -> listener.instrumentClosing(instrumentClosingEvent));
        }
        closeInstrument();
        if (eventListenerList != null) {
            InstrumentEvent instrumentClosedEvent = new InstrumentEvent(this);
            Arrays.stream(eventListenerList.getListeners(InstrumentListener.class))
                    .forEach(listener -> listener.instrumentClosed(instrumentClosedEvent));
        }
    }

    public void closeInstrument();

    public InstrumentInformation getInformation();

    public default void addInstrumentListener(InstrumentListener listener) {
        synchronized (this) {
            EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
            if (eventListenerList == null) {
                eventListenerList = new EventListenerList();
                WeakReferenceMapUtilities.put(this, EventListenerList.class, eventListenerList);
            }
            eventListenerList.add(InstrumentListener.class, listener);
        }
    }

    public default void removeInstrumentListener(InstrumentListener listener) {
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            eventListenerList.remove(InstrumentListener.class, listener);
        }
    }
}
