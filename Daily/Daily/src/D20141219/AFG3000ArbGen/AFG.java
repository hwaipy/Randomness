package D20141219.AFG3000ArbGen;

import D20141212.PCCouplingDataAnalysis.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class AFG {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file1 = new File("/Users/Hwaipy/Desktop/positive.TFW");
        File file2 = new File("/Users/Hwaipy/Desktop/negetive.TFW");
        RandomAccessFile raf1 = new RandomAccessFile(file1, "rw");
        RandomAccessFile raf2 = new RandomAccessFile(file2, "rw");
        raf1.skipBytes(512);
        raf2.skipBytes(512);
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            if (random.nextBoolean()) {
                high(raf1);
                low(raf2);
            } else {
                low(raf1);
                high(raf2);
            }
        }
        raf1.close();
        raf2.close();
    }

    private static void high(RandomAccessFile raf) throws IOException {
        for (int i = 0; i < 20; i++) {
            raf.write(0x3F);
            raf.write(0xFE);
        }
        for (int i = 0; i < 1960; i++) {
            raf.write(0x00);
        }
    }

    private static void low(RandomAccessFile raf) throws IOException {
        for (int i = 0; i < 2000; i++) {
            raf.write(0x00);
        }
    }
}
