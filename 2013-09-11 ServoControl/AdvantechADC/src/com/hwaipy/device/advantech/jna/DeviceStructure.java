package com.hwaipy.device.advantech.jna;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Hwaipy Lab
 */
public class DeviceStructure extends Structure {

    public int dwDeviceNum;
    public byte[] szDeviceName = new byte[50];
    public short nNumOfSubdevices;

    @Override
    protected List getFieldOrder() {
        String[] fieldOrder = new String[]{"dwDeviceNum", "szDeviceName", "nNumOfSubdevices"};
        return Arrays.asList(fieldOrder);
    }

    public static class ByReference extends DeviceStructure implements Structure.ByReference {
    }

    public static class ByValue extends DeviceStructure implements Structure.ByValue {
    }
}
