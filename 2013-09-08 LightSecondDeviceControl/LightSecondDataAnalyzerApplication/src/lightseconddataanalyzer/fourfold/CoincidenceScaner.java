package lightseconddataanalyzer.fourfold;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;

/**
 *
 * @author Administrator
 */
public class CoincidenceScaner {

  private static final int SCAN_RANGE = 1000000;
  private final TimeEventList list1;
  private final TimeEventList list2;

  public CoincidenceScaner(TimeEventList list1, TimeEventList list2) {
    this.list1 = list1;
    this.list2 = list2;
  }

  public int[] scan() {
    int stepLength = 100;
    int channelCount = SCAN_RANGE * 2 / stepLength + 1;
    int[] stat = new int[channelCount];
    int indexStart2 = 0;
    for (int index1 = 0; index1 < list1.size(); index1++) {
      long time1 = list1.get(index1).getTime();
      long time2Start = time1 - SCAN_RANGE;
      long time2End = time1 + SCAN_RANGE;
      while (indexStart2 < list2.size()) {
        long time2 = list2.get(indexStart2).getTime();
        if (time2 > time2Start) {
          break;
        }
        indexStart2++;
      }
      int index2 = indexStart2;
      while (index2 < list2.size()) {
        long time2 = list2.get(index2).getTime();
        if (time2 > time2End) {
          break;
        }
        int diffTime = (int) (time2 - time1 + SCAN_RANGE);
        int diffChannel = diffTime / stepLength;
        if (diffChannel < stat.length && diffChannel >= 0) {
          stat[diffChannel]++;
        }
        index2++;
      }
    }
    return stat;
  }
}
