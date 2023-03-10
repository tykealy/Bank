package bank.fx.bank.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

import bank.fx.bank.User;

public class signupController extends sceneController {
    @FXML
    private TextField firstNameField, lastNameField, ageField, nationalityField, cardIdField, phoneField, emailField;
    @FXML
    private PasswordField passwordField, comfirmPasswordField;

    public void toLogin(ActionEvent event) throws IOException {
        super.switchToLoginScene(event);
    }

    public void signUp(ActionEvent event) throws IOException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String nationality = nationalityField.getText();
        String cardId = cardIdField.getText();
        String phone = phoneField.getText();
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
                        "\nPassword: " + password);
        User.create(firstName, lastName, age, nationality, cardId, phone, email, password);
        super.switchToMainScene(event);
    }
}
