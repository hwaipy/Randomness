package lightseconddataanalyzer;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.Coincidence;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.RecursionCoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.TimeCalibrator;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T3Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;
import lightseconddataanalyzer.pxianalyzer.SkippedIteratable;

/**
 *
 * @author Hwaipy
 */
public class SyncTest {

    public static void main(String[] args) throws IOException, DeviceException {
        File pxiFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-03-25 双TDC同步测试/20140331002141TDC.dat");
        File caliFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-03-25 双TDC同步测试/cali.csv");
        TimeEventLoader pxiLoader = new PXI40PS1Loader(pxiFile, caliFile);
        TimeEventSegment pxiSegment = TimeEventDataManager.loadTimeEventSegment(pxiLoader);
        File hhFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-03-25 双TDC同步测试/20140331002141TDC.ht3");
        TimeEventLoader hhLoader = new HydraHarp400T3Loader(hhFile, 100000);
        TimeEventSegment hhSegment = TimeEventDataManager.loadTimeEventSegment(hhLoader);

        TimeEventList pxiSyncList = pxiSegment.getEventList(0);
        TimeEventList pxiSignalList = pxiSegment.getEventList(1);
        TimeEventList hhSyncList = hhSegment.getEventList(0);
        TimeEventList hhSignalList = hhSegment.getEventList(1);
        System.out.println("PXI:\tSync " + pxiSyncList.size() + "\tSignal " + pxiSignalList.size());
        System.out.println("HH:\tSync " + hhSyncList.size() + "\tSignal " + hhSignalList.size());
        long coarseDelay = hhSyncList.get(1).getTime() - pxiSyncList.get(0).getTime();
        System.out.println("CoarseDelay " + coarseDelay);
        RecursionCoincidenceMatcher cm = new RecursionCoincidenceMatcher(pxiSyncList, hhSyncList, 10000, coarseDelay);
        System.out.println("Sync coincidence find " + cm.find());

//        for (int i = 0; i < 1000; i++) {
//            System.out.println(i + "\t" + hhSignalList.get(i).getTime());
//        }
//        for (int i = 0; i < 100; i++) {
//            for (int j = 0; j < 100; j++) {
//                long coarseDelay = hhSyncList.get(i).getTime() - pxiSyncList.get(j).getTime();
//                System.out.println("CoarseDelay " + coarseDelay);
//                RecursionCoincidenceMatcher cm = new RecursionCoincidenceMatcher(pxiSyncList, hhSyncList, 100000, coarseDelay);
//                System.out.println(i + ", " + j + ", Sync coincidence find " + cm.find());
//            }
//        }
//        if (true) {
//            return;
//        }
        SkippedIteratable<Coincidence> skippedCm = new SkippedIteratable<>(cm, 9);

        TimeCalibrator timeCalibrator = new TimeCalibrator(skippedCm, hhSignalList);
        timeCalibrator.calibrate();
        //        timeCalibrator.calibrateOverall();
        CoincidenceMatcher cmSignal = new CoincidenceMatcher(pxiSignalList, hhSignalList, 2, 0);
        for (int delay = 0; delay < 1000; delay += 5) {
            cmSignal.setDelay(delay);
            int cc = cmSignal.count();
            if (cc > 0) {
                System.out.println(delay + "\t" + cc);
            }
        }
//        long seg = 100000;
//        CoincidenceMatcher cm = new CoincidenceMatcher(pxiSyncList, hhSyncList, seg / 2, 0);
//        for (long delay = -10 * seg; delay < 10 * seg; delay += seg) {
//            cm.setDelay(delay + coarseDelay);
//            int cc = cm.count();
//            if (cc > 0) {
//                System.out.println(delay + "\t" + cc);
//            }
//        }
    }
}
