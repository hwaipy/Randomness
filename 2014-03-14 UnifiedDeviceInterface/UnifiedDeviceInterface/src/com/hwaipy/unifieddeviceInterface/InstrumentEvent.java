package com.hwaipy.unifieddeviceInterface;

import java.util.EventObject;

/**
 *
 * @author Hwaipy
 */
public class InstrumentEvent extends EventObject {

    public InstrumentEvent(Instrument source) {
        super(source);
    }
}
