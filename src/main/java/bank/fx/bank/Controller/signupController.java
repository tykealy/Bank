package bank.fx.bank.Controller;

import bank.fx.bank.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import bank.fx.bank.User;
import javafx.scene.paint.Color;

public class signupController extends sceneController {
    @FXML
    public Label errorFirstName, errorLastname, errorAge, errorNationality, errorCardId, errorPhone, errorEmail, errorPassword, errorPasswordConfirm;
    @FXML
    private TextField firstNameField, lastNameField, ageField, nationalityField, cardIdField, phoneField, emailField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
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
        if (rs.next()) {
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

    public void check(boolean prompt, Label errorField, String keyword) {
        if (prompt) {
            errorField.setTextFill(Color.RED);
            errorField.setText("Please fill in your "+ keyword);
        }
    }

    public void signUp(ActionEvent event) throws IOException {
        User user = new User();
        errorFirstName.setText(""); errorLastname.setText(""); errorAge.setText(""); errorNationality.setText("");
        errorCardId.setText(""); errorPhone.setText(""); errorEmail.setText(""); errorPassword.setText(""); errorPasswordConfirm.setText("");
        try {
            if (Integer.parseInt(ageField.getText()) < 18) {
                errorAge.setTextFill(Color.RED);
                if (ageField.getText().isBlank()) {
                    errorAge.setText("Please fill in your age");
                } else {
                    errorAge.setText("You must be over 18");
                }
                ageField.setText("");
            }
        } catch (NumberFormatException ignored) {}
        if (passwordField.getText().length() < 8) {
            errorPassword.setTextFill(Color.RED);
            if (passwordField.getText().isBlank()) {
                check(passwordField.getText().isBlank(), errorPassword, "password");
            } else {
                errorPassword.setText("Password must be 8 characters or higher");
            }
            passwordField.setText("");
        }
        if (confirmPasswordField.getText().length() < 8) {
            errorPasswordConfirm.setTextFill(Color.RED);
            if (confirmPasswordField.getText().isBlank()) {
                check(confirmPasswordField.getText().isBlank(), errorPasswordConfirm, "confirm password");
            } else {
                errorPasswordConfirm.setText("Password must be 8 characters or higher");
            }
            confirmPasswordField.setText("");
        }
        if (!confirmPasswordField.getText().equals(passwordField.getText())) {
            errorPasswordConfirm.setTextFill(Color.RED);
            errorPasswordConfirm.setText("Password does not match!");
            confirmPasswordField.setText("");
        }
        if (
            firstNameField.getText().isBlank() || lastNameField.getText().isBlank() || ageField.getText().isBlank() ||
            nationalityField.getText().isBlank() || cardIdField.getText().isBlank() || phoneField.getText().isBlank() ||
            emailField.getText().isBlank() || passwordField.getText().isBlank() || confirmPasswordField.getText().isBlank()
        ) {
            check(firstNameField.getText().isBlank(), errorFirstName, "first name");
            check(lastNameField.getText().isBlank(), errorLastname, "last name");
            check(nationalityField.getText().isBlank(), errorNationality, "nationality");
            check(cardIdField.getText().isBlank(), errorCardId, "card Id");
            check(phoneField.getText().isBlank(), errorPhone, "phone number");
            check(emailField.getText().isBlank(), errorEmail, "email");
        } else {
            try {
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
                    createAccount(user);
                    super.switchToMainScene(event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
