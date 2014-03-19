package lightseconddataanalyzer.formal;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
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
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class DelayScanner {

    public static void main(String[] args) throws Exception {
        File moonFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-21 时间精度复查/20131222205651M.dat");
        TimeEventLoader moonLoader = new PXI40PS1Loader(moonFile);
        TimeEventSegment moonSegment = TimeEventDataManager.loadTimeEventSegment(moonLoader);
        File earthFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-21 时间精度复查/20131222205651E.ht2");
        TimeEventLoader earthLoader = new HydraHarp400T2Loader(earthFile);
        TimeEventSegment earthSegment = TimeEventDataManager.loadTimeEventSegment(earthLoader);

        TimeEventList moonSecondList = moonSegment.getEventList(0);
        TimeEventList moonSyncList = moonSegment.getEventList(1);
        TimeEventList moonSignalHList = moonSegment.getEventList(2);
        TimeEventList moonSignalVList = moonSegment.getEventList(3);
        TimeEventList moonMarkerList = moonSegment.getEventList(4);
        TimeEventList earthSecondList = earthSegment.getEventList(HydraHarp400T2Loader.CHANNEL_SYNC);
        TimeEventList earthMarkerList = earthSegment.getEventList(HydraHarp400T2Loader.CHANNEL_MARKER_1);
        TimeEventList earthSignalHList = earthSegment.getEventList(0);
        TimeEventList earthSignalVList = earthSegment.getEventList(1);
        TimeEventList earthSyncList = earthSegment.getEventList(2);
        System.out.println("Moon Data: Second " + moonSecondList.size()
                + ",\tSync " + moonSyncList.size()
                + ",\tMarker " + moonMarkerList.size()
                + ",\tH " + moonSignalHList.size()
                + ",\tV " + moonSignalVList.size());
        System.out.println("Earth Data: Second " + earthSecondList.size()
                + ",\tSync " + earthSyncList.size()
                + ",\tMarker " + earthMarkerList.size()
                + ",\tH " + earthSignalHList.size()
                + ",\tV " + earthSignalVList.size());

        long coarseDelay = earthSecondList.get(1).getTime() - moonSecondList.get(0).getTime();
//        System.out.println("CoarseDelay is " + coarseDelay);
//        RecursionCoincidenceMatcher cmSecond = new RecursionCoincidenceMatcher(moonSecondList, earthSecondList, 1000000000, coarseDelay);
//        System.out.println("Second coincidence find " + cmSecond.find());
//        TimeCalibrator.calibrate(cmSecond, earthSyncList);
////        TimeCalibrator.calibrate(cmSecond, earthMarkerList);
//        TimeCalibrator.calibrate(cmSecond, earthSignalHList);
//        TimeCalibrator.calibrate(cmSecond, earthSignalVList);

        RecursionCoincidenceMatcher cmSync = new RecursionCoincidenceMatcher(moonSyncList, earthSyncList, 100000, coarseDelay);
        System.out.println("Sync coincidence find " + cmSync.find());
//        TimeCalibrator.calibrate(cmSecond, earthMarkerList);
        TimeCalibrator.calibrate(cmSync, earthSignalHList);
        TimeCalibrator.calibrate(cmSync, earthSignalVList);

        CoincidenceMatcher cmSignalHH = new CoincidenceMatcher(moonSignalHList, earthSignalHList, 30, 0);
        CoincidenceMatcher cmSignalHV = new CoincidenceMatcher(moonSignalHList, earthSignalVList, 30, 0);
        CoincidenceMatcher cmSignalVH = new CoincidenceMatcher(moonSignalVList, earthSignalHList, 30, 0);
        CoincidenceMatcher cmSignalVV = new CoincidenceMatcher(moonSignalVList, earthSignalVList, 30, 0);
        for (int delay = -41000; delay < 28000; delay += 10) {
            cmSignalHH.setDelay(delay);
            cmSignalHV.setDelay(delay);
            cmSignalVH.setDelay(delay);
            cmSignalVV.setDelay(delay);
            System.out.println(delay + "\t" + cmSignalHH.find() + "\t" + cmSignalHV.find()
                    + "\t" + cmSignalVH.find() + "\t" + cmSignalVV.find());
        }
    }
}
