package northwindgui.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // First try to get credentials from environment variables
            String protocol = System.getenv("dvdrental_DB_PROTO");
            String host = System.getenv("dvdrental_DB_HOST");
            String port = System.getenv("dvdrental_DB_PORT");
            String dbName = System.getenv("dvdrental_DB_NAME");
            String user = System.getenv("dvdrental_DB_USERNAME");
            String password = System.getenv("dvdrental_DB_PASSWORD");
            
            if (protocol != null && host != null && port != null && 
                dbName != null && user != null && password != null) {
                // Use environment variables if all are set
                String url = String.format("%s://%s:%s/%s", protocol, host, port, dbName);
                connection = DriverManager.getConnection(url, user, password);
            } else {
                // Fall back to hardcoded values if environment variables aren't set
                String url = "jdbc:mysql://localhost:8080/u23542773_northwind";
                user = "root";
                password = "Malichi1x";
                
                connection = DriverManager.getConnection(url, user, password);
            }
        }
        return connection;
    }
    
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
