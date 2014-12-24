package D20141219.AFG3000ArbGen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Hwaipy
 */
public class antiAFG {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File("/Users/Hwaipy/Desktop/positive.TFW");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.skipBytes(512);
        for (int i = 0; i < 100; i++) {
            if (raf.read() == 0) {
                System.out.print(0);
            } else {
                System.out.print(1);
            }
            raf.skipBytes(1999);
        }
        raf.close();
    }
}
