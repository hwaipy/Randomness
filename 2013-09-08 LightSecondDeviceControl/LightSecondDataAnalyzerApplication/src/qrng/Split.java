package qrng;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import static qrng.Parameters.dir;

/**
 *
 * @author Hwaipy
 */
public class Split {

  public static void main(String[] args) throws IOException {
    File[] files = dir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".ptu");
      }
    });
    for (File file : files) {
      System.out.println("Slicing " + file.getName());
      split(file);
    }
  }

  private static void split(File dataFile) throws IOException {
    long sliceBegin = 8000;
    long sliceLength = 100000000;
    int fileIndex = 0;
    while (true) {
      long sliceEnd = Math.min(sliceBegin + sliceLength, dataFile.length());
      if (sliceEnd == sliceBegin) {
        break;
      }
      RandomAccessFile raf = new RandomAccessFile(dataFile, "r");
      MappedByteBuffer map = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, sliceBegin, sliceEnd - sliceBegin);
      File outputFile = new File(dataFile.getAbsolutePath() + "-" + fileIndex + ".sli");
      RandomAccessFile outputRaf = new RandomAccessFile(outputFile, "rw");
      outputRaf.getChannel().write(map);
      outputRaf.close();
      raf.close();
      System.out.println("Block " + fileIndex + " finished.");
      fileIndex++;
      sliceBegin = sliceEnd;
    }
  }
}
