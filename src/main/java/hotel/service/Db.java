package hotel.service;

import java.sql.*;

public class Db {
    private static final String URL ="jdbc:mysql://localhost:3306/hotelsystem?useSSL=false&serverTimezone=UTC";
    private static final String USER = "hotelapp";
    private static final String PASS = "StrongPwd!23";   // exactly as you created

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

