package lightseconddataanalyzer.synctest;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.RecursionCoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T3Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public class SyncTest2 {

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
        RecursionCoincidenceMatcher cmSync = new RecursionCoincidenceMatcher(pxiSyncList, hhSyncList, 10000, coarseDelay);
        System.out.println("Sync coincidence find " + cmSync.find());
        RecursionCoincidenceMatcher cmSignal = new RecursionCoincidenceMatcher(pxiSignalList, hhSignalList, 10000, coarseDelay);
        System.out.println("Sync coincidence find " + cmSignal.find());

//        TimeCalibrator timeCalibrator = new TimeCalibrator(cm, hhSignalList);
//        timeCalibrator.calibrateByLineFitting();
//        CoincidenceMatcher cmSignal = new CoincidenceMatcher(pxiSignalList, hhSignalList, 2, 0);
//        for (int delay = 0; delay < 1000; delay += 5) {
//            cmSignal.setDelay(delay);
//            int cc = cmSignal.count();
//            if (cc > 0) {
//                System.out.println(delay + "\t" + cc);
//            }
//        }
    }
}
