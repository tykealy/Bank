package bank.fx.bank.Controller;

import bank.fx.bank.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import bank.fx.bank.User;

public class signupController extends sceneController {
    @FXML
    private TextField firstNameField, lastNameField, ageField, nationalityField, cardIdField, phoneField, emailField;
    @FXML
    private PasswordField passwordField, comfirmPasswordField;
    private final Random rand = new Random();
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public void toLogin(ActionEvent event) throws IOException {
        super.switchToLoginScene(event);
    }

    public int accNoGenerator() throws SQLException {
        int exist, val = rand.nextInt(90000000) + 10000000;
        Database.grab("SELECT account_number FROM `bank_management`.`account`");
        ResultSet rs = Database.grab("SELECT account_number FROM `bank_management`.`account`");
        while (rs.next()) {
            exist = rs.getInt("account_number");
            if (exist == val) {
                val = rand.nextInt(90000000) + 10000000;
                rs.beforeFirst();
            }
        }
        return val;
    }

    public void createAccount(User user) throws SQLException {
        int id = 0;
        ResultSet rs = Database.get("" +
                "SELECT id FROM users " +
                "where first_name=\"" + user.firstName + "\" and last_name=\"" + user.lastName + "\" " +
                "and email=\"" + user.email + "\"");
        while (rs.next()) {
            id = rs.getInt(1);
        }
        String sqlString = """
                INSERT INTO `bank_management`.`account`
                (`account_number`, `user_id`, `account_name`, `account_type`)
                 VALUES (?, ?, ?, ?);
                                """;
        try {
            PreparedStatement stmt = Database.create(sqlString);
            stmt.setInt(1, accNoGenerator());
            stmt.setInt(2, id);
            stmt.setString(3, user.firstName + " " + user.lastName);
            stmt.setString(4, "Default");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void signUp(ActionEvent event) throws IOException {
        User user = new User();
        user.firstName = firstNameField.getText();
        user.lastName = lastNameField.getText();
        user.age = Integer.parseInt(ageField.getText());
        user.nationality = nationalityField.getText();
        user.idCard = cardIdField.getText();
        user.phone = phoneField.getText();
        user.email = emailField.getText();
        user.password = passwordField.getText();

        alert.setTitle("Pop up");
        alert.setHeaderText("Sign up successfully");
        if (alert.showAndWait().get() == ButtonType.OK) {
            user.create();
            try {
                createAccount(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            super.switchToMainScene(event);
        }
    }
}
