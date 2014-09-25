package com.hwaipy.unifieddeviceInterface;

import com.hwaipy.utilities.system.WeakReferenceMapUtilities;
import java.util.Arrays;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Hwaipy
 */
public interface Component {

    public default void start() {
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            ComponentEvent componentStartingEvent = new ComponentEvent(this);
            Arrays.stream(eventListenerList.getListeners(ComponentListener.class))
                    .forEach(listener -> listener.componentStarting(componentStartingEvent));
        }
        startComponent();
        if (eventListenerList != null) {
            ComponentEvent componentStartedEvent = new ComponentEvent(this);
            Arrays.stream(eventListenerList.getListeners(ComponentListener.class))
                    .forEach(listener -> listener.componentStarted(componentStartedEvent));
        }
    }

    public void startComponent();

    public default void stop() {
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            ComponentEvent componentStopingEvent = new ComponentEvent(this);
            Arrays.stream(eventListenerList.getListeners(ComponentListener.class))
                    .forEach(listener -> listener.componentStoping(componentStopingEvent));
        }
        stopComponent();
        if (eventListenerList != null) {
            ComponentEvent componentStopedEvent = new ComponentEvent(this);
            Arrays.stream(eventListenerList.getListeners(ComponentListener.class))
                    .forEach(listener -> listener.componentStoped(componentStopedEvent));
        }
    }

    public void stopComponent();

    public ComponentInformation getInformation();

    public default void addComponentListener(ComponentListener listener) {
        synchronized (this) {
            EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
            if (eventListenerList == null) {
                eventListenerList = new EventListenerList();
                WeakReferenceMapUtilities.put(this, EventListenerList.class, eventListenerList);
            }
            eventListenerList.add(ComponentListener.class, listener);
        }
    }

    public default void removeComponentListener(ComponentListener listener) {
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            eventListenerList.remove(ComponentListener.class, listener);
        }
    }
}
