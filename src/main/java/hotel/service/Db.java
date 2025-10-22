package hotel.service;

import java.sql.*;

public class Db {
    private static final String URL ="jdbc:mysql://localhost:3306/hotelsystem?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER = "hotelapp";        // from your screenshot
    private static final String PASS = "StrongPwd!23";    // exactly what you set

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
