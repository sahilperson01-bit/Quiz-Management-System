import java.sql.*;
public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe",
                "system",
                "admin"
            );
            System.out.println("Connected!");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
