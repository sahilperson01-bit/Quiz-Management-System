public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe",
                "system",
                "admin"
            );
            System.out.println("✓ Connected to Oracle DB!");
            con.close();
            
            // LAUNCH GUI HERE
            System.out.println("✓ Launching Quiz Management System...");
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

