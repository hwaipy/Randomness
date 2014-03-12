package com.hwaipy.unifieddeviceinterface;

import com.hwaipy.unifieddeviceinterface.Device;
import com.hwaipy.unifieddeviceinterface.Device;
import com.hwaipy.unifieddeviceinterface.datadispatch.DataIncomeListener;

/**
 * Test
 * @author Hwaipy
 */
public interface DataDevice<DATA_TYPE> extends Device {

    public void addDataIncomeListener(DataIncomeListener<DATA_TYPE> listener);

    public void removeDataIncomeListener(DataIncomeListener<DATA_TYPE> listener);
}
