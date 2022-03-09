package com.bafcloud.cloud.Server.Services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthorizationService {

    private final List<Entry> entries;
    private static Connection connection;
    private static PreparedStatement ps;

    public BaseAuthService() {
        entries = new ArrayList<>();
    }

    private Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users_cloud.db");
            ps = connection.prepareStatement("SELECT * FROM users");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entries.add(new Entry(rs.getInt("id"), rs.getString("login"), rs.getString("root"), rs.getString("pass"), rs.getString("space")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long checkUserVerification(String login, String password) {
        // int pass = password.hashCode(); // later
        long space = -1;
        for (Entry a : entries) {
            if (a.login.equals(login) && a.pass.equals(password)) {
                space = Long.parseLong(a.space);
                return space;
            }
        }
        return space;
    }

    public String getRootClient(String login, String password) {
        //int pass = password.hashCode(); // later
        for (Entry a : entries) {
            if (a.login.equals(login) && a.pass.equals(password)) {
                return a.root;
            }
        }
        return "notExist";
    }


    @Override
    public void start() {
        connect();
    }

    @Override
    public void stop() {
        disconnect();
    }

    private static class Entry {
        private final int id;
        private final String login;
        private final String root;
        private final String pass;
        private final String space;


        public Entry(int id, String login, String root, String pass, String space) {
            this.id = id;
            this.login = login;
            this.pass = pass;
            this.root = root;
            this.space = space;
        }
    }
}
