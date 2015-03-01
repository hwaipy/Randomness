package com.hwaipy.rrdps.RealtimeData;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public class TEST {

    public static void main(String[] args) throws IOException {
        File path = new File("/Users/Hwaipy/Desktop/RRDPS_Code/");
        File aliceFile = new File(path, "20150214131343-s-pc7-34.dat");
        File bobFile = new File(path, "20150214131343-PCALL-APD2-34.dat");
        DataLoader dataLoader = DataLoader.load("20150214131343", aliceFile, bobFile);
    }
}
