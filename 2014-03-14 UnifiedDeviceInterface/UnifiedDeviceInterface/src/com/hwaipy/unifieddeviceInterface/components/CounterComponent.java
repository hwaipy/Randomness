package com.hwaipy.unifieddeviceInterface.components;

import com.hwaipy.unifieddeviceInterface.Component;
import com.hwaipy.unifieddeviceInterface.DataComponent;

/**
 *
 * @author Hwaipy
 */
public interface CounterComponent extends Component, DataComponent {

    public int getChannelCount();

    public void setIntegrateTime(long time);

    public long getIntegrateTime();
}
