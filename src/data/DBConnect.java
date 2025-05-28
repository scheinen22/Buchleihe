package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import view.View;

public class DBConnect {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public DBConnect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(this.getDBURL(), this.getUser(), this.getPassword())) {
                View.ausgabe("Connected to the database successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            View.ausgabe("Failed to connect to database");
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
    public String getDBURL() {
        return DB_URL;
    }
    public String getUser() {
        return USER;
    }
    public String getPassword() {
        return PASSWORD;
    }
}
