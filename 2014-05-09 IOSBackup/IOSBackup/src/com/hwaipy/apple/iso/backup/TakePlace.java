package com.hwaipy.apple.iso.backup;

import com.hwaipy.apple.iso.backup.mbdb.MBDBEntry;
import com.hwaipy.apple.iso.backup.mbdb.MBDBParseException;
import com.hwaipy.apple.iso.backup.mbdb.MBDBParser;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class TakePlace {

    public static void main(String[] args) throws URISyntaxException, IOException, MBDBParseException {
        String pathString = "/Users/Hwaipy/Desktop/iPhone/56663e8234bb23c962c723c360bdaa2496ffa31d 1/Manifest.mbdb";
        MBDBParser parse = MBDBParser.parse(new File(pathString).toPath());
        Iterator<MBDBEntry> iterator = parse.iterator();
        int c = 0;
        while (iterator.hasNext()) {
            MBDBEntry mBDBEntry = iterator.next();
//            System.out.println(mBDBEntry.getDomain());
            c++;
        }
        System.out.println(c);
    }
}
