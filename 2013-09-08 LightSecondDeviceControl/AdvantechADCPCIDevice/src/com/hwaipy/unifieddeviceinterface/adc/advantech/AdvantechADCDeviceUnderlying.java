package com.hwaipy.unifieddeviceinterface.adc.advantech;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ustc
 */
class AdvantechADCDeviceUnderlying {

    private static final String LIBRARYPATH_STRING = "C:\\Windows\\System32\\adsapi32.dll";
    private static AdvantechADCJNA jna;

    AdvantechADCDeviceUnderlying() {
        jna = (AdvantechADCJNA) Native.loadLibrary(LIBRARYPATH_STRING, AdvantechADCJNA.class);
    }

    int getNumberOfDeviceList() throws AdvantechADCException {
        Memory memory = new Memory(2);
        checkError(jna.DRV_DeviceGetNumOfList(memory));
        return memory.getShort(0);
    }

    List<AdvantechADCDevice> getDeviceList() throws AdvantechADCException {
        Memory devList = new Memory(DeviceInfomaton.SIZE * getNumberOfDeviceList());
        Memory outEntries = new Memory(2);
        checkError(jna.DRV_DeviceGetList(devList, (short) 1000, outEntries));
        short number = outEntries.getShort(0);
        ArrayList<AdvantechADCDevice> deviceList = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            DeviceInfomaton di = DeviceInfomaton.parse(devList, i);
            AdvantechADCDevice device = new AdvantechADCDevice(
                    di.getDeviceIndex(), di.getDeviceName(), di.getNumberOfSubDevices(), this);
            deviceList.add(device);
        }
        return deviceList;
    }

    long openDevice(long deviceIndex) throws AdvantechADCException {
        Memory memory = new Memory(NativeLong.SIZE);
        checkError(jna.DRV_DeviceOpen(new NativeLong(deviceIndex), memory));
        return memory.getNativeLong(0).longValue();
    }

    void close(long handle) throws AdvantechADCException {
        Memory memory = new Memory(NativeLong.SIZE);
        memory.setNativeLong(0, new NativeLong(handle));
        checkError(jna.DRV_DeviceClose(memory));
    }

    void dioWriteBit(long handle, int port, int bit, boolean level) throws AdvantechADCException {
        Memory memory = new Memory(6);
        memory.setShort(0, (short) port);
        memory.setShort(2, (short) bit);
        memory.setShort(4, (short) (level ? 1 : 0));
        checkError(jna.DRV_DioWriteBit(new NativeLong(handle), memory));
    }

    private void checkError(NativeLong errorCode) throws AdvantechADCException {
        checkError(errorCode.longValue());
    }

    private void checkError(long errorCode) throws AdvantechADCException {
        if (errorCode != 0) {
            String errorString = getErrorString(errorCode);
            throw new AdvantechADCException(errorString);
        }
    }

    private String getErrorString(long errorCode) {
        Memory memory = new Memory(80);
        jna.DRV_GetErrorMessage(new NativeLong(errorCode), memory);
        return memory.getString(0);
    }

    private static class DeviceInfomaton {

        static final int SIZE = NativeLong.SIZE + 50 + 2;
        private long deviceIndex;
        private String deviceName;
        private int numberOfSubDevices;

        public long getDeviceIndex() {
            return deviceIndex;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public int getNumberOfSubDevices() {
            return numberOfSubDevices;
        }

        public static DeviceInfomaton parse(Memory memory, int index) {
            DeviceInfomaton deviceInfomaton = new DeviceInfomaton();
            deviceInfomaton.deviceIndex = memory.getNativeLong(index * SIZE).longValue();
            deviceInfomaton.deviceName = memory.getString(index * SIZE + NativeLong.SIZE);
            deviceInfomaton.numberOfSubDevices = memory.getShort(index * SIZE + NativeLong.SIZE + 50);
            return deviceInfomaton;
        }
    }
}
