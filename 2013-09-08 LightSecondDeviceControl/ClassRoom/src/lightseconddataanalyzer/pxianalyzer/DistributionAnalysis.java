package lightseconddataanalyzer.pxianalyzer;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T3Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author Hwaipy
 */
public class DistributionAnalysis {

    public static void main(String[] args) throws IOException, DeviceException {
        File earthFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-21 时间精度复查/20131223222959TDC.dat");
        File caliFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-21 时间精度复查/INL_1222U.csv");
        File hhFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-21 时间精度复查/201312241354-120M.ht3");

//        TimeEventLoader earthLoader = new PXI40PS1Loader(earthFile, caliFile);
        TimeEventLoader earthLoader = new HydraHarp400T3Loader(hhFile, 100000);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(earthLoader);

        System.out.println(segment.getEventCount());
        TimeEventList list = segment.getEventList(0);
        System.out.println("Count " + list.size());

        int inteval = 1;
        ArrayList<Long> diffList = new ArrayList<>();
        for (int i = 0; i < list.size() - inteval; i += inteval) {
            diffList.add(list.get(i + 1).getTime() - list.get(i).getTime());
        }
//        long[] diffs = new long[list.size() - 1];

        double sum = 0;
        for (long diff : diffList) {
            sum += diff;
        }
        double ave = sum / diffList.size();
        System.out.println("Ave " + ave);

        double varSum = 0;
        double varCount = 0;
        for (long diff : diffList) {
            if (Math.abs(diff - ave) > 1000) {
                continue;
            }
            varSum += (diff - ave) * (diff - ave);
            varCount++;
        }
        double std = Math.sqrt(varSum / varCount);
        System.out.println("Std " + std + "\t (" + (diffList.size() - varCount) + " excluded)");

        TreeMap<Long, Integer> statMap = new TreeMap<>();
        for (long diff : diffList) {
            Integer get = statMap.get(diff);
            if (get == null) {
                statMap.put(diff, 1);
            } else {
                statMap.put(diff, 1 + get);
            }
        }
//
//        Set<Long> keySet = statMap.keySet();
//        for (Long key : keySet) {
//            System.out.println(key + "\t" + statMap.get(key));
//        }
//
        long startKey = statMap.firstKey();
        long endKey = statMap.lastKey();
        for (long i = startKey; i < endKey; i += 1) {
            outputBinCount(statMap, i, 1);
        }
//
//        sum = 0;
//        for (long key = 10000000; key < statMap.lastKey(); key++) {
//            int v = statMap.containsKey(key) ? statMap.get(key) : 0;
//            sum += v;
//            System.out.println(key + "\t" + sum);
//        }
//
//        long standard = list.get(0).getTime();
//        long period = 10000000;
//        TreeMap<Long, Integer> statMap = new TreeMap<>();
//        Iterator<TimeEvent> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            TimeEvent timeEvent = iterator.next();
//            long time = timeEvent.getTime();
//            while (Math.abs(time - standard) > 1000000) {
//                standard += period;
//            }
//            Integer get = statMap.get(time);
//            if (get == null) {
//                statMap.put(time, 1);
//            } else {
//                statMap.put(time, 1 + get);
//            }
//        }
//        long startKey = statMap.firstKey();
//        long endKey = statMap.lastKey();
//        for (long i = startKey; i < endKey; i += 1) {
//            outputBinCount(statMap, i, 1);
//        }
    }

    private static void outputBinCount(TreeMap<Long, Integer> statMap, long startKey, long length) {
        int sum = 0;
        for (long k = startKey; k < startKey + length; k++) {
            if (statMap.containsKey(k)) {
                sum += statMap.get(k);
            }
        }
        if (sum != 0) {
            System.out.println(startKey + "\t" + sum);
        }
    }
}
