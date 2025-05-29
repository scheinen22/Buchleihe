package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("java:S2115") // Passwort ist nicht nötig bei der Projektgröße
public class DBConnect {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/Test";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final Logger LOGGER = Logger.getLogger(DBConnect.class.getName());

    public DBConnect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection ignored = DriverManager.getConnection(this.getDBURL(), this.getUser(), this.getPassword())) {
                LOGGER.info("Connected to the database successfully");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Verbinden zur Datenbank", e);
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
