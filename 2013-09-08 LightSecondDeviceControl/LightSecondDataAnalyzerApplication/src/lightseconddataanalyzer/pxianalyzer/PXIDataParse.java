package lightseconddataanalyzer.pxianalyzer;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.CalibrationCreator;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public class PXIDataParse {

    public static void main(String[] args) throws IOException, DeviceException {
        File file = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-02-21 改进TDC同步测试/20140221202407TDC.dat");
        File caliFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-21 时间精度复查/INL_1222U.csv");

        TimeEventLoader loader = new PXI40PS1Loader(file, caliFile);

        CalibrationCreator calibrationCreator = new CalibrationCreator(file);
        long[] calibrationMap = calibrationCreator.calibrationMap(1);
        System.out.println(calibrationMap.length);
        for (long l : calibrationMap) {
            System.out.print(l + ";");
        }
        if (true) {
            return;
        }
//        TimeEventLoader loader = new PXI40PS1Loader(file);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);
        TimeEventList syncList = segment.getEventList(1);
//        TimeEventList s1List = segment.getEventList(0);
        TimeEventList signalList = segment.getEventList(2);
//        System.out.println(syncList.size());
        System.out.println(s1List.size());
//        System.out.println(s2List.size());
        System.out.println(sTList.size());

        CoincidenceMatcher cm1 = new CoincidenceMatcher(sTList, s1List, 2, 0);
//        CoincidenceMatcher cm2 = new CoincidenceMatcher(sTList, s2List, 20, 0);
        for (int delay = 20000; delay < 240000; delay += 4) {
            cm1.setDelay(delay);
//            cm2.setDelay(delay);
            int find = cm1.find();
            if (find != 0) {
                System.out.println(delay + "\t" + find
                //                    + "\t" + cm2.find()
                );
            }
        }
    }
}
