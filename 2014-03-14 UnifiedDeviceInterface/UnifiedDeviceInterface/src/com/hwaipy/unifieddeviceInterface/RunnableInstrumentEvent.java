package com.hwaipy.unifieddeviceInterface;

import java.util.EventObject;

/**
 *
 * @author Hwaipy
 */
public class RunnableInstrumentEvent extends EventObject {

    public RunnableInstrumentEvent(RunnableInstrument source) {
        super(source);
    }

}
