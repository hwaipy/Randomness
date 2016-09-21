package qrng;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import static qrng.Parameters.dir;

/**
 *
 * @author Hwaipy
 */
public class RNGB {

  /**
   * @param args the command line arguments
   * @throws java.lang.Exception
   */
  public static void main(String[] args) throws Exception {
    File[] files = dir.listFiles((File dir1, String name) -> name.endsWith(".bin"));
    for (File file : files) {
      new RNGB(file).convert();
    }
    if (rafOut != null) {
      rafOut.close();
    }
  }
  private static long count = 0;
  private static int rafOutID = 0;
  private static RandomAccessFile rafOut;

  private static void output(byte b) throws IOException {
    if (rafOut == null) {
      rafOut = new RandomAccessFile("RNG" + rafOutID, "rw");
      rafOutID++;
    }
    rafOut.write(b);
    count++;
    if (count == 1000000 * 127) {
      rafOut.close();
      rafOut = null;
      count = 0;
    }
  }
  private final File file;

  private RNGB(File file) {
    this.file = file;
  }

  private void convert() throws Exception {
    try (RandomAccessFile rafIn = new RandomAccessFile(file, "r")) {
      byte[] buffer = new byte[(int) rafIn.length()];
      rafIn.readFully(buffer);
      ArrayList<Integer> bufferB = new ArrayList<>();
      for (byte b : buffer) {
        switch (b) {
          case '1':
            bufferB.add(0);
            break;
          case '2':
            bufferB.add(1);
            break;
          default:
            break;
        }
        if (bufferB.size() == 8) {
          byte d = convert(bufferB);
          output(d);
          bufferB.clear();
        }
      }
    }
  }

  private byte convert(ArrayList<Integer> bufferB) {
    int d = 0;
    for (Integer i : bufferB) {
      switch (i) {
        case 0:
          d += 0;
          break;
        case 1:
          d += 1;
          break;
        default:
          throw new RuntimeException();
      }
      d <<= 1;
    }
    d >>= 1;
    return (byte) d;
  }
}
