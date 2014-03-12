package com.hwaipy.unifieddeviceinterface;

/**
 * Component offers certain features of devices.
 * 
 * @see com.hwaipy.unifieddeviceinterface.DataComponent
 * @author Hwaipy
 */
public interface Component {

    /**
     * Initialize the component.
     * @throws DeviceException
     */
    public void initialization() throws DeviceException;

    /**
     * Uninitialize the component.
     * @throws DeviceException
     */
    public void uninitialization() throws DeviceException;
}
