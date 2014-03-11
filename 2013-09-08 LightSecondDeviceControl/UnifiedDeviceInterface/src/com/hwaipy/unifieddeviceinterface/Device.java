package com.hwaipy.unifieddeviceinterface;

/**
 * An abstract representation of device. fewafwae
 *
 * @author HwaipyLab
 */
public interface Device {

    /**
     * Open this device.
     *
     * @throws DeviceException
     */
    public void open() throws DeviceException;

    /**
     * Close this device.
     *
     * @throws DeviceException
     */
    public void close() throws DeviceException;
}
