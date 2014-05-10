package com.hwaipy.apple.iso.backup;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

/**
 *
 * @author HwaipyLab
 * @param <T>
 */
public abstract class SQLiteMessageDatabase<T extends Message> extends MessageDatabase<T> {

    private Connection connection;

    public SQLiteMessageDatabase(Path path) {
        super(path);
    }

    @Override
    public void initialize() throws DatabaseException {
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.setSharedCache(true);
            config.enableRecursiveTriggers(true);
            SQLiteDataSource ds = new SQLiteDataSource(config);
            ds.setUrl("jdbc:sqlite:" + path);
            connection = ds.getConnection();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public void dispose() throws DatabaseException {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    protected Connection getConnection() {
        return connection;
    }
}
