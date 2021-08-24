package com.epam.rd.java.basic.practice8;

import com.epam.rd.java.basic.practice8.db.DBManager;
import org.junit.BeforeClass;
import org.mockito.Spy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Part1StudentTest {

//    private static final String JDBC_DRIVER = "org.h2.Driver";
//    private static final String DB_URL = "jdbc:h2:~/test";
//    private static final String URL_CONNECTION = "jdbc:h2:~/test;user=youruser;password=yourpassword";
//    private static final String USER = "youruser";
//    private static final String PASS = "yourpassword";
//    private static final String SQL_CREATE_USERS =
//            "CREATE TABLE IF NOT EXISTS users (\n" +
//            "  id INTEGER(11) NOT NULL AUTO_INCREMENT,\n" +
//            " login VARCHAR(20) NOT NULL, \n" +
//            "  PRIMARY KEY (id));";
//
//    @Spy //actually this annotation is not necessary here
//    private static DBManager dbManager;
//
//    @BeforeClass
//    public static void beforeTest() throws SQLException, ClassNotFoundException {
//        Class.forName(JDBC_DRIVER);
//
//        try (OutputStream output = new FileOutputStream("app.properties")) {
//            Properties prop = new Properties();
//            prop.setProperty("connection.url", URL_CONNECTION);
//            prop.store(output, null);
//        } catch (IOException io) {
//            io.printStackTrace();
//        }
//
//        dbManager = DBManager.getInstance();
//
//        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
//             Statement statement = con.createStatement()) {
//            statement.executeUpdate(SQL_CREATE_USERS);
//        }
//    }

}