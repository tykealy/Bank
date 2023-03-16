package bank.fx.bank.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import bank.fx.bank.CurrentAccount;
import bank.fx.bank.CurrentUser;
import bank.fx.bank.Database;
import bank.fx.bank.Encryption;
import bank.fx.bank.Main;

public class LoginFormController extends sceneController {
    @FXML
    private TextField accountNumber;
    @FXML
    private Label btnForgot, lblErrors;
    @FXML
    private PasswordField txtPassword;

    @FXML
    void SignIn(ActionEvent event) {
        int account_number = Integer.parseInt(accountNumber.getText());
        String user_password = txtPassword.getText();
        if (accountNumber.getText().isBlank() || txtPassword.getText().isBlank()) {
            lblErrors.setText("Please enter your account number and password!");
        } else {
            try {
                ResultSet rs = Database
                        .get("select user_id from account where account_number =  '" + account_number + "';");

                rs.absolute(1);
                int user_id = rs.getInt(1);
                rs = Database.get(
                        "select * from users where id = " + user_id + ";");
                rs.absolute(1);
                String password = rs.getString("password");
                String password_salt = rs.getString("password_salt");

                if (Encryption.verifyUserPassword(user_password, password, password_salt)) {
                    CurrentUser.setCurrentUser(rs);
                    CurrentAccount.setCurrentAccount(account_number);
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
                    Parent root = loader.load();
                    accountController accountCtrl = loader.getController();
                    accountCtrl.initializeUser();
                    accountCtrl.getCurrentUser();
                    accountCtrl.getCurrentDeposit();
                    super.switchToAccScene(event, root);
                } else {
                    lblErrors.setText("Password does not match!");
                    txtPassword.setText("");
                }
            } catch (SQLException e) {
                lblErrors.setText("Invalid login information.");
            } catch (IOException ignored) {}
        }
    }

    public void switchToRegister(ActionEvent event) throws IOException {
        super.switchToRegisterScene(event);
    }

}
