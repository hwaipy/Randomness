package com.hwaipy.apple.iso.backup.old;

import com.hwaipy.apple.iso.backup.DatabaseException;
import com.hwaipy.apple.iso.backup.AddressBook;
import java.nio.file.Path;
import java.util.Collection;

/**
 *
 * @author HwaipyLab
 */
public class IMessageDatabase extends SQLiteMessageDatabase<IMessage> {

    public IMessageDatabase(Path path) {
        super(path);
    }

    @Override
    public Collection<IMessage> loadMessages(AddressBook addressBook) throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<IMessage> loadRecentMessages(long newerThan, AddressBook addressBook) throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
