import java.sql.*;
import gui.LoginWindow;

public class Main {
    public static void main(String[] args) {
        try {
            // Test database connection
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe",
                "system",
                "admin"
            );
            System.out.println("✓ Connected to Oracle DB!");
            con.close();
            
            // Launch the Quiz Management System GUI
            System.out.println("✓ Launching Quiz Management System...");
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Oracle JDBC Driver not found. Ensure ojdbc8.jar is in classpath.");
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            System.err.println("✗ Database connection failed. Check Oracle 11G is running.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("✗ Error launching application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
