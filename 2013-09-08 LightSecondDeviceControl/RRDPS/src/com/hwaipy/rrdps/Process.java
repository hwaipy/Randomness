package com.hwaipy.rrdps;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class Process {

    public static void main(String[] args) throws Exception {
        process("20150214100455", 0b111111111);
//        measureDelay("20150214023231", 1);
    }

    public static void process(String id, int pcMask) throws Exception {
        File path = new File("/Users/Hwaipy/desktop/rrdps/");
        long delay1 = 2495786600l;
        long delay2 = 2495780600l;
        int contrast = 0;
        Experiment experiment = new Experiment(id, path, pcMask);
        experiment.loadData();
        experiment.sync(delay1, delay2);
        experiment.filterAndMerge(1000, 258000);
        ArrayList<Decoder.Entry> result = experiment.decodingWithPhase(800, contrast);
        ResultParser resultParser = new ResultParser(result, 64);
        System.out.println("Encode 0 with " + resultParser.getEncode0Count() + ", ErrorRate = " + resultParser.getEncode0ErrorRate());
        System.out.println("Encode 1 with " + resultParser.getEncode1Count() + ", ErrorRate = " + resultParser.getEncode1ErrorRate());
        System.out.println("APD0: " + resultParser.getApd0Count() + ", ErrorRate = " + resultParser.getApd0ErrorRate());
        System.out.println("APD1: " + resultParser.getApd1Count() + ", ErrorRate = " + resultParser.getApd1ErrorRate());

        System.out.println("--------------------------------");
        PhaseLockingResultSet phaseLockingResults = experiment.getPhaseLockingResults();
        System.out.println("Phase Locking Result");
        System.out.println("Average -> " + phaseLockingResults.average());
        System.out.println("Better than " + contrast + " -> " + 100 * phaseLockingResults.rate(contrast) + "%");

//        System.out.println();
//        int[] encodingCounts = experiment.getEncodingCounts(15);
//        System.out.println(Arrays.toString(encodingCounts));
//        System.out.println();
//        System.out.println();
        resultParser.resultByBobQRNG12(result);
        staticPhaseLockingRandomly(phaseLockingResults, experiment.getBobRandomList());
    }

    private static void measureDelay(String id, int apdIndex) throws Exception {
        File path = new File("/Users/Hwaipy/desktop/rrdps/");
        Experiment experiment = new Experiment(id, path);
        experiment.loadData();
        experiment.measureTiming(apdIndex, 2491030000l, 1000, 10000);
    }

    private static void staticPhaseLockingRandomly(PhaseLockingResultSet phaseLockingResults, TimeEventList list) {
        ArrayList<Double>[] results = new ArrayList[128];
        for (int i = 0; i < 128; i++) {
            results[i] = new ArrayList<>();
        }
        for (int i = 0; i < phaseLockingResults.size(); i++) {
            PhaseLockingResult result = phaseLockingResults.get(i);
            double contrast = result.contrast();
            ExtandedTimeEvent<DecodingRandom> event = (ExtandedTimeEvent) list.get(i);
            DecodingRandom random = event.getProperty();
            int randomNumber = random.getRandom();
            results[randomNumber].add(contrast);
        }
        double[] contrasts = new double[128];
        for (int j = 0; j < 128; j++) {
            ArrayList<Double> result = results[j];
            double sum = 0;
            for (Double d : result) {
                sum += 1 / d;
            }
            contrasts[j] = result.size() / sum;
        }
        for (double contrast : contrasts) {
            System.out.println(contrast);
        }

//        int i = 0;
//        Iterator<PhaseLockingResult> iterator = phaseLockingResults.iterator();
//        while (iterator.hasNext()) {
//            PhaseLockingResult result = iterator.next();
//            double contrast = result.contrast();
//            System.out.print(contrast + "\t");
//            i++;
//            if (i == 128) {
//                i = 0;
//                System.out.println();
//            }
//        }
    }

    private static void staticPhaseLocking(PhaseLockingResultSet phaseLockingResults) {
        ArrayList<Double>[] results = new ArrayList[128];
        for (int i = 0; i < 128; i++) {
            results[i] = new ArrayList<>();
        }
        int i = 0;
        Iterator<PhaseLockingResult> iterator = phaseLockingResults.iterator();
        while (iterator.hasNext()) {
            PhaseLockingResult result = iterator.next();
            double contrast = result.contrast();
            results[i].add(contrast);
            i++;
            if (i == 128) {
                i = 0;
            }
        }
        double[] contrasts = new double[128];
        for (int j = 0; j < 128; j++) {
            ArrayList<Double> result = results[j];
            double sum = 0;
            for (Double d : result) {
                sum += 1 / d;
            }
            contrasts[j] = result.size() / sum;
        }
        for (double contrast : contrasts) {
            System.out.println(contrast);
        }

//        int i = 0;
//        Iterator<PhaseLockingResult> iterator = phaseLockingResults.iterator();
//        while (iterator.hasNext()) {
//            PhaseLockingResult result = iterator.next();
//            double contrast = result.contrast();
//            System.out.print(contrast + "\t");
//            i++;
//            if (i == 128) {
//                i = 0;
//                System.out.println();
//            }
//        }
    }
}
