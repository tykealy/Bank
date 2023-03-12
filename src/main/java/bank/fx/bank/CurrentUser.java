package bank.fx.bank;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrentUser extends User {
    public static String firstName, lastName, nationality, idCard, phone, email, password, password_salt;
    public static int id, age;

    public static void setCurrentUser(ResultSet rs) throws SQLException {
        firstName = rs.getString("first_name");
        lastName = rs.getString("last_name");
        phone = rs.getString("phone");
        email = rs.getString("email");
        id = rs.getInt("id");
        age = rs.getInt("age");
    }
}
