package com.hwaipy.apple.iso.backup;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public class TakePlace {

    public static void main(String[] args) throws IOException, DatabaseException {
        String pathString = "/Users/Hwaipy/Desktop/iPhone/56663e8234bb23c962c723c360bdaa2496ffa31d 1/";
        File addressBookFile = new File(pathString, "31bb7ba8914766d4ba40d6dfb6113c8b614be442");
        AddressBook addressBook = new AddressBook(addressBookFile.toPath());
        addressBook.initialize();
        System.out.println(addressBook.query("+8613817867870"));
    }
}
