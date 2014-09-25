package com.hwaipy.unifieddeviceInterface;

import java.util.EventListener;

/**
 *
 * @author Hwaipy
 */
public interface InstrumentListener extends EventListener {

    public default void instrumentOpening(InstrumentEvent event) {
    }

    public default void instrumentOpened(InstrumentEvent event) {
    }

    public default void instrumentClosing(InstrumentEvent event) {
    }

    public default void instrumentClosed(InstrumentEvent event) {
    }
}
