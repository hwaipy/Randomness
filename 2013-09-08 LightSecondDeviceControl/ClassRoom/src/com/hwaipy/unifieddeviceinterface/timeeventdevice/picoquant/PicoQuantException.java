package com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant;

import com.hwaipy.unifieddeviceinterface.DeviceException;

/**
 *
 * @author Hwaipy Lab
 */
public class PicoQuantException extends DeviceException {

    public PicoQuantException(String errorString) {
        super(errorString);
    }

    public PicoQuantException(int errorCode, String errorString) {
        super("Error " + errorCode + ": " + errorString);
    }
}
