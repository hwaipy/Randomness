package com.hwaipy.unifieddeviceInterface;

import java.util.EventListener;

/**
 *
 * @author Hwaipy
 */
public interface RunnableInstrumentListener extends EventListener {

    public default void instrumentStarting(RunnableInstrumentEvent event) {
    }

    public default void instrumentStarted(RunnableInstrumentEvent event) {
    }

    public default void instrumentStoping(RunnableInstrumentEvent event) {
    }

    public default void instrumentStoped(RunnableInstrumentEvent event) {
    }
}
