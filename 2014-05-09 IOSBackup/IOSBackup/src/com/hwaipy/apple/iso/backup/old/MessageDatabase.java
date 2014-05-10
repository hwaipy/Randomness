package com.hwaipy.apple.iso.backup.old;

import java.nio.file.Path;
import java.util.Collection;

/**
 *
 * @author HwaipyLab
 * @param <T>
 */
public abstract class MessageDatabase<T extends Message> {

    protected final Path path;

    public MessageDatabase(Path path) {
        this.path = path;
    }

    public abstract void initialize() throws DatabaseException;

    public abstract void dispose() throws DatabaseException;

    public abstract Collection<T> loadMessages(AddressBook addressBook) throws DatabaseException;

    public abstract Collection<T> loadRecentMessages(long newerThan, AddressBook addressBook) throws DatabaseException;
}
