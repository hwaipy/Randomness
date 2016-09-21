package qrng;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * pycharm
 *
 * @author Hwaipy
 */
public class R {

  public static void main(String[] args) throws Exception {
    File dir = new File("/Users/Hwaipy/Desktop/0F/");
    File[] files = dir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".sli");
//        return name.toLowerCase().endsWith(".sli.bin");
      }
    });
    for (File file : Arrays.copyOfRange(files, 0, 1)) {
//      parseToBin(file.getAbsolutePath());
      System.out.print(file.getName() + "\t");
      staticticErrorRate(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4));
      System.out.println("--------------------------------------------------------");
      System.out.println();
    }
  }

  private static void parseToBin(String filePath) throws Exception {
//    RandomAccessFile log = new RandomAccessFile(new File("LOG.csv"), "rw");
//    log.seek(log.length());
    File file = new File(filePath);
//    log.writeBytes(file.getName() + ",");
//    System.out.println(file.exists());
    System.out.println(file.getName() + "\t");
    TimeEventLoader loader = new HydraHarp400T2Loader(file);
    TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);
    TimeEventList c1List = segment.getEventList(0);
    TimeEventList c2List = segment.getEventList(1);
    TimeEventList cSList = segment.getEventList(8);
    System.out.println("Channel 0: " + c1List.size());
    System.out.println("Channel 1: " + c2List.size());
//    System.out.println("Channel S: " + cSList.size());
//    log.writeBytes(c1List.size() + "," + c2List.size() + ",");
    if (segment.getEventList(2).size() + segment.getEventList(3).size() + segment.getEventList(4).size() + segment.getEventList(5).size() + segment.getEventList(6).size() + segment.getEventList(7).size() > 0) {
      throw new RuntimeException();
    }
    long startTime = Math.min(c1List.get(0).getTime(), c2List.get(0).getTime());
    ArrayList<Byte> data1 = extract(c1List, startTime);
    ArrayList<Byte> data2 = extract(c2List, startTime);
//    System.out.println("Extracted 1: " + data1.size() + " [" + onCount(data1) + "]" + (onCount(data1) * 100. / data1.size()) + "%");
//    System.out.println("Extracted 2: " + data2.size() + " [" + onCount(data2) + "]" + (onCount(data2) * 100. / data2.size()) + "%");
//    log.writeBytes(data1.size() + "," + data2.size() + "," + (onCount(data1) * 1. / data1.size()) + "," + onCount(data2) * 1. / data2.size() + ",");

    ArrayList<Byte> data = merge(data1, data2);
    try (PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath() + ".bin")))) {
      for (Byte d : data) {
        printWriter.print("" + d);
      }
    }
//    log.close();
  }

  private static ArrayList<Byte> extract(TimeEventList list, long start) {
    long error = 10000;
    long period = 125000;
    ArrayList<Byte> data = new ArrayList<>();
    Iterator<TimeEvent> iterator = list.iterator();
    TimeEvent event = iterator.next();
    long targetTime = start;
    while (event != null) {
      long eventTime = event.getTime();
      long delta = eventTime - targetTime;
      if (delta > error) {
        targetTime += period;
        data.add((byte) 0);
      } else if (delta < -error) {
        if (iterator.hasNext()) {
          event = iterator.next();
        } else {
          event = null;
        }
      } else {
        targetTime = eventTime + period;
        data.add((byte) 1);
      }
    }
    return data;
  }

  private static int onCount(ArrayList<Byte> data) {
    int on = 0;
    for (Byte b : data) {
      if (b > 0) {
        on++;
      }
    }
    return on;
  }

  private static ArrayList<Byte> merge(ArrayList<Byte> data1, ArrayList<Byte> data2) {
    while (data1.size() > data2.size()) {
      data2.add((byte) 0);
    }
    while (data1.size() < data2.size()) {
      data1.add((byte) 0);
    }
    ArrayList<Byte> data = new ArrayList<>(data1.size());
    for (int i = 0; i < data1.size(); i++) {
      data.add((byte) (data1.get(i) + (data2.get(i) << 1)));
    }
    return data;
  }

  private static void staticticErrorRate(String dataFilePath) throws Exception {
    RandomAccessFile log = new RandomAccessFile(new File("LOG.csv"), "rw");
    log.seek(log.length());
    File file = new File(dataFilePath + ".bin");
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    String dataS = reader.readLine();
//    System.out.println("Readed data: " + dataS.length());
    double[] totals = new double[1000];
    double[] errors = new double[1000];
    int dphoton = 0;
    for (int i = 0; i < dataS.length(); i++) {
      char c = dataS.charAt(i);
      int index = i % totals.length;
      switch (c) {
        case '1':
          totals[index] += 1;
          errors[index] += 1;
          break;
        case '2':
          totals[index] += 1;
          break;
        case '3':
          dphoton++;
          break;
      }
    }

    ArrayList<Double> ers = new ArrayList<>(500);
    for (int i = 0; i < totals.length; i++) {
      ers.add(errors[i] / totals[i]);
    }
    ers.sort(null);
    System.out.println("1st: " + ers.get(ers.size() - 1) + ", " + (1 - ers.get(ers.size() - 1)));
    System.out.println("2nd: " + ers.get(ers.size() - 2) + ", " + (1 - ers.get(ers.size() - 2)));
    System.out.println("3th: " + ers.get(ers.size() - 3) + ", " + (1 - ers.get(ers.size() - 3)));
    log.writeBytes(ers.get(ers.size() - 1) + "," + ers.get(ers.size() - 2) + "," + ers.get(ers.size() - 3) + System.lineSeparator());
    log.close();

    int errorsForCheck = 0;
    int totalsForCheck = 0;
    int errorsForRNG = 0;
    int totalsForRNG = 0;
    for (int i = 0; i < totals.length; i++) {
      double er = errors[i] / totals[i];
      if (er > 0.8) {
        errorsForCheck += errors[i];
        totalsForCheck += totals[i];
      } else {
        errorsForRNG += errors[i];
        totalsForRNG += totals[i];
      }
    }
    System.out.println("::::\t" + errorsForCheck + "\t" + totalsForCheck + "\t" + errorsForRNG + "\t" + totalsForRNG + "\t" + dphoton);
  }
}
