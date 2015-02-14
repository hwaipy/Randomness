package com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400device;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.Mode;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.PicoQuantException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.PicoQuantTimeEventDevice;
import java.nio.ByteBuffer;

/**
 *
 * @author Hwaipy
 */
public class HydraHarp400Device extends PicoQuantTimeEventDevice {

    private final HydraHarp400DeviceUnderlying device;

    public HydraHarp400Device(int deviceIndex) {
        super(deviceIndex, "HydraHarp 400");
        device = new HydraHarp400DeviceUnderlying();
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
    public void calibrate() throws PicoQuantException {
        if (isMeasurementing()) {
            throw new PicoQuantException("Calibrate is not allowed when measurementing");
        }
        device.calibrate(getDeviceIndex());
    }

    @Override
    public void setInputCFD(int channel, int zeroCross, int level) throws PicoQuantException {
        if (isMeasurementing()) {
            throw new PicoQuantException("Change input CFD is not allowed when measurementing");
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
        return device.getNumOfInputChannels(getDeviceIndex());
    }
}
