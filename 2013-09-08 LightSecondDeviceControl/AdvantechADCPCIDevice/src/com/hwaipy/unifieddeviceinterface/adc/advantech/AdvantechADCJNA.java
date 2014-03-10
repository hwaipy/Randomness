package com.hwaipy.unifieddeviceinterface.adc.advantech;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

/**
 *
 * @author ustc
 */
public interface AdvantechADCJNA extends Library {

    public NativeLong DRV_DeviceGetNumOfList(Pointer numOfDevices);

    public NativeLong DRV_DeviceGetList(Pointer devList, short maxEntries, Pointer outEntries);

    public NativeLong DRV_DeviceOpen(NativeLong deviceNum, Pointer driverHandle);

    public NativeLong DRV_DeviceClose(Pointer driverHandle);

    public NativeLong DRV_DioWriteBit(NativeLong driverHandle, Pointer lpDioWriteBit);

    public void DRV_GetErrorMessage(NativeLong errorCode, Pointer errorMsg);
}
