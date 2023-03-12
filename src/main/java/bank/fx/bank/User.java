package bank.fx.bank;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {
    public String firstName, lastName, nationality, idCard, phone, email, password, password_salt;
    public int id, age;

    public void create() {
        String sqlString = """
                INSERT INTO `bank_management`.`users`
                (`first_name`, `last_name`, `age`, `nationality`, `id_card`, `phone`, `email`, `password`,`password_salt`)
                 VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                                """;
        String salt = Encryption.getSaltvalue(30);
        try {
            PreparedStatement stmt = Database.create(sqlString);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, age);
            stmt.setString(4, nationality);
            stmt.setString(5, idCard);
            stmt.setString(6, phone);
            stmt.setString(7, email);
            stmt.setString(8, Encryption.encrypt(password, salt));
            stmt.setString(9, salt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
