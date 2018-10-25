package lightseconddataanalyzer.synctest;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public class SyncTestWC {

    private static TimeEventSegment pxiSegmentA;
    private static TimeEventSegment pxiSegmentB;

    public static void main(String[] args) throws IOException, DeviceException, InterruptedException {
        final File file = new File("/Users/hwaipy/Desktop/test.ptu");
        TimeEventLoader pxiLoaderA = new HydraHarp400T2Loader(file);
        pxiSegmentA = TimeEventDataManager.loadTimeEventSegment(pxiLoaderA);
        System.out.println("Loaded");

        for (int i = 0; i < 9; i++) {
            System.out.print(pxiSegmentA.getEventCount(i) + ", ");
        }
        System.out.println();

        TimeEventList syncList = pxiSegmentA.getEventList(8);

        for (int i = 0; i < 10; i++) {
            System.out.println(syncList.get(i + 1).getTime() - syncList.get(i).getTime());
        }
    }
}
