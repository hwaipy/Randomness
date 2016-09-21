package qrng;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.MergedTimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.Iterator;
import static qrng.Parameters.delay1;
import static qrng.Parameters.delay2;
import static qrng.Parameters.dir;
import static qrng.Parameters.halfGate;
import static qrng.Parameters.pulsePeriod;

/**
 * pycharm
 *
 * @author Hwaipy
 */
public class ToBin {

  public static void main(String[] args) throws Exception {
    File[] files = dir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".sli");
      }
    });
    for (File file : files) {
      parseToBin(file.getAbsolutePath());
      break;
    }
  }

  private static void parseToBin(String filePath) throws Exception {
    File file = new File(filePath);
    System.out.print(file.getName() + "\t");
    TimeEventLoader loader = new HydraHarp400T2Loader(file);
    TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);
    TimeEventList c1List = segment.getEventList(0);
    TimeEventList c2List = segment.getEventList(1);
    TimeEventList cSList = segment.getEventList(8);
    System.out.print(cSList.size() + "\t");
    System.out.print(c1List.size() + "\t");
    System.out.print(c2List.size() + "\t");
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

//    for (int i = 0; i < 100; i++) {
//      System.out.println(c1List.get(i + 1).getTime() - c1List.get(i).getTime());
//    }
    Iterator<TimeEvent> csIterator = cSList.iterator();
    TimeEvent currentSync = csIterator.next();
    TimeEvent nextSync = csIterator.next();
    int currentSyncIndex = 0;
    Iterator<TimeEvent> sIterator = new MergedTimeEventList(c1List, c2List).iterator();
    TimeEvent currentSignal = sIterator.next();
    int[] valid = new int[cSList.size() * Parameters.pulsePerSync];
    while (true) {
      if (currentSignal.getTime() > nextSync.getTime() - halfGate) {
        currentSync = nextSync;
        currentSyncIndex++;
        if (csIterator.hasNext()) {
          nextSync = csIterator.next();
        } else {
          break;
        }
        continue;
      }
      long delta = currentSignal.getTime() - currentSync.getTime();
      double logicI = (delta + halfGate) / (double) pulsePeriod;
      double logicFullGate = ((double) halfGate) * 2 / pulsePeriod;
      if ((logicI - (int) logicI < logicFullGate) && logicI >= 0) {
        int pulseIndex = currentSyncIndex * Parameters.pulsePerSync + (int) logicI;
        if (pulseIndex < valid.length) {
          valid[pulseIndex] |= currentSignal.getChannel() == 0 ? 1 : 2;
        }
      } else {
//        System.out.println(":::" + logicI);
//        System.out.println(currentSignal.getTime());
//        System.out.println(currentSync.getTime());
//        System.out.println(nextSync.getTime());
      }
      if (sIterator.hasNext()) {
        currentSignal = sIterator.next();
      } else {
        break;
      }
    }
    try (PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath() + ".bin")))) {
      for (int i : valid) {
        printWriter.print("" + i);
      }
    }
    System.out.println();
  }
}
