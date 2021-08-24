package com.epam.rd.java.basic.practice8;

import com.epam.rd.java.basic.practice8.db.DBManager;
import org.junit.BeforeClass;
import org.junit.Test;
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

    private static final String JDBC_DRIVER = "org.h2.Driver";
    //private static final String DB_URL = "jdbc:h2:~/test";
    private static final String DB_URL = "jdbc:h2:mem:test;MODE=MySQL";
    //private static final String URL_CONNECTION = "jdbc:h2:~/test;user=youruser;password=yourpassword";
    private static final String URL_CONNECTION = "jdbc:h2:mem:test;user=youruser;password=yourpassword";
    private static final String USER = "youruser";
    private static final String PASS = "yourpassword";
    private static final String SQL_CREATE_USERS =
            "CREATE TABLE IF NOT EXISTS users (\n" +
            "  id INTEGER(11) NOT NULL AUTO_INCREMENT,\n" +
            " login VARCHAR(10) NOT NULL, \n" +
            "  PRIMARY KEY (id));";
    private static final String SQL_SETUP_USERS = "CREATE TABLE `users`\n" +
            "(\n" +
            "    `id`    INT         NOT NULL AUTO_INCREMENT,\n" +
            "    `login` VARCHAR(10) NOT NULL,\n" +
            "    PRIMARY KEY (`id`),\n" +
            "    UNIQUE KEY `login` (`login`)\n" +
            ");";
    private static final String SQL_SETUP_TEAMS = "CREATE TABLE `teams`\n" +
            "(\n" +
            "    `id`   INT         NOT NULL AUTO_INCREMENT,\n" +
            "    `name` VARCHAR(10) NOT NULL,\n" +
            "    PRIMARY KEY (`id`),\n" +
            "    UNIQUE KEY `name` (`name`)\n" +
            ");";
    private static final String SQL_SETUP_UT = "CREATE TABLE `users_teams`\n" +
            "(\n" +
            "    `user_id` INT REFERENCES users (id) ON DELETE CASCADE,\n" +
            "    `team_id` INT REFERENCES teams (id) ON DELETE CASCADE,\n" +
            "    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,\n" +
            "    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,\n" +
            "    UNIQUE (`user_id`, `team_id`)\n" +
            ");";
    private static final String SQL_SETUP_DATA =
            "INSERT INTO `users` VALUES (1, 'ivanov');\n"
            + "INSERT INTO `teams` VALUES (1, 'teamA');"
            + "INSERT INTO `users_teams` VALUES (1, 1);";


    @Spy //actually this annotation is not necessary here
    private static DBManager dbManager;

    @BeforeClass
    public static void beforeTest() throws SQLException, ClassNotFoundException {
        //Class.forName(JDBC_DRIVER);
        try (OutputStream output = new FileOutputStream("app.properties")) {
            Properties prop = new Properties();
            prop.setProperty("connection.url", URL_CONNECTION);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
        dbManager = DBManager.getInstance();
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement s = con.createStatement()) {
            s.executeUpdate(SQL_SETUP_USERS);
            s.executeUpdate(SQL_SETUP_TEAMS);
            s.executeUpdate(SQL_SETUP_UT);
            s.executeUpdate(SQL_SETUP_DATA);
        }
    }

    //The DBManager#insertUser method should modify the ‘id’ field of the User object.
    //The DBManager#findAllUsers method returns a java.util.List object
    @Test
    public void shouldRunDemo() {
        Demo.main(new String[0]);
    }


}