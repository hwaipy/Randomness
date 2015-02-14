package com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant;

/**
 *
 * @author Hwaipy Lab
 */
public enum Mode {

    HISTOGRAMMING, T2, T3;

    public int getValue() {
        switch (this) {
            case HISTOGRAMMING:
                return 0;
            case T2:
                return 2;
            case T3:
                return 3;
            default:
                throw new RuntimeException();
        }
    }
}
