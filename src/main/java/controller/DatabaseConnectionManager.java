package controller;
import java.sql.*;
import java.io.FileInputStream;
import java.util.Properties;

public class DatabaseConnectionManager {
    private static String url, user, pass;

    static {
        Properties dbCredentials = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            dbCredentials.load(fis);
            url = dbCredentials.getProperty("DB_URL");
            user = dbCredentials.getProperty("DB_USER");
            pass = dbCredentials.getProperty("DB_PASSWORD");
        } catch (Exception e) {
            System.err.println("env file not found or incomplete"); }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}
