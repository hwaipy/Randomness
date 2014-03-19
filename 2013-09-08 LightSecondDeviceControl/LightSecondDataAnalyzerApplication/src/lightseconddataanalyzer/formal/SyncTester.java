package lightseconddataanalyzer.formal;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.Coincidence;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T3Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Hwaipy
 */
public class SyncTester {

    public static void main(String[] args) throws Exception {
        File moonFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-26 流程/20131231034658M.dat");
        File caliFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-26 流程/INL_1222U.csv");
        TimeEventLoader moonLoader = new PXI40PS1Loader(moonFile, null);
        TimeEventSegment moonSegment = TimeEventDataManager.loadTimeEventSegment(moonLoader);
        File earthFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-26 流程/20131231034658E.ht3");
        TimeEventLoader earthLoader = new HydraHarp400T3Loader(earthFile, 100000);
        TimeEventSegment earthSegment = TimeEventDataManager.loadTimeEventSegment(earthLoader);

        TimeEventList moonSecondList = moonSegment.getEventList(0);
        TimeEventList moonSyncList = moonSegment.getEventList(1);
        TimeEventList moonSignalList = moonSegment.getEventList(2);
        TimeEventList moonCheckList = moonSegment.getEventList(1);
        TimeEventList earthSecondList = earthSegment.getEventList(0);
        TimeEventList earthSyncList = earthSegment.getEventList(0);
        TimeEventList earthSignalList = earthSegment.getEventList(2);
        TimeEventList earthCheckList = earthSegment.getEventList(1);
        System.out.println("Moon Data: Second " + moonSecondList.size()
                + ",\tSync " + moonSyncList.size()
                + ",\tSignal " + moonSignalList.size()
                + ",\tCheck " + moonCheckList.size());
        System.out.println("Earth Data: Second " + earthSecondList.size()
                + ",\tSync " + earthSyncList.size()
                + ",\tSignal " + earthSignalList.size()
                + ",\tCheck " + earthCheckList.size());

//        long coarseDelay = earthSecondList.get(19).getTime() - moonSyncList.get(20).getTime();
//        System.out.println("CoarseDelay is " + coarseDelay);
        long scd = earthCheckList.get(0).getTime() - moonCheckList.get(0).getTime();
//        for (int i = 0; i < 9; i++) {
//            long cd = earthCheckList.get(i).getTime() - moonCheckList.get(i).getTime() - scd;
//            System.out.println(cd);
//        }
        CoincidenceMatcher cm = new CoincidenceMatcher(moonSyncList, earthCheckList, 2, 0);
        for (int delay = -500; delay < 500; delay += 5) {
            cm.setDelay(delay + scd);
            System.out.println(delay + "\t" + cm.find());
        }
//        RecursionCoincidenceMatcher cmSync = new RecursionCoincidenceMatcher(moonSyncList, earthSyncList, 100000, coarseDelay);
//        System.out.println("Sync coincidence find " + cmSync.find());
//        TimeCalibrator.calibrate(cmSync, earthSignalList);
//        CoincidenceMatcher cmSignal = new CoincidenceMatcher(moonSignalList, earthSignalList, 2, 0);
//        for (int delay = 9200; delay < 12000; delay += 4) {
//            cmSignal.setDelay(delay);
//            System.out.println(delay + "\t" + cmSignal.find());
//        }
//        System.out.println(earthSignalList.get(0).getTime()-moonSignalList.get(0).getTime());
//        checkDiff("Moon Sync", moonSyncList, 9999000, 10001000);
//        checkDiff("Moon Signal", moonSignalList, 9900000, 10100000);
//        checkDiff("Earth Sync", earthSyncList, 9900000, 10100000);
//        checkDiff("Earth Signal", earthSignalList, 9900000, 10100000);
//        checkDiff("Sync CM", cmSync.iterator());
//        TimeCalibrator.calibrateOverall(cmSync, earthSignalList);
//        checkDiff("Calibrated Earth Signal", earthSignalList, 9999000, 10001000);
//        checkAbs("Moon Signal", moonSignalList, 10, 10000);
//        checkAbs("Moon Signal", moonSignalList, 10, 100000);
//        checkAbs("Moon Signal", moonSignalList, 10, 1000000);
//        checkAbs("Moon Signal", moonSignalList, 10, 10000000);
//        checkAbs("Earth Signal", earthSignalList, 10, 10000);
//        checkAbs("Earth Signal", earthSignalList, 10, 100000);
//        checkAbs("Earth Signal", earthSignalList, 10, 1000000);
//        checkAbs("Earth Signal", earthSignalList, 10, 10000000);
//
//        CoincidenceMatcher cmSync = new CoincidenceMatcher(moonSyncList, earthSyncList, 500, 0);
//        CoincidenceMatcher cmSignal = new CoincidenceMatcher(moonSignalList, earthSignalList, 5000, 0);
//        for (int delay = -100000; delay < 100000; delay += 10000) {
//            cmSync.setDelay(delay + scd);
//            cmSignal.setDelay(delay + scd);
////            System.out.println(delay + "\t" + cmSync.find() + "\t" + cmSignal.find());
//            System.out.println(delay + "\t" + cmSignal.find());
//        }

//        StandardPeriodTimeEventList standardPeriodTimeEventList = new StandardPeriodTimeEventList(0, 100000000, 600000, 12);
//        long coarseDelayMoon = moonSyncList.get(10).getTime();
//        RecursionCoincidenceMatcher rcmMoon = new RecursionCoincidenceMatcher(standardPeriodTimeEventList, moonSyncList, 10000, coarseDelayMoon);
//        System.out.println("Moon Sync find " + rcmMoon.find());
//        TimeCalibrator.calibrate(rcmMoon, moonSignalList);
//        System.out.println("Check");
//        checkDiff("Std Calid moon sync", moonSyncList, 9900000, 10100000);
//        checkDiff("Std Calid earth sync", earthSyncList, 9900000, 10100000);
//        for (int i = 0; i < 1000000; i++) {
//            long earthTime = earthSyncList.get(i).getTime() - coarseDelay - i * 10000000;
//            long moonTime = moonSyncList.get(i).getTime() - i * 10000000;
//            System.out.println(earthTime + "\t" + moonTime);
//        }
//        CoincidenceMatcher cm = new CoincidenceMatcher(moonSyncList, earthSyncList, 2, 0);
//        long cd = earthSyncList.get(0).getTime() - moonSyncList.get(0).getTime();
//        System.out.println("cd = " + cd);
//        for (int delay = -1000; delay < 4000; delay += 5) {
//            cm.setDelay(delay + cd);
//            int find = cm.find();
//            if (find > 0) {
//                System.out.println(delay + "\t" + find);
//            }
//        }
//        Iterator<TimeEvent> iterator = earthSyncList.iterator();
//        TimeEvent event = iterator.next();
//        double sum = 0;
//        int count = 0;
//        while (iterator.hasNext()) {
//            TimeEvent e = iterator.next();
//            long t = e.getTime() - event.getTime();
//            if (t > 99000 && t < 101000) {
////                sum += t;
//                sum += (t - 99999.87870627423) * (t - 99999.87870627423);
//                count++;
//            } else {
//                System.out.println(t);
//            }
//            event = e;
//        }
////        System.out.println(sum / count);
//        System.out.println(Math.sqrt(sum / count));
    }

    private static void checkAbs(String name, TimeEventList list, int start, int count) {
        if (count > list.size() - start - 1) {
            count = list.size() - start - 1;
            System.out.println("Count over" + count);
        }
        long startTime = list.get(start).getTime();
        long endTime = list.get(start + count - 1).getTime();
        double averageTime = (endTime - startTime) / (double) (count - 1);
        long[] actualTimes = new long[count];
        long[] absTimes = new long[count];
        for (int i = 0; i < count; i++) {
            actualTimes[i] = list.get(start + i).getTime();
            absTimes[i] = (long) (startTime + i * averageTime);
//            System.out.println(actualTimes[i] + "\t" + absTimes[i]);
        }
        double sum = 0;
        for (int i = 0; i < absTimes.length; i++) {
            sum += (actualTimes[i] - absTimes[i]) * (actualTimes[i] - absTimes[i]);
            if ((actualTimes[i] - absTimes[i]) > 9000000) {
                System.out.println("!" + i);
            }
        }
        double std = Math.sqrt(sum / count);
        System.out.println("Check ABS " + name + ": Std=" + std);
    }

    private static void checkDiff(String name, TimeEventList list, long min, long max) {
        ArrayList<Long> diffs = new ArrayList<>(list.size());
        for (int i = 0; i < list.size() - 1; i++) {
            TimeEvent event1 = list.get(i);
            TimeEvent event2 = list.get(i + 1);
            long diff = event2.getTime() - event1.getTime();
            if (diff > min && diff < max) {
                diffs.add(diff);
//                System.out.println(diff);
            } else {
                if (diff != 0) {
                    System.out.println(diff);
                }
            }
        }
        checkDiff(name, diffs);
    }

    private static void checkDiff(String name, Iterator<Coincidence> coincidenceIterator) {
        ArrayList<Long> diffs = new ArrayList<>();
        while (coincidenceIterator.hasNext()) {
            Coincidence coincidence = coincidenceIterator.next();
            long diff = coincidence.getTimeDifferent();
//            System.out.println(diff);
            diffs.add(diff);
        }
        checkDiff(name, diffs);
    }

    private static void checkDiff(String name, ArrayList<Long> list) {
        double ave = average(list);
        double std = standardVar(list, ave);
        System.out.println("Check Diff " + name + ": Ave=" + ave + ",Std=" + std);
    }

    private static double average(List<Long> data) {
        double sum = 0;
        for (long d : data) {
            sum += d;
        }
        return sum / data.size();
    }

    private static double standardVar(List<Long> data, double ave) {
        double sum = 0;
        for (long d : data) {
            sum += (d - ave) * (d - ave);
        }
        return Math.sqrt(sum / data.size());
    }
}
