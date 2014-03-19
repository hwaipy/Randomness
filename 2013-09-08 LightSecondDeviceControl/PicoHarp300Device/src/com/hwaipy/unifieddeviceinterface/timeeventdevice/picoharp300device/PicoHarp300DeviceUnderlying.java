package com.hwaipy.unifieddeviceinterface.timeeventdevice.picoharp300device;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.Mode;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.PicoQuantException;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import java.nio.ByteBuffer;

/**
 *
 * @author Hwaipy
 */
class PicoHarp300DeviceUnderlying {
    //TODO Lib_Path need to be more clever

    private static final String LIBRARYPATH_STRING = "C:\\WINDOWS\\sysWOW64\\phlib.dll";
    public static final int TTREADMAX = 131072;
    public static final int FLAG_OVERFLOW = 0x0040;
    public static final int FLAG_FIFOFULL = 0x0003;
    private static PicoHarp300JNA jna;
    private final Memory fifoReaderMemory = new Memory(TTREADMAX * 4);

    PicoHarp300DeviceUnderlying() {
        jna = (PicoHarp300JNA) Native.loadLibrary(LIBRARYPATH_STRING, PicoHarp300JNA.class);
    }

    String getLibraryVersion() {
        Memory memory = new Memory(8);
        jna.PH_GetLibraryVersion(memory);
        return memory.getString(0);
    }

    String openDevice(int deviceIndex) throws PicoQuantException {
        Memory memory = new Memory(8);
        checkReturnValue(jna.PH_OpenDevice(deviceIndex, memory));
        return memory.getString(0);
    }

    void closeDevice(int deviceIndex) throws PicoQuantException {
        checkReturnValue(jna.PH_CloseDevice(deviceIndex));
    }

    void initialize(int deviceIndex, Mode mode, boolean externalClock) throws PicoQuantException {
        checkReturnValue(jna.PH_Initialize(deviceIndex, mode.getValue()));
    }

    String getHardwareInfo(int deviceIndex) throws PicoQuantException {
        Memory model = new Memory(16);
        Memory vers = new Memory(8);
        checkReturnValue(jna.PH_GetHardwareInfo(deviceIndex, model, vers));
        return model.getString(0) + ", " + vers.getString(0);
    }

    String getSerialNumber(int deviceIndex) throws PicoQuantException {
        Memory serial = new Memory(8);
        checkReturnValue(jna.PH_GetSerialNumber(deviceIndex, serial));
        return serial.getString(0);
    }

    void calibrate(int deviceIndex) throws PicoQuantException {
        checkReturnValue(jna.PH_Calibrate(deviceIndex));
    }

    void setInputCFDLevel(int deviceIndex, int channel, int value) throws PicoQuantException {
        checkReturnValue(jna.PH_SetInputCFDLevel(deviceIndex, channel, value));
    }

    void setInputCFDZeroCross(int deviceIndex, int channel, int value) throws PicoQuantException {
        checkReturnValue(jna.PH_SetInputCFDZeroCross(deviceIndex, channel, value));
    }

    int getCountRate(int deviceIndex, int channel) throws PicoQuantException {
        int countRate = jna.PH_GetCountRate(deviceIndex, channel);
        checkReturnValue(countRate);
        return countRate;
    }

    void startMeas(int deviceIndex, int acquisitionTime) throws PicoQuantException {
        checkReturnValue(jna.PH_StartMeas(deviceIndex, acquisitionTime));
    }

    void stopMeas(int deviceIndex) throws PicoQuantException {
        checkReturnValue(jna.PH_StopMeas(deviceIndex));
    }

    ByteBuffer readFIFO(int deviceIndex) throws PicoQuantException {
        int flag = jna.PH_GetFlags(deviceIndex);
        if (flag != 0) {
            System.out.println(flag);
//            throw new PicoQuantException("FLAG is " + flag);
        }
        int readedRecordsNumber = jna.PH_TTReadData(deviceIndex, fifoReaderMemory, TTREADMAX);
        checkReturnValue(readedRecordsNumber);
        if (readedRecordsNumber > 0) {
            ByteBuffer byteBuffer = fifoReaderMemory.getByteBuffer(0, readedRecordsNumber * 4);
            return byteBuffer;
        } else {
            int ctcStatus = jna.PH_CTCStatus(deviceIndex);
            checkReturnValue(ctcStatus);
            if (ctcStatus > 0) {
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
        int v = jna.PH_GetErrorString(memory, errorCode);
        return v >= 0 ? memory.getString(0) : "";
    }
}
