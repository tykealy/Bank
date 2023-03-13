package bank.fx.bank.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;

import bank.fx.bank.Account;
import bank.fx.bank.CurrentUser;
import bank.fx.bank.Database;
import bank.fx.bank.Encryption;
import bank.fx.bank.Main;

public class LoginFormController extends sceneController {

    @FXML
    private TextField accountNumber;

    @FXML
    private Label btnForgot;

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnSignup;

    @FXML
    private Label lblErrors;

    @FXML
    private PasswordField txtPassword;

    @FXML
    void SignIn(ActionEvent event) {
        int account_number = Integer.parseInt(accountNumber.getText());
        String user_password = txtPassword.getText();

        if (accountNumber.getText().isBlank() == false && txtPassword.getText().isBlank() == false) {
            try {
                ResultSet rs = Database
                        .get("select user_id from account where account_number =  " + account_number + ";");

                rs.absolute(1);
                int user_id = rs.getInt(1);
                System.out.println(user_id);
                rs = Database.get(
                        "select * from users where id = " + user_id + ";");
                rs.absolute(1);
                String password = rs.getString("password");
                String password_salt = rs.getString("password_salt");
                System.out.println(password);
                System.out.println(password_salt);

                if (Encryption.verifyUserPassword(user_password, password, password_salt)) {
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
                    Parent root = loader.load();
                    CurrentUser.setCurrentUser(rs);
                    // accountController accountCtrl = loader.getController();
                    // accountCtrl.setUserIdLabel(String.valueOf(user_id));
                    super.switchToAccScene(event, root);
                }
            } catch (Exception e) {
                lblErrors.setText("Invalid login infomation.");
            }
        } else {
            lblErrors.setText("Please enter your account number and password!");
        }

    }

    public void btnSignup(ActionEvent e) {

        Stage stage = (Stage) btnSignup.getScene().getWindow();
        stage.close();
    }

    public void invalidateLogin() {

    }

    public void switchToRegister(ActionEvent event) throws IOException {
        super.switchToRegisterScene(event);
    }

}
