package lightseconddataanalyzer.pxianalyzer;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
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
        File earthFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-03-25 双TDC同步测试/20140331002141TDC.dat");
        File caliFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-03-25 双TDC同步测试/cali01.csv");
//        File file = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-03-25 双TDC同步测试/20140331002141TDC.ht3");

        TimeEventLoader loader = new PXI40PS1Loader(earthFile, caliFile);
//        TimeEventLoader loader = new HydraHarp400T3Loader(file, 100000);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);

        TimeEventList list = segment.getEventList(0);
        System.out.println("Count " + list.size());

        long period = 10000000;
        int inteval = 10;
        int discarded = 0;
        ArrayList<Long> diffList = new ArrayList<>();
        for (int i = 0; i < list.size() - inteval; i += inteval) {
            long diff = list.get(i + inteval).getTime() - list.get(i).getTime();
            if (diff < period * (inteval - 0.5) || diff > period * (inteval + 0.5)) {
                discarded++;
                continue;
            }
            diffList.add(diff);
        }
        System.out.println("Discarded: " + discarded);
        System.out.println("Diff list: " + diffList.size());

        double sum = 0;
        for (long diff : diffList) {
            sum += diff;
        }
        double ave = sum / diffList.size();
        System.out.println("Ave " + ave);

        double varSum = 0;
        double varCount = 0;
        for (long diff : diffList) {
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

        long startKey = statMap.firstKey();
        long endKey = statMap.lastKey();
        for (long i = startKey; i < endKey; i += 1) {
            outputBinCount(statMap, i, 1);
        }
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
