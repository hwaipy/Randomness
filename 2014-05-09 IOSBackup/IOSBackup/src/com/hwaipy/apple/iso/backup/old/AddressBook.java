package com.hwaipy.apple.iso.backup.old;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

/**
 *
 * @author Hwaipy
 */
public class AddressBook {

    private final Path path;
    private Connection connection;
    private final Map<String, String> addressMap = new HashMap<>();

    public AddressBook(Path path) {
        this.path = path;
    }

    public void initialize() throws DatabaseException {
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.setSharedCache(true);
            config.enableRecursiveTriggers(true);
            SQLiteDataSource ds = new SQLiteDataSource(config);
            ds.setUrl("jdbc:sqlite:" + path);
            connection = ds.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select ABPerson.First,ABPerson.Last,ABMultiValue.value from ABPerson,ABMultiValue where ABPerson.ROWID=ABMultiValue.record_id");
            while (resultSet.next()) {
                String last = resultSet.getString("Last");
                String first = resultSet.getString("First");
                String name = (last == null ? "" : last) + (first == null ? "" : first);
                String value = resultSet.getString("value");
                if (value != null && name != null && !name.isEmpty()) {
                    value = format(value);
                    if (addressMap.containsKey(value)) {
                        String existName = addressMap.get(value);
                        name = existName + "," + name;
                    }
                    addressMap.put(value, name);
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    public String query(String address) throws DatabaseException {
        if (address == null || "".equals(address) || "Unknown".equals(address)) {
            return "Unknown";
        }
        String value = format(address);
        String name = addressMap.get(value);
        if (name != null) {
            return name;
        }
        name = addressMap.get("86" + value);
        if (name != null) {
            return name;
        }
        if (value.startsWith("86")) {
            name = addressMap.get(value.substring(2));
            if (name != null) {
                return name;
            }
        }
        if (value.startsWith("17951")) {
            name = addressMap.get(value.substring(5));
            if (name != null) {
                return name;
            }
        }
        return null;
    }
    private static final Pattern phoneNumberPattern = Pattern.compile("^[-0-9 ()+]+$");

    private String format(String value) {
        Matcher matcher = phoneNumberPattern.matcher(value);
        if (matcher.find()) {
            return value.replaceAll("[- ()+]", "");
        } else {
            return value;
        }
    }
}
