package com.hwaipy.unifieddeviceinterface.virtualinstrument;

import com.hwaipy.unifieddeviceInterface.ComponentInformation;
import com.hwaipy.unifieddeviceInterface.Data;
import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.components.AbstractDataComponent;
import com.hwaipy.unifieddeviceInterface.components.CounterComponent;

/**
 *
 * @author Hwaipy
 */
public class VirtualCounterComponent extends AbstractDataComponent implements CounterComponent {

    private final VirtualCounterInstrument instrument;
    private long integrateTime = 1000;

    public VirtualCounterComponent(VirtualCounterInstrument instrument) {
        this.instrument = instrument;
        instrument.setIntegrateTime(integrateTime);
        instrument.addDataUpdateListener((event) -> {
            fireDataUpdateEvent(new DataUpdateEvent(VirtualCounterComponent.this, event.getData()));
        });
    }

    @Override
    public int getChannelCount() {
        return instrument.getChannelCount();
    }

    @Override
    public void setIntegrateTime(long time) {
        integrateTime = time;
        instrument.setIntegrateTime(integrateTime);
    }

    @Override
    public long getIntegrateTime() {
        return integrateTime;
    }

    @Override
    public void startComponent() {
        instrument.start();
    }

    @Override
    public void stopComponent() {
        instrument.stop();
    }

    @Override
    public ComponentInformation getInformation() {
        return new ComponentInformation();
    }

    @Override
    public void dataUpdate(Data data) {
    }
}
