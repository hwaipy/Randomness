package lightseconddataanalyzer.synctest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hwaipy
 */
public class Deframer {

  private final File dataFile;
  private final ByteBuffer dataBuffer = ByteBuffer.allocate(100000000);
  private static final int FRAME_SIZE = 2048;
  private final CRC16 CRC16 = new CRC16(0x8005);

  private Deframer(File dataFile) {
    this.dataFile = dataFile;
  }

  private void doDeframe() {
    String src = dataFile.getAbsolutePath();
    String tar = src.replace(".dat", "TDC.dat");
    try {
      FileOutputStream out = new FileOutputStream(tar);
      FileInputStream in = new FileInputStream(src);
      byte[] buffer = new byte[1024 * 1024 * 32];
      while (true) {
        int read = in.read(buffer);
        if (read == -1) {
          break;
        }
        offer(buffer, 0, read, out);
      }
      out.close();
    } catch (IOException ex) {
      Logger.getLogger(Deframer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void offer(byte[] data, int start, int length, FileOutputStream out) throws IOException {
    try {
      dataBuffer.put(data, start, length);
    } catch (BufferOverflowException e) {
      throw new IllegalArgumentException("Input data too much.", e);
    }
    dataBuffer.flip();
    while (dataBuffer.hasRemaining()) {
      if (!seekForFrameHead()) {
        break;
      }
      if (dataBuffer.remaining() < FRAME_SIZE) {
        break;
      }
      if (checkFrameTail()) {
//        frameCount++;
        if (crc()) {
//          validFrameCount++;
          int pStart = dataBuffer.position() + 8;
          int pEnd = pStart + FRAME_SIZE - 16;
//          for (int p = pStart; p < pEnd; p += 8) {
//            parseToTimeEvent(p);
//          }
          out.write(dataBuffer.array(), pStart, pEnd - pStart);
          dataBuffer.position(dataBuffer.position() + FRAME_SIZE);
        } else {
          System.out.println("crc failed");
          dataBuffer.position(dataBuffer.position() + FRAME_SIZE);
        }
      } else {
        dataBuffer.position(dataBuffer.position() + 4);
//        skippedInSeekingHead += 4;
      }
    }
    dataBuffer.compact();
//    return timeEvents;

  }

  private boolean seekForFrameHead() {
    int headFlagCount = 0;
    int startPosition = dataBuffer.position();
    while (dataBuffer.hasRemaining()) {
      byte b = dataBuffer.get();
      if (b == -91) {
        headFlagCount++;
      } else {
        headFlagCount = 0;
      }
      if (headFlagCount == 4) {
        dataBuffer.position(dataBuffer.position() - 4);
//        skippedInSeekingHead += (dataBuffer.position() - startPosition);
        return true;
      }
    }
    if (headFlagCount > 0) {
      dataBuffer.position(dataBuffer.position() - headFlagCount);
    }
//    skippedInSeekingHead += (dataBuffer.position() - startPosition);
    return false;
  }

  private boolean checkFrameTail() {
    int tailPosition = dataBuffer.position() + FRAME_SIZE - 8;
    return dataBuffer.get(tailPosition + 2) == 71 && dataBuffer.get(tailPosition + 3) == 71
            && dataBuffer.get(tailPosition + 4) == 71 && dataBuffer.get(tailPosition + 5) == 71
            && dataBuffer.get(tailPosition + 6) == 71 && dataBuffer.get(tailPosition + 7) == 71;
  }

  private boolean crc() {
    int p = dataBuffer.position() + 4;
    int crc = CRC16.calculateCRC(dataBuffer.array(), p, FRAME_SIZE - 12, true);
    int dc1 = dataBuffer.get(p + FRAME_SIZE - 12);
    int dc2 = dataBuffer.get(p + FRAME_SIZE - 11);
    dc1 = dc1 >= 0 ? dc1 : dc1 + 256;
    dc2 = dc2 >= 0 ? dc2 : dc2 + 256;
    int datacrc = dc1 + dc2 * 256;
    return crc == datacrc;
  }

  private class CRC16 {

    private final short[] crcTable = new short[256];
    private final int gPloy;

    public CRC16(int gPloy) {
      this.gPloy = gPloy;
      computeCrcTable();
    }

    private short getCrcOfByte(int aByte) {
      int value = aByte << 8;
      for (int count = 7; count >= 0; count--) {
        if ((value & 0x8000) != 0) {
          value = (value << 1) ^ gPloy;
        } else {
          value = value << 1;
        }
      }
      value = value & 0xFFFF;
      return (short) value;
    }

    private void computeCrcTable() {
      for (int i = 0; i < 256; i++) {
        crcTable[i] = getCrcOfByte(i);
      }
    }

    public int calculateCRC(byte[] data, int offset, int lenth, boolean reverse) {
      int crc = 0;
      for (int i = 0; i < lenth; i++) {
        int ic = i;
        if (reverse) {
          ic = i % 2 == 0 ? i + 1 : i - 1;
        }
        crc = ((crc & 0xFF) << 8) ^ crcTable[(((crc & 0xFF00) >> 8) ^ data[offset + ic]) & 0xFF];
      }
      crc = crc & 0xFFFF;
      return crc;
    }
  }

  public static void main(String[] args) {
    String pathS = "/Users/Hwaipy/Desktop/2016-09-09/";
    File path = new File(pathS);
    File[] dataFiles = path.listFiles((File dir, String name) -> {
      return name.toLowerCase().endsWith(".dat");
    });
    for (File dataFile : dataFiles) {
      Deframer deframer = new Deframer(dataFile);
      deframer.doDeframe();
    }
  }
}
