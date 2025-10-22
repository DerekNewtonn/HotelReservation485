package hotel.service;

import java.sql.*;

public class Db {
    private static final String URL  = "jdbc:mysql://localhost:3306/hotel_system?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";            // change if needed
    private static final String PASS = "your_password";   // change

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}