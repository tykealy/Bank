package bank.fx.bank;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {
    public static void create(String firstname, String lastname, int age, String nationality, String idCard,
            String phone, String email, String password) {
        String sqlString = """
                INSERT INTO `bank_management`.`users`
                (`first_name`, `last_name`, `age`, `nationality`, `id_card`, `phone`, `email`, `password`)
                 VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                                """;
        try {
            PreparedStatement stmt = Database.create(sqlString);
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.setInt(3, age);
            stmt.setString(4, nationality);
            stmt.setString(5, idCard);
            stmt.setString(6, phone);
            stmt.setString(7, email);
            stmt.setString(8, password);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
