package com.epam.rd.java.basic.practice8.db;
import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {

    private static DBManager dbManager;

    private DBManager() {
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
