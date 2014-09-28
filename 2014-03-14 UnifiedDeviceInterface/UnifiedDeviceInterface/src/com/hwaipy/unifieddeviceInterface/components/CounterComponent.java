package com.hwaipy.unifieddeviceInterface.components;

import com.hwaipy.unifieddeviceInterface.Component;
import com.hwaipy.unifieddeviceInterface.DataComponent;
import com.hwaipy.unifieddeviceInterface.DataType;
import com.hwaipy.unifieddeviceInterface.data.CounterData;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Hwaipy
 */
public interface CounterComponent extends Component, DataComponent {

    public int getChannelCount();

    public void setIntegrateTime(long time);

    public long getIntegrateTime();

    @Override
    public default Collection<DataType> export() {
        return Arrays.asList(new DataType("CounterRate", CounterData.class));
    }
}
