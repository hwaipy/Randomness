package com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400device;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.Mode;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.PicoQuantException;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import java.nio.ByteBuffer;

/**
 *
 * @author Hwaipy
 */
class HydraHarp400DeviceUnderlying {
    //TODO Lib_Path need to be more clever

    private static final String LIBRARYPATH_STRING = "C:\\WINDOWS\\system32\\hhlib.dll";
    public static final int TTREADMAX = 131072;
    private static HydraHarp400JNA jna;
    private final Memory fifoReaderMemory = new Memory(TTREADMAX * 4);

    HydraHarp400DeviceUnderlying() {
        jna = (HydraHarp400JNA) Native.loadLibrary(LIBRARYPATH_STRING, HydraHarp400JNA.class);
    }

    String getLibraryVersion() {
        Memory memory = new Memory(8);
        jna.HH_GetLibraryVersion(memory);
        return memory.getString(0);
    }

    String openDevice(int deviceIndex) throws PicoQuantException {
        Memory memory = new Memory(8);
        checkReturnValue(jna.HH_OpenDevice(deviceIndex, memory));
        return memory.getString(0);
    }

    void closeDevice(int deviceIndex) throws PicoQuantException {
        checkReturnValue(jna.HH_CloseDevice(deviceIndex));
    }

    void initialize(int deviceIndex, Mode mode, boolean externalClock) throws PicoQuantException {
        checkReturnValue(jna.HH_Initialize(deviceIndex, mode.getValue(), externalClock ? 1 : 0));
    }

    String getHardwareInfo(int deviceIndex) throws PicoQuantException {
        Memory model = new Memory(16);
        Memory partno = new Memory(8);
        checkReturnValue(jna.HH_GetHardwareInfo(deviceIndex, model, partno));
        return model.getString(0) + ", " + partno.getString(0);
    }

    String getSerialNumber(int deviceIndex) throws PicoQuantException {
        Memory serial = new Memory(8);
        checkReturnValue(jna.HH_GetSerialNumber(deviceIndex, serial));
        return serial.getString(0);
    }

    int getNumOfInputChannels(int deviceIndex) throws PicoQuantException {
        Memory number = new Memory(4);
        checkReturnValue(jna.HH_GetNumOfInputChannels(deviceIndex, number));
        return number.getInt(0);
    }

    void calibrate(int deviceIndex) throws PicoQuantException {
        checkReturnValue(jna.HH_Calibrate(deviceIndex));
    }

    void setInputCFDLevel(int deviceIndex, int channel, int value) throws PicoQuantException {
        checkReturnValue(jna.HH_SetInputCFDLevel(deviceIndex, channel, value));
    }

    void setInputCFDZeroCross(int deviceIndex, int channel, int value) throws PicoQuantException {
        checkReturnValue(jna.HH_SetInputCFDZeroCross(deviceIndex, channel, value));
    }

    int getSyncRate(int deviceIndex) throws PicoQuantException {
        Memory memory = new Memory(4);
        checkReturnValue(jna.HH_GetSyncRate(deviceIndex, memory));
        return memory.getInt(0);
    }

    int getCountRate(int deviceIndex, int channel) throws PicoQuantException {
        Memory memory = new Memory(4);
        checkReturnValue(jna.HH_GetCountRate(deviceIndex, channel, memory));
        return memory.getInt(0);
    }

    void startMeas(int deviceIndex, int acquisitionTime) throws PicoQuantException {
        checkReturnValue(jna.HH_StartMeas(deviceIndex, acquisitionTime));
    }

    void stopMeas(int deviceIndex) throws PicoQuantException {
        checkReturnValue(jna.HH_StopMeas(deviceIndex));
    }

    ByteBuffer readFIFO(int deviceIndex) throws PicoQuantException {
        Memory memory = new Memory(4);
        checkReturnValue(jna.HH_GetFlags(deviceIndex, memory));
        int flag = memory.getInt(0);
        if (flag != 0) {
            throw new PicoQuantException("FLAG is " + flag);
        }
        checkReturnValue(jna.HH_ReadFiFo(deviceIndex, fifoReaderMemory, TTREADMAX, memory));
        int readedRecordsNumber = memory.getInt(0);
        if (readedRecordsNumber > 0) {
            ByteBuffer byteBuffer = fifoReaderMemory.getByteBuffer(0, readedRecordsNumber * 4);
            return byteBuffer;
        } else {
            checkReturnValue(jna.HH_CTCStatus(deviceIndex, memory));
            int ctcStatus = memory.getInt(0);
            if (ctcStatus == 1) {
                return null;
            }
        }
        return ByteBuffer.allocate(0);
    }

    private void checkReturnValue(int returnValue) throws PicoQuantException {
        if (returnValue >= 0) {
            return;
        }
        String errorString = getErrorString(returnValue);
        throw new PicoQuantException(returnValue, errorString);
    }

    private String getErrorString(int errorCode) {
        Memory memory = new Memory(40);
        int v = jna.HH_GetErrorString(memory, errorCode);
        return v >= 0 ? memory.getString(0) : "";
    }
}
