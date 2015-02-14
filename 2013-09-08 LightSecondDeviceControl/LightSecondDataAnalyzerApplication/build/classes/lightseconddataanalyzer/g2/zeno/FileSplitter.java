package lightseconddataanalyzer.g2.zeno;

import java.io.File;
import java.io.RandomAccessFile;

/**
 *
 * @author Hwaipy
 */
public class FileSplitter {

    public static void main(String[] args) throws Exception {
        File fileIn = new File("/Users/Hwaipy/Desktop/data/201412282154.ht2");
        File fileOut = new File("/Users/Hwaipy/Desktop/data/201412282154litter.ht2");
        RandomAccessFile rafIn = new RandomAccessFile(fileIn, "r");
        RandomAccessFile rafOut = new RandomAccessFile(fileOut, "rw");
        byte[] buffer = new byte[1024 * 1024];
        for (int i = 0; i < 1000; i++) {
            rafIn.read(buffer);
            rafOut.write(buffer);
        }
        rafIn.close();
        rafOut.close();
    }
}
