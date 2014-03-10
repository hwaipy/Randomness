package com.hwaipy.device.advantech.jna;

import com.sun.jna.Library;

/**
 *
 * @author Hwaipy Lab
 */
public interface AdvantechADCJNA extends Library {

    public boolean test();

    public boolean loadDevices(ExceptionCallback callback);

    public short getNumberOfDevices();

    public DeviceStructure.ByValue getDevice(int index);

    public boolean deviceOpen(int index, ExceptionCallback callback);

    public boolean deviceClose(int index);

    public DeviceFeatureStructure.ByValue readDeviceFeature(int index, ExceptionCallback callback);

    public boolean setAIConfiguration(int index, short gain, short channel, ExceptionCallbacker callbacker);

    public float readAIVoltage(int index, short channel, ExceptionCallbacker callbacker);

    boolean writeDOByte(int index, short channel, byte data, ExceptionCallbacker callbacker);

    boolean writeDOBit(int index, short channel, short bit, boolean on, ExceptionCallbacker callbacker);

    short getCurrentDOByte(int index, short channel, ExceptionCallbacker callbacker);
}