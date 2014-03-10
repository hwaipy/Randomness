package com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400device.old;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400device.HydraHarp400Device;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.Mode;

/**
 *
 * @author Hwaipy Lab
 */
public class Test {

//    private static HydraHarp400Device hydraHarp400;
//
//    public static void main2(String[] args) throws HydraHarpException, InterruptedException {
//        hydraHarp400 = new HydraHarp400Device(0, new DefaultDataParser());
//        System.out.println("Library version is " + hydraHarp400.getLibraryVersion());
//        hydraHarp400.open(Mode.T2);
//        System.out.println("Serials Number: " + hydraHarp400.getSerialNumber());
//        System.out.println("Model: " + hydraHarp400.getHardwareInfo());
//        int inputChannelNumber = hydraHarp400.getNumOfInputChannels();
//        System.out.println("Device has " + inputChannelNumber + " input channels.");
//        hydraHarp400.calibrate();
//        System.out.println("Calibrated.");
//        hydraHarp400.setInputCFD(10, 300);
//        hydraHarp400.prepare();
//        System.out.println("Sync rate is " + hydraHarp400.getSyncRate());
//        for (int i = 0; i < inputChannelNumber; i++) {
//            System.out.println("Count rate in channel " + i + " is " + hydraHarp400.getCountRate(i));
//        }
//        hydraHarp400.start(10000);
//        //        while (true) {
//        //            boolean cont = hydraHarp400.readFIFO(0);
//        //            if (!cont) {
//        //                break;
//        //            }
//        //        }
//        Thread.sleep(15000);
//        hydraHarp400.stopAndWait();
//    }
//
//    public static void mainc(String[] args) throws HydraHarpException, InterruptedException {
//        HydraHarp400Device hydraHarp400 = new HydraHarp400Device();
//        System.out.println("Library version is " + hydraHarp400.getLibraryVersion());
//        System.out.println("Serials Number: " + hydraHarp400.openDevice(0));
//        hydraHarp400.initialize(0, Mode.T2, false);
//        System.out.println("Model: " + hydraHarp400.getHardwareInfo(0));
//        int inputChannelNumber = hydraHarp400.getNumOfInputChannels(0);
//        System.out.println("Device has " + inputChannelNumber + " input channels.");
//        hydraHarp400.calibrate(0);
//        System.out.println("Calibrated.");
//        for (int i = 0; i < inputChannelNumber; i++) {
//            hydraHarp400.setInputCFDZeroCross(0, i, 10);
//            hydraHarp400.setInputCFDLevel(0, i, 300);
//        }
//        Thread.sleep(400);
//        System.out.println("Sync rate is " + hydraHarp400.getSyncRate(0));
//        for (int i = 0; i < inputChannelNumber; i++) {
//            System.out.println("Count rate in channel " + i + " is " + hydraHarp400.getCountRate(0, i));
//        }
//        //System.out.println(hydraHarp400.getWarnings(0));
//        hydraHarp400.startMeas(0, 10000);
//        while (true) {
//            boolean cont = hydraHarp400.readFIFO(0);
//            if (!cont) {
//                break;
//            }
//        }
//    }
}
