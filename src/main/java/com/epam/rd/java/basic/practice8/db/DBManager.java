package com.epam.rd.java.basic.practice8.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DBManager {

    private static DBManager dbManager;
    private static Connection connection;

    private static final String SQL_INSERT_USER = "INSERT INTO users VALUES (DEFAULT ,?)";


    public static Connection getConnection() {
        try (InputStream is = new FileInputStream("app.properties")) {
            Properties prop = new Properties();
            prop.load(is);
            return DriverManager.getConnection(prop.getProperty("connection.url"));
        } catch (SQLException | IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
        return null;
    }

    private DBManager() {
        connection = getConnection();
    }

    public static synchronized DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public Connection getConnection(String connectionUrl) throws SQLException {
        return null;
    }

}
