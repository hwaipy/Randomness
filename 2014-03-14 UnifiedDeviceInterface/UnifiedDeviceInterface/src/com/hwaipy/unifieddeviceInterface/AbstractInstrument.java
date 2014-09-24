package com.hwaipy.unifieddeviceInterface;

import java.util.Arrays;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Hwaipy
 */
public abstract class AbstractInstrument implements Instrument {

    private final EventListenerList eventListenerList = new EventListenerList();

    @Override
    public void open() {
        InstrumentEvent instrumentOpeningEvent = new InstrumentEvent(this);
        Arrays.stream(eventListenerList.getListeners(InstrumentListener.class))
                .forEach(listener -> listener.instrumentOpening(instrumentOpeningEvent));
        openInstrument();
        InstrumentEvent instrumentOpenedEvent = new InstrumentEvent(this);
        Arrays.stream(eventListenerList.getListeners(InstrumentListener.class))
                .forEach(listener -> listener.instrumentOpened(instrumentOpenedEvent));
    }

    protected abstract void openInstrument();

    @Override
    public void close() {
        InstrumentEvent instrumentClosingEvent = new InstrumentEvent(this);
        Arrays.stream(eventListenerList.getListeners(InstrumentListener.class))
                .forEach(listener -> listener.instrumentClosing(instrumentClosingEvent));
        openInstrument();
        InstrumentEvent instrumentClosedEvent = new InstrumentEvent(this);
        Arrays.stream(eventListenerList.getListeners(InstrumentListener.class))
                .forEach(listener -> listener.instrumentClosed(instrumentClosedEvent));
    }

    protected abstract void closeInstrument();

    @Override
    public void addInstrumentListener(InstrumentListener listener) {
        eventListenerList.add(InstrumentListener.class, listener);
    }

    @Override
    public void removeInstrumentListener(InstrumentListener listener) {
        eventListenerList.remove(InstrumentListener.class, listener);
    }
}
