public class Main {
    public static void main(String[] args) {
        DBConnect dbConnect = new DBConnect("Test", "Test");
        System.out.println(dbConnect.getStatement());
    }
}
