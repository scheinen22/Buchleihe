package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Unterste Schicht in der Architektur. In der Klasse werden nötige Parameter
 * um eine Connection mit der Datenbank aufzubauen festgelegt.
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
