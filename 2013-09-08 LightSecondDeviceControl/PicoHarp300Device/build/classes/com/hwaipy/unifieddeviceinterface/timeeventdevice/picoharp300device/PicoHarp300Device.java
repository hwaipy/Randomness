package com.hwaipy.unifieddeviceinterface.timeeventdevice.picoharp300device;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.Mode;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.PicoQuantException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.PicoQuantTimeEventDevice;
import java.nio.ByteBuffer;

/**
 *
 * @author Hwaipy
 */
public class PicoHarp300Device extends PicoQuantTimeEventDevice {

    private final PicoHarp300DeviceUnderlying device;

    public PicoHarp300Device(int deviceIndex) {
        super(deviceIndex, "PicoHarp 300");
        device = new PicoHarp300DeviceUnderlying();
    }

    @Override
    public void open() throws PicoQuantException {
        if (isOpened()) {
            throw new IllegalStateException();
        }
        device.openDevice(getDeviceIndex());
        setOpened(true);
    }

    @Override
    public void close() throws PicoQuantException {
        if (!isOpened()) {
            throw new IllegalStateException();
        }
        device.closeDevice(getDeviceIndex());
        setOpened(false);
    }

    @Override
    public void initialize(Mode mode) throws PicoQuantException {
        device.initialize(getDeviceIndex(), mode, false);
    }

    @Override
    public String getLibraryVersion() {
        return device.getLibraryVersion();
    }

    @Override
    public String getSerialNumber() throws PicoQuantException {
        return device.getSerialNumber(getDeviceIndex());
    }

    @Override
    public String getHardwareInfo() throws PicoQuantException {
        return device.getHardwareInfo(getDeviceIndex());
    }

    @Override
    public int getNumOfInputChannels() throws PicoQuantException {
        return 2;
    }

    @Override
    public void setInputCFD(int channel, int zeroCross, int level) throws PicoQuantException {
        if (isMeasurementing()) {
            throw new IllegalStateException();
        }
        device.setInputCFDZeroCross(getDeviceIndex(), channel, zeroCross);
        device.setInputCFDLevel(getDeviceIndex(), channel, level);
    }

    @Override
    public void setInputCFD(int zeroCross, int level) throws PicoQuantException {
        int nic = getNumOfInputChannels();
        for (int i = 0; i < nic; i++) {
            setInputCFD(i, zeroCross, level);
        }
    }

    @Override
    protected void startMeasurement(int acquisitionTime) throws PicoQuantException {
        device.startMeas(getDeviceIndex(), acquisitionTime);
    }

    @Override
    protected void stopMeasurement() throws PicoQuantException {
        device.stopMeas(getDeviceIndex());
    }

    @Override
    protected ByteBuffer readFIFO() throws PicoQuantException {
        return device.readFIFO(getDeviceIndex());
    }

    @Override
    protected void calibrate() throws PicoQuantException {
        if (isMeasurementing()) {
            throw new RuntimeException();
        }
        device.calibrate(getDeviceIndex());
    }
}
