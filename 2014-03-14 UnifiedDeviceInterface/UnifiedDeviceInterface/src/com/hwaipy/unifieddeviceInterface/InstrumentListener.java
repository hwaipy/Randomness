package com.hwaipy.unifieddeviceInterface;

import java.util.EventListener;

/**
 *
 * @author Hwaipy
 */
public interface InstrumentListener extends EventListener {

    public void instrumentOpening(InstrumentEvent event);

    public void instrumentOpened(InstrumentEvent event);

    public void instrumentClosing(InstrumentEvent event);

    public void instrumentClosed(InstrumentEvent event);
}
