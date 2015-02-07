package com.hwaipy.rrdps;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Hwaipy
 */
public class Process {

    public static void main(String[] args) throws IOException, DeviceException {
        String id = "20150130125829";
        File path = new File("/Users/Hwaipy/desktop/rrdpsdata/");
        long delay1 = 97751800;
        long delay2 = 97744700;
        Experiment experiment = new Experiment(id, path);
        experiment.loadData();
        experiment.sync(delay1, delay2);
        experiment.filterAndMerge(1000, 258000);
        ArrayList<Decoder.Entry> result = experiment.decoding(2000);
//        experiment.test();

    }
}
