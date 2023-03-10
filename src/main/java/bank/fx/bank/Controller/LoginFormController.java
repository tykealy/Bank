package bank.fx.bank.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginFormController {

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
    void SignIn(ActionEvent event){

        if (accountNumber.getText().isBlank() == false && txtPassword.getText().isBlank() == false){
            lblErrors.setText("You try to login!");
         //   invalidateLogin();
        }
        else {
            lblErrors.setText("Please enter your account number and password!");
        }


    }

    public void btnSignup(ActionEvent e) {

        Stage stage = (Stage) btnSignup.getScene().getWindow();
        stage.close();
    }

    public void invalidateLogin(){

    }
}







