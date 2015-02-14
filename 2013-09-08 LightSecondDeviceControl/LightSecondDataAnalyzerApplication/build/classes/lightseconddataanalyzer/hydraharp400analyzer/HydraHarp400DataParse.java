package lightseconddataanalyzer.hydraharp400analyzer;

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
public class HydraHarp400DataParse {

    public static void main(String[] args) throws IOException, DeviceException {
        File file = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2013-12-21 时间精度复查/201312230614.ht2");

        TimeEventLoader loader = new HydraHarp400T2Loader(file);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);
        TimeEventList syncList = segment.getEventList(HydraHarp400T2Loader.CHANNEL_SYNC);
        TimeEventList signalList = segment.getEventList(1);
        System.out.println(syncList.size());
        System.out.println(signalList.size());

        for (int i = 0; i < 1000; i++) {
            System.out.println(signalList.get(i + 1).getTime() - signalList.get(i).getTime());
        }
    }
}
