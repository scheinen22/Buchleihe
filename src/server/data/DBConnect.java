package server.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Stellt die unterste Schicht der Server-Architektur dar. Diese Utility-Klasse
 * ist verantwortlich für den Aufbau einer Verbindung zur MariaDB-Datenbank.
 * Die Verbindungsparameter sind hier zentral definiert.
 */
@SuppressWarnings("java:S2115") // Passwort ist nicht nötig bei der Projektgröße
public class DBConnect {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/Test";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
