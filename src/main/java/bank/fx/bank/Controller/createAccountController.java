package bank.fx.bank.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class createAccountController {
    @FXML
    public Label depositLabel, errorFirstName, errorLastname, errorAge, errorNationality, errorCardId, errorPhone, errorEmail, errorPasswordConfirm, errorPassword;
    @FXML
    public TextField firstNameField, lastNameField, nationalityField, cardIdField, phoneField, emailField;
    @FXML
    public PasswordField passwordField, confirmPasswordField;
    @FXML
    public ChoiceBox<String> accountType;

    @FXML
    public void create(ActionEvent event) {
    }

    @FXML
    public void toAccount(ActionEvent event) {
    }

    @FXML
    public void toWithdraw(ActionEvent event) {
    }

    @FXML
    public void logout(ActionEvent event) {
    }
}
