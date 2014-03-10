package com.hwaipy.device.advantech;

import com.hwaipy.device.advantech.jna.AdvantechADCJNA;
import com.hwaipy.device.advantech.jna.DeviceFeatureStructure;
import com.hwaipy.device.advantech.jna.DeviceStructure;
import com.hwaipy.device.advantech.jna.ExceptionCallbacker;
import com.sun.jna.Native;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Hwaipy Lab
 */
public class AdvantechADC {

    private static final String LIBRARYPATH_STRING = "D:\\Data\\CPP\\AdvantechADC\\Debug\\AdvantechADC.dll";
    private final AdvantechADCJNA jna;
    private ArrayList<Device> deviceList = new ArrayList<>();

    public AdvantechADC() {
        jna = (AdvantechADCJNA) Native.loadLibrary(LIBRARYPATH_STRING, AdvantechADCJNA.class);
    }

    public void loadDevice() throws AdvantechException {
        synchronized (jna) {
            ExceptionCallbacker callbacker = new ExceptionCallbacker();
            if (!jna.loadDevices(callbacker)) {
                throw new AdvantechException(callbacker.getException());
            }
            deviceList.clear();
            int numberOfDevices = jna.getNumberOfDevices();
            for (int i = 0; i < numberOfDevices; i++) {
                callbacker = new ExceptionCallbacker();
                if (!jna.deviceOpen(i, callbacker)) {
                    throw new AdvantechException(callbacker.getException());
                }
                DeviceStructure.ByValue deviceStructure = jna.getDevice(i);
                callbacker = new ExceptionCallbacker();
                DeviceFeatureStructure.ByValue deviceFeatureStructure = jna.readDeviceFeature(i, callbacker);
                if (callbacker.getException() != null && !callbacker.getException().isEmpty()) {
                    System.out.println(i);
                    throw new AdvantechException(callbacker.getException());
                }
                deviceList.add(new Device(jna, i, deviceStructure, deviceFeatureStructure));
                jna.deviceClose(i);
            }
        }
    }

    public int getDeviceCount() {
        return deviceList.size();
    }

    public Device getDevice(int index) {
        return deviceList.get(index);
    }

    public void show() {
        Device device = deviceList.get(0);
        DeviceFeature deviceFeature = device.getDeviceFeature();
        System.out.println(deviceFeature.getMaxDOChannel());
        Iterator<MeasurementGain> iterator = deviceFeature.getMeasurementGains().iterator();
        while (iterator.hasNext()) {
            MeasurementGain g = iterator.next();
            System.out.println("------------------------");
            System.out.println(g.getCode());
            System.out.println(g.getMaxGainValue());
            System.out.println(g.getMinGainValue());
            System.out.println(g.getDescription());
        }
    }
}
