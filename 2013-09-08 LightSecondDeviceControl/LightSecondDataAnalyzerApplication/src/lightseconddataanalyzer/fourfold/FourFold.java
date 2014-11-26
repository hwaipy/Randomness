package lightseconddataanalyzer.fourfold;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Hwaipy
 */
public class FourFold {

    public static void main(String[] args) throws IOException, DeviceException {
        File file = new File("/Users/Hwaipy/Desktop/G2/201410161205.ht2");
        HydraHarp400T2Loader loader = new HydraHarp400T2Loader(file);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);

        TimeEventList signalList1 = segment.getEventList(1);
        TimeEventList signalList2 = segment.getEventList(8);
        System.out.println("Signal 1: " + signalList1.size());
        System.out.println("Signal 2: " + signalList2.size());

        CoincidenceScaner scaner = new CoincidenceScaner(signalList1, signalList2);
        List<Integer> stat = scaner.scanOfPeaks();
//        int[] stat = scaner.scan();
        for (int t : stat) {
            System.out.println(t);
        }
    }
}
