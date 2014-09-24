package com.hwaipy.unifieddeviceInterface;

import java.util.HashMap;
import java.util.Map;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Hwaipy
 */
public interface DataInstrument extends Instrument {

    private static Map<DataInstrument, EventListenerList> eventListenerListMap = new HashMap<DataInstrument, EventListenerList>();
}
