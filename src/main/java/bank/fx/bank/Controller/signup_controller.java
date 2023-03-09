package bank.fx.bank.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class signup_controller {
    @FXML
    private TextField firstNameField,lastNameField,ageField,nationalityField,cardIdField,phoneField,emailField;
    @FXML
    private PasswordField passwordField,comfirmPasswordField;

    public void signUp(ActionEvent event){
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String nationality = nationalityField.getText();
        String cardId = cardIdField.getText();
        String phone =  phoneField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        System.out.println(
                "Firstname: " + firstName +
                "\nLastname: " + lastName +
                "\nAge: " + age +
                "\nNationality: " + nationality +
                "\nCard ID: " + cardId +
                "\nPhone No.: " + phone +
                "\nEmail: " + email +
                "\nPassword: " +password
        );
    }
}
