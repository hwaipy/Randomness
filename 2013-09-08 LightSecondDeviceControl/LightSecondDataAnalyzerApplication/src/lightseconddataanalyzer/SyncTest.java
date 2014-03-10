package lightseconddataanalyzer;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.RecursionCoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.TimeCalibrator;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public class SyncTest {

    public static void main(String[] args) throws IOException, DeviceException {
        File pxiFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-02-21 改进TDC同步测试/20140221202407TDC.dat");
        File caliFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-02-21 改进TDC同步测试/cali.csv");
        TimeEventLoader pxiLoader = new PXI40PS1Loader(pxiFile, caliFile);
        TimeEventSegment pxiSegment = TimeEventDataManager.loadTimeEventSegment(pxiLoader);
//        File hhFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-06 PXI_TDC时间精度复测/20131206143127.ht2");
//        TimeEventLoader hhLoader = new HydraHarp400T2Loader(hhFile);
//        TimeEventSegment hhSegment = TimeEventDataManager.loadTimeEventSegment(hhLoader);

        TimeEventList pxiSyncList = pxiSegment.getEventList(0);
        TimeEventList pxiSignalList = pxiSegment.getEventList(1);
//        TimeEventList hhSyncList = hhSegment.getEventList(HydraHarp400T2Loader.CHANNEL_SYNC);
//        TimeEventList hhSignalList = hhSegment.getEventList(0);
        System.out.println("PXI:\tSync " + pxiSyncList.size() + "\tSignal " + pxiSignalList.size());
//        System.out.println("HH:\tSync " + hhSyncList.size() + "\tSignal " + hhSignalList.size());
//        long coarseDelay = hhSyncList.get(0).getTime() - pxiSyncList.get(0).getTime();
//        System.out.println("CoarseDelay " + coarseDelay);

//        RecursionCoincidenceMatcher cm = new RecursionCoincidenceMatcher(pxiSyncList, hhSyncList, 100000, coarseDelay);
//        System.out.println("Sync coincidence find " + cm.find());
//        TimeCalibrator timeCalibrator = new TimeCalibrator(cm, hhSignalList);
//        timeCalibrator.calibrate();
////        timeCalibrator.calibrateOverall();
//        CoincidenceMatcher cmSignal = new CoincidenceMatcher(pxiSignalList, hhSignalList, 5, 0);
//        for (int delay = 2500; delay < 5000; delay += 10) {
//            cmSignal.setDelay(delay);
//            System.out.println(delay + "\t" + cmSignal.find());
//        }
        CoincidenceMatcher cm = new CoincidenceMatcher(pxiSyncList, pxiSignalList, 7, 0);
        for (int delay = -9000; delay < -6000; delay += 15) {
            cm.setDelay(delay);
            int cc = cm.find();
            if (cc > 0) {
                System.out.println(delay + "\t" + cc);
            }
        }
    }
}
