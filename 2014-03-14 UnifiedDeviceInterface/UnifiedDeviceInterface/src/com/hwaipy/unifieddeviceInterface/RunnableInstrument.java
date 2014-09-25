package com.hwaipy.unifieddeviceInterface;

import com.hwaipy.utilities.system.WeakReferenceMapUtilities;
import java.util.Arrays;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Hwaipy
 */
public interface RunnableInstrument {

    public default void start() {
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            RunnableInstrumentEvent instrumentStartingEvent = new RunnableInstrumentEvent(this);
            Arrays.stream(eventListenerList.getListeners(RunnableInstrumentListener.class))
                    .forEach(listener -> listener.instrumentStarting(instrumentStartingEvent));
        }
        startInstrument();
        if (eventListenerList != null) {
            RunnableInstrumentEvent instrumentStartedEvent = new RunnableInstrumentEvent(this);
            Arrays.stream(eventListenerList.getListeners(RunnableInstrumentListener.class))
                    .forEach(listener -> listener.instrumentStarted(instrumentStartedEvent));
        }
    }

    public void startInstrument();

    public default void stop() {
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            RunnableInstrumentEvent instrumentStopingEvent = new RunnableInstrumentEvent(this);
            Arrays.stream(eventListenerList.getListeners(RunnableInstrumentListener.class))
                    .forEach(listener -> listener.instrumentStoping(instrumentStopingEvent));
        }
        stopInstrument();
        if (eventListenerList != null) {
            RunnableInstrumentEvent instrumentStopedEvent = new RunnableInstrumentEvent(this);
            Arrays.stream(eventListenerList.getListeners(RunnableInstrumentListener.class))
                    .forEach(listener -> listener.instrumentStoped(instrumentStopedEvent));
        }
    }

    public void stopInstrument();

    public default void addRunnableInstrumentListener(RunnableInstrumentListener listener) {
        synchronized (this) {
            EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
            if (eventListenerList == null) {
                eventListenerList = new EventListenerList();
                WeakReferenceMapUtilities.put(this, EventListenerList.class, eventListenerList);
            }
            eventListenerList.add(RunnableInstrumentListener.class, listener);
        }
    }

    public default void removeRunnableInstrumentListener(RunnableInstrumentListener listener) {
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            eventListenerList.remove(RunnableInstrumentListener.class, listener);
        }
    }
}
