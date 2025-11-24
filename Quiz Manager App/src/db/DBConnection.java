package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "system";
    private static final String PASS = "admin";

    private static Connection conn = null;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            // Driver auto-registered if ojdbc is on classpath; Optionally: Class.forName(...)
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✔ Connected to Oracle DB");
        }
        return conn;
    }
}
