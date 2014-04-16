package com.hwaipy.storage;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Hwaipy
 */
public class Test {

    public static void main(String[] args) throws SQLException, InterruptedException {
        Path dataStoragyPath = com.hwaipy.systemutilities.Paths.getDataStoragyPath();

        File dbFile = new File(dataStoragyPath.toFile(), "h2databasetest");System.out.println(dbFile.getAbsolutePath());
        String dbURL = "jdbc:h2:" + dbFile.getAbsolutePath();

        Connection connection = DriverManager.getConnection(dbURL, "sa", "");
        Statement statement = connection.createStatement();
        System.out.println("OK");
    }
}
