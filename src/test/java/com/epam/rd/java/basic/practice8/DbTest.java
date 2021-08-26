package com.epam.rd.java.basic.practice8;

import com.epam.rd.java.basic.practice8.db.DBManager;
import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;
import org.junit.Assert;
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

public class DbTest {

    //private static final String DB_URL = "jdbc:h2:~/test";
    private static final String DB_URL = "jdbc:h2:mem:test;MODE=MySQL";
    //private static final String URL_CONNECTION = "jdbc:h2:~/test;user=youruser;password=yourpassword";
    private static final String URL_CONNECTION = "jdbc:h2:mem:test;user=youruser;password=yourpassword";
    private static final String USER = "youruser";
    private static final String PASS = "yourpassword";
    private static final String SQL_SETUP_USERS =
                    "CREATE TABLE `users`\n" +
                    "(" +
                    "    `id`    INT         NOT NULL AUTO_INCREMENT,\n" +
                    "    `login` VARCHAR(10) NOT NULL,\n" +
                    "    PRIMARY KEY (`id`),\n" +
                    "    UNIQUE KEY `login` (`login`)\n" +
                    ");";
    private static final String SQL_SETUP_TEAMS =
                    "CREATE TABLE `teams`\n" +
                    "(" +
                    "    `id`   INT         NOT NULL AUTO_INCREMENT,\n" +
                    "    `name` VARCHAR(10) NOT NULL,\n" +
                    "    PRIMARY KEY (`id`),\n" +
                    "    UNIQUE KEY `name` (`name`)\n" +
                    ");";
    private static final String SQL_SETUP_UT =
                    "CREATE TABLE `users_teams`\n" +
                    "(" +
                    "    `user_id` INT REFERENCES users (id) ON DELETE CASCADE,\n" +
                    "    `team_id` INT REFERENCES teams (id) ON DELETE CASCADE,\n" +
                    "    UNIQUE (`user_id`, `team_id`)\n" +
                    ");";
    private static final String SQL_SETUP_DATA =
                    "INSERT INTO `users` VALUES (1, 'ivanov');\n" +
                    "INSERT INTO `teams` VALUES (1, 'teamA');" +
                    "INSERT INTO `users_teams` VALUES (1, 1);";


    @Spy //actually this annotation is not necessary here
    private static DBManager dbManager;

    @BeforeClass
    public static void beforeTest() throws SQLException {
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

    @Test
    public void getUserShouldReturnId() {
        Assert.assertEquals(1, dbManager.getUser("ivanov").getId());
    }

    @Test
    public void getTeamShouldReturnId() {
        Assert.assertEquals(1, dbManager.getTeam("teamA").getId());
    }

    @Test
    public void getUserTeamsShouldReturnListOfTeams() {
        Assert.assertEquals(1,
                dbManager.getUserTeams(dbManager.getUser("ivanov")).size());
    }

    @Test
    public void setTeamsShouldReturnTrueIfOk() {
        User ivanov = dbManager.getUser("ivanov");
        Team t1 = dbManager.getTeam("teamB");
        Team t2 = new Team();
        t2.setName("teamS");
        dbManager.insertTeam(t2);
        t2 = dbManager.getTeam(t2.getName());
        Assert.assertTrue(dbManager.setTeamsForUser(ivanov, t1, t2));
        Assert.assertFalse(dbManager.setTeamsForUser(ivanov, t1, t2, new Team()));
    }

    @Test
    public void deleteTeamShouldReturnOkOrFalse() {
        Team t2 = new Team();
        t2.setName("teamB");
        dbManager.insertTeam(t2);
        Assert.assertTrue(dbManager.deleteTeam(t2));
        Assert.assertFalse(dbManager.deleteTeam(t2));
    }

    @Test
    public void updateTeamShouldReturnOkOrFalse() {
        Team t = dbManager.getTeam("teamB");
        t.setName("teamQ");
        Assert.assertTrue(dbManager.updateTeam(t));
        t.setId(136);
        Assert.assertFalse(dbManager.updateTeam(t));
    }

    @Test
    public void actionsWithNullShouldReturnFalse() {
        Assert.assertFalse(dbManager.insertUser(null));
        Assert.assertFalse(dbManager.insertTeam(null));
        Assert.assertFalse(dbManager.insertTeam(null));
        Assert.assertFalse(dbManager.deleteTeam(null));
        Assert.assertFalse(dbManager.updateTeam(null));
        Assert.assertFalse(dbManager.setTeamsForUser(null));
    }

    @Test
    public void getUserTeamsShouldReturnEmptyListForNull() {
        Assert.assertTrue(dbManager.getUserTeams(null).isEmpty());
    }

    @Test
    public void getForNullShouldReturnNull() {
        Assert.assertNull(dbManager.getUser(null));
        Assert.assertNull(dbManager.getTeam(null));
    }

    @Test
    public void secondInsertShouldReturnFalse() {
        User u = new User();
        u.setLogin("John");
        Team t = new Team();
        t.setName("Kyiv");
        dbManager.insertUser(u);
        dbManager.insertTeam(t);
        Assert.assertFalse(dbManager.insertUser(u));
        Assert.assertFalse(dbManager.insertTeam(t));
    }

    @Test
    public void shouldReturnListOfUsers() {
        Assert.assertFalse(dbManager.findAllUsers().isEmpty());
    }

    @Test
    public void shouldReturnListOfTeams() {
        Assert.assertFalse(dbManager.findAllTeams().isEmpty());
    }

    @Test
    public void shouldRunDemo() {
        try {
            Demo.main(new String[0]);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}