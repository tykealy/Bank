package bank.fx.bank.Controller;

import bank.fx.bank.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class mainController extends sceneController{
    @FXML
    public TextField idLabel;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void login(ActionEvent event) throws IOException {
        String id = idLabel.getText();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
        Parent root = loader.load();

        accountController accountCtrl = loader.getController();
        accountCtrl.setUserIdLabel(id);

        super.switchToAccScene(event, root);
    }

    public void exit() {

    }
}