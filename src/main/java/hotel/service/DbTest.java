
package hotel.service;
import java.sql.*;

public class DbTest {
    public static void main(String[] args) throws Exception {
        try (Connection c = Db.get();
             Statement st = c.createStatement()) {

            try (ResultSet r = st.executeQuery("SELECT DATABASE()")) {
                r.next();
                System.out.println("Connected to DB: " + r.getString(1));
            }

            try (ResultSet rs = st.executeQuery("SELECT id,username,email FROM users")) {
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    System.out.println("User: " + rs.getInt("id") + " / " +
                                       rs.getString("username") + " / " +
                                       rs.getString("email"));
                }
                if (!any) System.out.println("No users found.");
            }
        }
    }
}

