package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private String dbname;
    private String statement;

    public DBConnect(String dbname, String statement) {
        this.setStatement(statement);
        this.setDname(dbname);
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection con = DriverManager.getConnection(this.getDB_URL(), this.getUser(), this.getPassword());
            fuehreStatementAus(con);
            System.out.println("Connected to the database successfully");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database");
        }
    }
    public DBConnect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(this.getDB_URL(), this.getUser(), this.getPassword())) {
                System.out.println("Connected to the database successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database");
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
    public String getStatement() {
        return statement;
    }
    public void setStatement(String statement) {
        this.statement = statement;
    }
    public String getDB_URL() {
        return DB_URL;
    }
    public String getUser() {
        return USER;
    }
    public String getPassword() {
        return PASSWORD;
    }
    public String getDbname() {
        return dbname;
    }
    public void setDname(String dname) {
        this.dbname = dname;
    }
    private void fuehreStatementAus(Connection con) {
        try (con; Statement stmt = con.createStatement()) {
            stmt.execute("USE" + " " + this.getDbname());
            String ergebnis = "";
            ResultSet rs = stmt.executeQuery(this.getStatement());
            while (rs.next()) {
                ergebnis = ergebnis.concat(rs.getString(1) + ",");
            }
            System.out.println(ergebnis);
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
