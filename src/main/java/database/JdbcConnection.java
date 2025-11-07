package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {

    // ✅ Database credentials (you can later move these to a config file)
    private static final String URL = "jdbc:mysql://localhost:3306/CustomerFeedbackSystem";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    // ✅ Step 1: Create a private static volatile instance
    private static volatile JdbcConnection instance;

    // ✅ Step 2: Private constructor to prevent external instantiation
    private JdbcConnection() {
        try {
            // Load JDBC driver (some environments still need this explicitly)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found. Please check your classpath.");
        }
    }

    // ✅ Step 3: Public method to provide global access to the single instance
    public static JdbcConnection getInstance() {
        if (instance == null) { // First check (no locking)
            synchronized (JdbcConnection.class) { // Lock
                if (instance == null) { // Second check (with lock)
                    instance = new JdbcConnection();
                }
            }
        }
        return instance;
    }

    // ✅ Step 4: Method to provide connection each time it's requested
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
