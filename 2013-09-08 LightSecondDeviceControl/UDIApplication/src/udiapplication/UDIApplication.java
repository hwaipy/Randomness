package udiapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 *
 * @author Hwaipy
 */
public class UDIApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File("test.map");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, (long) Integer.MAX_VALUE + 2, Integer.MAX_VALUE);

    }
}
