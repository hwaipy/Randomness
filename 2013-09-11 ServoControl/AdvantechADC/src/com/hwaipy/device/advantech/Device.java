package com.hwaipy.device.advantech;

import com.hwaipy.device.advantech.jna.AdvantechADCJNA;
import com.hwaipy.device.advantech.jna.DeviceFeatureStructure;
import com.hwaipy.device.advantech.jna.DeviceStructure;
import com.hwaipy.device.advantech.jna.ExceptionCallbacker;
import java.util.Arrays;

/**
 *
 * @author Hwaipy Lab
 */
public class Device {

    private final AdvantechADCJNA jna;
    private final int index;
    private final int deviceNumber;
    private final String deviceName;
    private final int numberOfSubdevices;
    private final DeviceFeature deviceFeature;
    private final int[] AISingleEndChannelGains;

    Device(AdvantechADCJNA jna, int index, DeviceStructure deviceStructure, DeviceFeatureStructure dfs) {
        this.jna = jna;
        this.index = index;
        deviceNumber = deviceStructure.dwDeviceNum;
        numberOfSubdevices = deviceStructure.nNumOfSubdevices;
        deviceName = Util.createStringFromBytesArray(deviceStructure.szDeviceName);
        deviceFeature = new DeviceFeature(dfs);
        int AISingleEndChannelCount = deviceFeature.getMaxAISingleEndChannel();
        AISingleEndChannelGains = new int[AISingleEndChannelCount];
        Arrays.fill(AISingleEndChannelGains, 16);
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getNumberOfSubdevices() {
        return numberOfSubdevices;
    }

    public DeviceFeature getDeviceFeature() {
        return deviceFeature;
    }

    public void open() throws AdvantechException {
        synchronized (jna) {
            ExceptionCallbacker callbacker = new ExceptionCallbacker();
            if (!jna.deviceOpen(index, callbacker)) {
                throw new AdvantechException(callbacker.getException());
            }
        }
    }

    public void close() {
        synchronized (jna) {
            jna.deviceClose(index);
        }
    }

    public void setAIConfiguration(int gainCode, int channel) throws AdvantechException {
//        synchronized (jna) {
//            ExceptionCallbacker callbacker = new ExceptionCallbacker();
//            if (!jna.setAIConfiguration(index, (short) gainCode, (short) channel, callbacker)) {
//                throw new AdvantechException(callbacker.getException());
//            }
//        }
        AISingleEndChannelGains[channel] = gainCode;
    }

    public double readAIVoltage(int channel) throws AdvantechException {
        synchronized (jna) {
            ExceptionCallbacker callbacker = new ExceptionCallbacker();
            int gainCode = AISingleEndChannelGains[channel];
            if (!jna.setAIConfiguration(index, (short) gainCode, (short) channel, callbacker)) {
                throw new AdvantechException(callbacker.getException());
            }
            callbacker = new ExceptionCallbacker();
            double voltage = jna.readAIVoltage(index, (short) channel, callbacker);
            if (callbacker.getException() != null && !callbacker.getException().isEmpty()) {
                throw new AdvantechException(callbacker.getException());
            }
            return voltage;
        }
    }

    public int getCurrentDOByte(int channel) throws AdvantechException {
        synchronized (jna) {
            ExceptionCallbacker callbacker = new ExceptionCallbacker();
            short b = jna.getCurrentDOByte(index, (short) channel, callbacker);
            if (b == -1 || (callbacker.getException() != null && !callbacker.getException().isEmpty())) {
                throw new AdvantechException(callbacker.getException());
            }
            return b;
        }
    }

    public void writeDOByte(int channel, int data) throws AdvantechException {
        synchronized (jna) {
            ExceptionCallbacker callbacker = new ExceptionCallbacker();
            if (!jna.writeDOByte(index, (short) channel, (byte) data, callbacker)) {
                throw new AdvantechException(callbacker.getException());
            }
        }
    }

    public void writeDOBit(int channel, int bit, boolean on) throws AdvantechException {
        synchronized (jna) {
            ExceptionCallbacker callbacker = new ExceptionCallbacker();
            if (!jna.writeDOBit(index, (short) channel, (short) bit, on, callbacker)) {
                throw new AdvantechException(callbacker.getException());
            }
        }
    }
}
