package com.hwaipy.unifieddeviceInterface;

/**
 *
 * @author Hwaipy
 */
public interface Instrument {

    public void open();

    public void close();

    public InstrumentInformation getInformation();

    public void addInstrumentListener(InstrumentListener listener);

    public void removeInstrumentListener(InstrumentListener listener);
}
