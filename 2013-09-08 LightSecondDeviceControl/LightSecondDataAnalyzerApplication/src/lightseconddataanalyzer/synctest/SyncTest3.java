package lightseconddataanalyzer.synctest;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.MergedTimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import lightseconddataanalyzer.fourfold.CoincidenceScaner;

/**
 *
 * @author Hwaipy
 */
public class SyncTest3 {

  public static void main(String[] args) throws IOException, DeviceException {
    File pxiFileA = new File("/Users/Hwaipy/Desktop/2016-09-17/20160917213402-ATDC.dat");
    TimeEventLoader pxiLoaderA = new PXI40PS1Loader(pxiFileA, null);
    TimeEventSegment pxiSegmentA = TimeEventDataManager.loadTimeEventSegment(pxiLoaderA);
    System.out.println("A Loaded");
    File pxiFileB = new File("/Users/Hwaipy/Desktop/2016-09-17/non.dat");
    TimeEventLoader pxiLoaderB = new PXI40PS1Loader(pxiFileB, null);
    TimeEventSegment pxiSegmentB = TimeEventDataManager.loadTimeEventSegment(pxiLoaderB);
    System.out.println("B Loaded");

    System.out.print("TDC-A: ");
    for (int i = 0; i < 16; i++) {
      System.out.print(pxiSegmentA.getEventCount(i) + ", ");
    }
    System.out.println();
    System.out.print("TDC-B: ");
    for (int i = 0; i < 16; i++) {
      System.out.print(pxiSegmentB.getEventCount(i) + ", ");
    }
    System.out.println();

    TimeEventList syncListA = pxiSegmentA.getEventList(2);
    TimeEventList syncListB = pxiSegmentB.getEventList(2);
    System.out.println("PXIA:\tSync " + syncListA.size());
    System.out.println("PXIB:\tSync " + syncListB.size());
//    checkSync(syncListA);
//    checkSync(syncListB);

//    long coarseDelay = syncListB.get(0).getTime() - syncListA.get(0).getTime();
//    System.out.println("CoarseDelay " + coarseDelay);
//    RecursionCoincidenceMatcher cmSync = new RecursionCoincidenceMatcher(syncListA, syncListB, 1000000, coarseDelay);
//    System.out.println("Sync coincidence find " + cmSync.find());
//    for (int i = 8; i < 12; i++) {
//      new TimeCalibrator(new SkippedIteratable<>(cmSync, 9), pxiSegmentB.getEventList(i)).calibrate();
//    }
    TimeEventList triggerList = pxiSegmentA.getEventList(0);
    TimeEventList[] signalLists = new TimeEventList[9];
//    for (int i = 0; i < 4; i++) {
//      signalLists[i] = pxiSegmentB.getEventList(i + 8);
//    }
    for (int i = 0; i < 9; i++) {
      signalLists[i] = pxiSegmentA.getEventList(i + 6);
    }
    System.out.print("TDC: ");
    for (int i = 0; i < 9; i++) {
      System.out.print(signalLists[i].size() + ", ");
    }
    System.out.println();

//    long[] delays = new long[]{0, -5500, -3500, -7000, 1500, -1000, 1200, -1500, 500};
    long[] delays = new long[]{-4000, -9200, -7500, -11000, -7000, -10000, -7500, -10500, -8500};
    for (int i = 0; i < 9; i++) {
      TimeEventList list = signalLists[i];
      for (int j = 0; j < list.size(); j++) {
        TimeEvent te = list.get(j);
        if (te.getTime() > 0) {
          list.set(new TimeEvent(te.getTime() + delays[i], i), j);
        }
      }
    }
    long triggerDelay = -700000;
    for (int i = 0; i < triggerList.size(); i++) {
      TimeEvent te = triggerList.get(i);
      if (te.getTime() > 0) {
        triggerList.set(new TimeEvent(te.getTime() + triggerDelay, 0), i);
      }
    }

    int[][] histograms = new int[9][0];
    for (int i = 0; i < 9; i++) {
      CoincidenceScaner cs0 = new CoincidenceScaner(triggerList, signalLists[i]);
      histograms[i] = cs0.scan();
    }
    for (int i = 0; i < histograms[0].length; i++) {
      System.out.print((i * 100 - 1000000) + "\t");
      for (int j = 0; j < 9; j++) {
        System.out.print(histograms[j][i] + "\t");
      }
      System.out.println();
    }
    doSampling(triggerList, signalLists);

//    for (TimeEventList signalList : signalLists) {
//      System.out.println("list---");
//      Iterator<TimeEvent> it222 = signalList.iterator();
//      while (it222.hasNext()) {
//        TimeEvent next = it222.next();
//        if (next.getTime() == 1728086845942l) {
//          System.out.println(next.getTime() + ", " + next.getChannel());
//        }
//      }
//    }
  }

  private static boolean checkSync(TimeEventList list) {
    boolean check = true;
    Iterator<TimeEvent> iterator = list.iterator();
    TimeEvent te1 = iterator.next();
    while (iterator.hasNext()) {
      TimeEvent te2 = iterator.next();
      double delta = te2.getTime() - te1.getTime();
      if (delta > 15000000) {
        System.out.println("Wrong in " + te2 + ":      " + delta);
        check = false;
      }
      te1 = te2;
    }
    return check;
  }

  private static void doSampling(TimeEventList triggerList, TimeEventList[] dataLists) {
    Sampler sampler = new Sampler(triggerList, dataLists);
    System.out.println(sampler.result[0]);
    System.out.println(sampler.result[1]);
  }

  private static class Sampler {

    private final TimeEventList triggerList;
    private final TimeEventList[] dataLists;
    private final long gateWidth = 8000;
    private final long sliceLength = 263000;
    private final Iterator<TimeEvent> iterator;
    private TimeEvent buffer = null;
    private final Iterator<TimeEvent> triggerIterator;
    private final ArrayList<Integer> coinsList = new ArrayList<>();
    private final int[] modes = new int[1 << 9];
    private long triggerBuffer = -1;
    private final int maxMask = 1 << 9;
    private final ArrayList[] result;

    public Sampler(TimeEventList triggerList, TimeEventList[] dataLists) {
      this.triggerList = triggerList;
      this.dataLists = dataLists;

      MergedTimeEventList mergedTimeEventList = new MergedTimeEventList(dataLists[0], dataLists[1]);
      for (int i = 2; i < 9; i++) {
        mergedTimeEventList = new MergedTimeEventList(mergedTimeEventList, dataLists[i]);
      }
      iterator = new ValidIterator(mergedTimeEventList.iterator());
      triggerIterator = new ValidIterator(triggerList.iterator());
      loadNextTriggerToBuffer();
      loadNextToBuffer();
      HashSet<Integer> validModeSet = getValidModeSet();

      System.out.println(validModeSet);

      while (buffer != null) {
        int mode = loadNextCoincident();
        if (mode >= 0 && mode < maxMask && validModeSet.contains(mode)) {
          coinsList.add(mode);
          modes[mode] += 1;
        }
      }
      result = convertToMatchedCoins(modes, coinsList);
    }

    private int loadNextCoincident() {
      long startTime = buffer.getTime();
      long stopTime = startTime + gateWidth;
      LinkedList<Integer> modes = new LinkedList<>();
      LinkedList<Long> times = new LinkedList<>();
      int mode = (1 << buffer.getChannel());
      int count = 0;
      while (buffer != null && buffer.getTime() <= stopTime) {
        mode |= (1 << buffer.getChannel());
        modes.add(buffer.getChannel());
        times.add(buffer.getTime());
        count += 1;
        loadNextToBuffer();
      }
      if (count >= 5) {
        System.out.println(modes);
        System.out.println(times);
        System.out.println(count);
      }
      return mode;
    }

    private boolean inTriggerRange(long time) {
//      while (triggerBuffer >= 0 && triggerBuffer + sliceLength < time) {
//        loadNextTriggerToBuffer();
//      }
//      return triggerBuffer >= 0 && triggerBuffer <= time;
      return true;
    }

    private void loadNextToBuffer() {
      buffer = null;
      while (iterator.hasNext() && buffer == null) {
        TimeEvent next = iterator.next();
        if (inTriggerRange(next.getTime())) {
          buffer = next;
        }
      }
    }

    private void loadNextTriggerToBuffer() {
      long nt = -1;
      if (triggerIterator.hasNext()) {
        nt = triggerIterator.next().getTime();
      }
      triggerBuffer = nt;
    }

    private ArrayList[] convertToMatchedCoins(int[] allModes, ArrayList<Integer> coinsList) {
      int mode = 9;
      int subMode = 5;
      ArrayList<ArrayList<Integer>> arranges = listArrange(1, mode + 1, subMode);
      int[] modes = new int[arranges.size()];
      for (int i = 0; i < modes.length; i++) {
        ArrayList<Integer> arrange = arranges.get(i);
        int mask = 0;
        for (Integer a : arrange) {
          mask |= (1 << (a - 1));
        }
        modes[i] = mask;
      }
      int[] matchedCoins = new int[modes.length];
      for (int i = 0; i < matchedCoins.length; i++) {
        matchedCoins[i] = allModes[modes[i]];
      }
      HashSet<Integer> validModesSet = new HashSet<>();
      for (int m : modes) {
        validModesSet.add(m);
      }
      ArrayList<Integer> ra = new ArrayList<>();
      for (int matchedCoin : matchedCoins) {
        ra.add(matchedCoin);
      }
      ArrayList<Integer> rb = new ArrayList<>();
      for (int c : coinsList) {
        if (validModesSet.contains(c)) {
          rb.add(c);
        }
      }
      return new ArrayList[]{ra, rb};
    }

    private HashSet<Integer> getValidModeSet() {
      int mode = 9;
      int subMode = 5;
      ArrayList<ArrayList<Integer>> arranges = listArrange(1, mode + 1, subMode);
      int[] modes = new int[arranges.size()];
      for (int i = 0; i < modes.length; i++) {
        ArrayList<Integer> arrange = arranges.get(i);
        int mask = 0;
        for (Integer a : arrange) {
          mask |= (1 << (a - 1));
        }
        modes[i] = mask;
      }
      HashSet<Integer> validModesSet = new HashSet<>();
      for (int m : modes) {
        validModesSet.add(m);
      }
      return validModesSet;
    }

    private ArrayList<ArrayList<Integer>> listArrange(int from, int to, int deepth) {
      ArrayList<ArrayList<Integer>> arranges = new ArrayList<>();
      for (int i = from; i < to; i++) {
        int current = i;
        if (deepth > 1) {
          ArrayList<ArrayList<Integer>> nexts = listArrange(i + 1, to, deepth - 1);
          for (ArrayList<Integer> next : nexts) {
            next.add(0, current);
            arranges.add(next);
          }
        } else {
          ArrayList<Integer> list = new ArrayList<>();
          list.add(current);
          arranges.add(list);
        }
      }
      return arranges;
    }
  }

  private static class ValidIterator implements Iterator<TimeEvent> {

    private final Iterator<TimeEvent> it;
    private TimeEvent next;

    public ValidIterator(Iterator<TimeEvent> it) {
      this.it = it;
      load();
    }

    private void load() {
      if (next == null) {
        while (it.hasNext()) {
          TimeEvent nextOfIt = it.next();
          if (nextOfIt.getTime() > 0) {
            next = nextOfIt;
            break;
          }
        }
      }
    }

    @Override
    public boolean hasNext() {
      return next != null;
    }

    @Override
    public TimeEvent next() {
      TimeEvent ret = next;
      next = null;
      load();
      return ret;
    }
  }
}
