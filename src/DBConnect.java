import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnect {
    private final String DB_URL = "jdbc:mysql://localhost:3306/";
    private String USER = "root";
    private String PASSWORD = "";
    private String DB_NAME;
    private String statement;

    public DBConnect(String DB_NAME, String statement) {
        this.setStatement(statement);
        try {
            Class.forName("com.mysql.jdbc.driver").newInstance();
            Connection con = DriverManager.getConnection(this.getDB_URL(), this.getUSER(), this.getPASSWORD());
            Statement stmt = con.createStatement();
            stmt.execute("USE" + " " + this.getDB_NAME());
            ResultSet rs = stmt.executeQuery(this.getStatement());
            String ergebnis = rs.getString("ergebnis");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public String getUSER() {
        return USER;
    }
    public String getPASSWORD() {
        return PASSWORD;
    }
    public String getDB_NAME() {
        return DB_NAME;
    }
}
