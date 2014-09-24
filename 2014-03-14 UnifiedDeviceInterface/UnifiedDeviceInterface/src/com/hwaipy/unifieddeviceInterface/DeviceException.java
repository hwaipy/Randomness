package com.hwaipy.unifieddeviceInterface;

/**
 *
 * @author Hwaipy
 */
public class DeviceException extends Exception {

    public DeviceException() {
    }

    public DeviceException(String message) {
        super(message);
    }

    public DeviceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeviceException(Throwable cause) {
        super(cause);
    }

}
