package bank.fx.bank.Controller;

import bank.fx.bank.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static bank.fx.bank.Account.accNoGenerator;

public class createAccountController extends sceneController implements Initializable {
    @FXML
    public Label depositLabel, errorFirstName, errorLastname, errorAge, errorNationality, errorCardId, errorPhone, errorEmail, errorPasswordConfirm, errorPassword;
    @FXML
    public TextField firstNameField, lastNameField, ageField, nationalityField, cardIdField, phoneField, emailField;
    @FXML
    public PasswordField passwordField, confirmPasswordField;
    @FXML
    public ChoiceBox<String> accountType;
    String[] accType = {"Default", "Saving", "Salary"};
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    int id, accNo;

    public void setUserId(int id){
        this.id = id;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountType.getItems().addAll(accType);
        accountType.setValue("Default");
    }

    public void check(boolean prompt, Label errorField, String keyword) {
        if (prompt) {
            errorField.setTextFill(Color.RED);
            errorField.setText("Please fill in your " + keyword);
        }
    }

    public void createAccount(User user) throws SQLException {
        String sqlString = """
                INSERT INTO `bank_management`.`account`
                (`account_number`, `user_id`, `account_name`, `account_type`)
                 VALUES (?, ?, ?, ?);
                                """;
        try {
            PreparedStatement stmt = Database.create(sqlString);
            accNo = accNoGenerator();
            String accName = user.firstName + " " + user.lastName;
            stmt.setInt(1, accNo);
            stmt.setInt(2, id);
            stmt.setString(3, accName);
            stmt.setString(4, accountType.getValue());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void create(ActionEvent event) {
        User user = new User();
        errorFirstName.setText("");
        errorLastname.setText("");
        errorAge.setText("");
        errorNationality.setText("");
        errorCardId.setText("");
        errorPhone.setText("");
        errorEmail.setText("");
        errorPassword.setText("");
        errorPasswordConfirm.setText("");
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
        if (firstNameField.getText().isBlank() || lastNameField.getText().isBlank() || ageField.getText().isBlank() ||
                nationalityField.getText().isBlank() || cardIdField.getText().isBlank()
                || phoneField.getText().isBlank() ||
                emailField.getText().isBlank() || passwordField.getText().isBlank()
                || confirmPasswordField.getText().isBlank()) {
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

                alert.setTitle("Account Creation");
                alert.setHeaderText("Account create successfully");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    user.create();
                    createAccount(user);
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
                    Parent root = loader.load();
                    accountController accCtrl = loader.getController();
                    accCtrl.getCurrentUser();
                    accCtrl.getCurrentDeposit();
                    super.switchToAccScene(event, root);
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void toAccount(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
        Parent root = loader.load();
        accountController accountCtrl = loader.getController();
        accountCtrl.getCurrentUser();
        accountCtrl.getCurrentDeposit();
        super.switchToAccScene(event,root);
    }

    @FXML
    public void toDeposit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("depositScene.fxml"));
        Parent root = loader.load();
        depositController depositCtrl = loader.getController();
        depositCtrl.setCurrentAccount(CurrentAccount.account_number);
        super.switchToDepositScene(event, root);
    }

    @FXML
    public void toWithdraw(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("withdrawScene.fxml"));
        Parent root = loader.load();
        withdrawController withdrawCtrl = loader.getController();
        withdrawCtrl.setCurrentAccount(CurrentAccount.account_number);
        super.switchToWithdrawScene(event, root);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        super.switchToLoginScene(event);
    }
}
