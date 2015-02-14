package lightseconddataanalyzer.hydraharp400analyzer;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T3Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;

/**
 *
 * @author Hwaipy
 */
public class HydraHarp400T3DataParser {

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-21 时间精度复查/201312241317-4.ht3");

        TimeEventLoader loader = new HydraHarp400T3Loader(file, 100000);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);
        TimeEventList signalList = segment.getEventList(0);
        System.out.println(signalList.size());

        for (int i = 0; i < 1000; i++) {
            System.out.println(signalList.get(i).getTime());
//            System.out.println(signalList.get(i + 1).getTime() - signalList.get(i).getTime());
        }
    }
}
