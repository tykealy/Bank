package bank.fx.bank.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class mainController extends sceneController {
    @FXML
    public TextField idLabel;
    public AnchorPane mainAnchor;
    @FXML
    private Label welcomeText;

    public void switchToLogin(ActionEvent event) throws IOException {
        super.switchToLoginScene(event);
    }

    public void switchToRegister(ActionEvent event) throws IOException {
        super.switchToRegisterScene(event);
    }

    public void returnToMenu(ActionEvent event) throws IOException {
        super.switchToMainScene(event);
    }

    public void exit(ActionEvent event) {
        super.exit(event, mainAnchor);
    }
}