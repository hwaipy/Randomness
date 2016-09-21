package qrng;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import lightseconddataanalyzer.g2.CoincidenceScaner;
import static qrng.Parameters.delay1;
import static qrng.Parameters.delay2;
import static qrng.Parameters.dir;

/**
 * pycharm
 *
 * @author Hwaipy
 */
public class CheckDelay {

  public static void main(String[] args) throws Exception {
    File[] files = dir.listFiles(new FilenameFilter() {
      @Override

      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".sli");
      }
    });
    for (File file : Arrays.copyOfRange(files, 0, 1)) {
      checkDelay(file.getAbsolutePath());
    }
  }

  private static void checkDelay(String filePath) throws Exception {
    File file = new File(filePath);
    System.out.println(file.getName());
    TimeEventLoader loader = new HydraHarp400T2Loader(file);
    TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);
    TimeEventList c1List = segment.getEventList(0);
    TimeEventList c2List = segment.getEventList(1);
    TimeEventList cSList = segment.getEventList(8);
    System.out.println("Channel Sync: " + cSList.size());
    System.out.println("Channel 1: " + c1List.size());
    System.out.println("Channel 2: " + c2List.size());
    if (segment.getEventList(2).size() + segment.getEventList(3).size() + segment.getEventList(4).size() + segment.getEventList(5).size() + segment.getEventList(6).size() + segment.getEventList(7).size() > 0) {
      throw new RuntimeException();
    }

    for (int i = 0; i < c1List.size(); i++) {
      TimeEvent oldEvent = c1List.get(i);
      c1List.set(new TimeEvent(oldEvent.getTime() + delay1, oldEvent.getChannel()), i);
    }
    for (int i = 0; i < c2List.size(); i++) {
      TimeEvent oldEvent = c2List.get(i);
      c2List.set(new TimeEvent(oldEvent.getTime() + delay2, oldEvent.getChannel()), i);
    }

    CoincidenceScaner scaner1 = new CoincidenceScaner(cSList, c1List);
    CoincidenceScaner scaner2 = new CoincidenceScaner(cSList, c2List);
    int[] scan1 = scaner1.scan();
    int[] scan2 = scaner2.scan();
    for (int i = 0; i < scan1.length; i++) {
      int delay = -((int) (scan1.length / 2)) * 100 + 100 * i;
      System.out.println(delay + "\t" + scan1[i] + "\t" + scan2[i]);
    }
  }
}
