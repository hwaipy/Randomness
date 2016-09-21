package lightseconddataanalyzer.pxianalyzer;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CoincidenceMatcher;
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
    File file = new File("/Users/Hwaipy/Documents/Data/Samples/56mTDC.dat");
//    File caliFile = new File();

    TimeEventLoader loader = new PXI40PS1Loader(file, null);
    TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);
    TimeEventList s1List = segment.getEventList(1);
    TimeEventList s2List = segment.getEventList(2);
    System.out.println(s1List.size());
    System.out.println(s2List.size());

//    CoincidenceMatcher cm1 = new CoincidenceMatcher(s1List, s2List, 100, 0);
//    for (int delay = -0; delay < 6011200; delay += 200) {
//      cm1.setDelay(delay);
//      int find = cm1.find();
//      System.out.println(delay + "\t" + find);
//    }
    CoincidenceMatcher cm1 = new CoincidenceMatcher(s1List, s2List, 2000, 0);
    int pulseExp = 80;
    int initDelay = 18000;
    int period = 13160;
    for (int delay = initDelay - period * pulseExp; delay < initDelay + period * pulseExp; delay += period) {
      cm1.setDelay(delay);
      int find = cm1.find();
      System.out.println(delay + "\t" + find);
    }
  }
}
