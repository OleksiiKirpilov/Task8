package com.epam.rd.java.basic.practice8.db;

import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class DBManager {

    private static DBManager dbManager;
    private static Connection connection;

    private static final String SQL_ADD_USER = "INSERT INTO users VALUES (DEFAULT ,?)";
    private static final String SQL_ADD_TEAM = "INSERT INTO teams VALUES (DEFAULT ,?)";
    private static final String SQL_GET_ALL_USERS = "SELECT * FROM users";
    private static final String SQL_GET_ALL_TEAMS = "SELECT * FROM teams";
    private static final String SQL_GET_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
    private static final String SQL_GET_TEAM_BY_NAME = "SELECT * FROM teams WHERE name=?";
    private static final String SQL_GET_TEAMS_BY_USER_ID =
            "SELECT t.id, t.name FROM users_teams ut JOIN teams t ON ut.team_id = t.id\n"
                    + "WHERE ut.user_id = ?";
    private static final String SQL_ADD_USER_TO_TEAM = "INSERT INTO users_teams VALUES (?, ?)";
    private static final String SQL_DELETE_TEAM = "DELETE FROM teams WHERE name=?";
    private static final String SQL_UPDATE_TEAM_NAME = "UPDATE teams SET name=? WHERE id=?";


    public static synchronized Connection getConnection() {
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
    }

    public static synchronized DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
            connection = getConnection();
        }
        return dbManager;
    }

    public boolean insertUser(User user) {
        ResultSet rsId = null;
        try (PreparedStatement ps = connection.prepareStatement(SQL_ADD_USER,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getLogin());
            if (ps.executeUpdate() != 1) {
                return false;
            }
            rsId = ps.getGeneratedKeys();
            if (rsId.next()) {
                user.setId(rsId.getInt(1));
            }
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
            return false;
        } finally {
            close(rsId);
        }
        return true;
    }

    public boolean insertTeam(Team team) {
        ResultSet rsId = null;
        try (PreparedStatement ps = connection.prepareStatement(SQL_ADD_TEAM,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, team.getName());
            if (ps.executeUpdate() != 1) {
                return false;
            }
            rsId = ps.getGeneratedKeys();
            if (rsId.next()) {
                team.setId(rsId.getInt(1));
            }
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
            return false;
        } finally {
            close(rsId);
        }
        return true;
    }

    public User getUser(String login) {
        ResultSet rs = null;
        User user = null;
        try (PreparedStatement st = connection.prepareStatement(SQL_GET_USER_BY_LOGIN)) {
            st.setString(1, login);
            rs = st.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("login"));
            }
        } catch (SQLException ex) {
            Logger.getGlobal().severe(ex.getMessage());
        } finally {
            close(rs);
        }
        return user;
    }

    public Team getTeam(String name) {
        ResultSet rs = null;
        Team team = null;
        try (PreparedStatement st = connection.prepareStatement(SQL_GET_TEAM_BY_NAME)) {
            st.setString(1, name);
            rs = st.executeQuery();
            if (rs.next()) {
                team = new Team();
                team.setId(rs.getInt("id"));
                team.setName(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getGlobal().severe(ex.getMessage());
        } finally {
            close(rs);
        }
        return team;
    }

    public List<Team> getUserTeams(User user) {
        ResultSet rs = null;
        List<Team> teams = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SQL_GET_TEAMS_BY_USER_ID)) {
            st.setInt(1, user.getId());
            rs = st.executeQuery();
            while (rs.next()) {
                Team team = new Team();
                team.setId(rs.getInt(1));
                team.setName(rs.getString(2));
                teams.add(team);
            }
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
            return Collections.emptyList();
        } finally {
            close(rs);
        }
        return teams;
    }

    public boolean setTeamsForUser(User user, Team... teams) {
        try (PreparedStatement st = connection.prepareStatement(SQL_ADD_USER_TO_TEAM)) {
            setAutoCommit(false);
            for (Team t : teams) {
                st.setInt(1, user.getId());
                st.setInt(2, t.getId());
                st.addBatch();
            }
            int[] usersTeams = st.executeBatch();
            for (int i : usersTeams) {
                if (i != 1) {
                    return false;
                }
            }
            connection.commit();
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                Logger.getGlobal().severe(e.getMessage());
            }
            Logger.getGlobal().severe(ex.getMessage());
        } finally {
            setAutoCommit(true);
        }
        return false;
    }

    public boolean deleteTeam(Team team) {
        try (PreparedStatement st = connection.prepareStatement(SQL_DELETE_TEAM)) {
            st.setString(1, team.getName());
            if (st.executeUpdate() != 1) {
                return false;
            }
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateTeam(Team team) {
        try (PreparedStatement st = connection.prepareStatement(SQL_UPDATE_TEAM_NAME)) {
            st.setString(1, team.getName());
            st.setInt(2, team.getId());
            if (st.executeUpdate() != 1) {
                return false;
            }
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
            return false;
        }
        return true;
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(SQL_GET_ALL_USERS)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setLogin(rs.getString(2));
                users.add(user);
            }
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
            return Collections.emptyList();
        }
        return users;
    }

    public List<Team> findAllTeams() {
        List<Team> teams = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(SQL_GET_ALL_TEAMS)) {
            while (rs.next()) {
                Team team = new Team();
                teams.add(team);
                team.setId(rs.getInt(1));
                team.setName(rs.getString(2));
            }
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
            return Collections.emptyList();
        }
        return teams;
    }

    private static void close(AutoCloseable ac) {
        try {
            ac.close();
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    private static void setAutoCommit(boolean value) {
        try {
            connection.setAutoCommit(value);
        } catch (SQLException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }
}
