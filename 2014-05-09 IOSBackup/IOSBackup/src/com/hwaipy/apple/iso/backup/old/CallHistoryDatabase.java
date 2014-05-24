package com.hwaipy.apple.iso.backup.old;

import com.hwaipy.apple.iso.backup.DatabaseException;
import com.hwaipy.apple.iso.backup.AddressBook;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.TreeSet;

/**
 *
 * @author Hwaipy
 */
public class CallHistoryDatabase extends SQLiteMessageDatabase<Call> {

    public CallHistoryDatabase(Path path) {
        super(path);
    }

    @Override
    public Collection<Call> loadMessages(AddressBook addressBook) throws DatabaseException {
        return loadRecentMessages(-1, addressBook);
    }

    @Override
    public Collection<Call> loadRecentMessages(long laterThan, AddressBook addressBook) throws DatabaseException {
        try {
            String sql = "select ROWID,address,date,duration,flags from call where date>" + laterThan;
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            TreeSet<Call> messages = new TreeSet<>();
            while (resultSet.next()) {
                Call call = new Call();
                call.setAddress(resultSet.getString("address"));
                call.setDate(resultSet.getInt("date"));
                call.setDuration(resultSet.getInt("duration"));
                call.setFlag(resultSet.getInt("flags"));
                call.setName(addressBook.query(call.getAddress()));
                messages.add(call);
            }
            return messages;
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }
}
