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
        int gate = 70;
        process("20150214092147", 1, 0b111111111, 70);
        process("20150214095756", 2, 0b111111111, 70);
        process("20150214100455", 3, 0b111111111, 70);
        process("20150214101614", 4, 0b111111111, 70);
        process("20150214102130", 5, 0b111111111, 70);
        process("20150214102642", 6, 0b111111111, 70);
        process("20150214103609", 7, 0b111111111, 70);
        process("20150214104444", 8, 0b111111111, 70);
        process("20150214105152", 9, 0b111111111, 70);
        process("20150214105852", 10, 0b111111111, 70);
        process("20150214110452", 11, 0b111111111, 70);
        process("20150214111158", 12, 0b111111111, 70);
        process("20150214112513", 13, 0b111111111, 70);
        process("20150214113034", 14, 0b111111111, 70);
        process("20150214113743", 15, 0b111111111, 70);
        process("20150214114246", 16, 0b111111111, 70);
        process("20150214114749", 17, 0b111111111, 70);
        process("20150214115252", 18, 0b111111111, 70);
        process("20150214115755", 19, 0b111111111, 70);
        process("20150214120259", 20, 0b111111111, 70);
        process("20150214120802", 21, 0b111111111, 70);
        process("20150214121305", 22, 0b111111111, 70);
        process("20150214121808", 23, 0b111111111, 70);
        process("20150214122311", 24, 0b111111111, 70);
        process("20150214122814", 25, 0b111111111, 70);
        process("20150214123317", 26, 0b111111111, 70);
        process("20150214123821", 27, 0b111111111, 70);
    }

    public static void process(String id, int index, int pcMask, int gate) throws Exception {
        File path = new File("/Users/Hwaipy/desktop/rrdps/正式/");
        long delay1 = 2495786600l;
        long delay2 = 2495780600l;
        int contrast = 0;
        Experiment.FILENAME_MAP.put(id, new String[]{id + "-s-pc7-" + index + "_时间测量数据.dat", id + "-s-pc7-" + index + "_发射端随机数.dat", id + "-PCALL-APD2-" + index + "_时间测量数据.dat", id + "-PCALL-APD2-" + index + "_接收端随机数.dat", id + "-PCALL-APD2-" + index + "_稳相结果.csv", id + "-PCALL-APD2-" + index + "_稳相数据.csv"});
        Experiment experiment = new Experiment(id, path, pcMask);
        experiment.loadData();
        experiment.sync(delay1, delay2);
        experiment.filterAndMerge(1000, 258000);
        ArrayList<Decoder.Entry> result = experiment.decoding(gate);
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

        FinalResultParser finalResultParser = new FinalResultParser(result, phaseLockingResults);
        finalResultParser.output(new File(new File(path.getParentFile(), "结果"), id + "-" + index + "-result.csv"));
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
