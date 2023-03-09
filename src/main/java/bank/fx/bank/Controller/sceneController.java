package bank.fx.bank.Controller;

import bank.fx.bank.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class sceneController {
    private Parent root;
    private Scene scene;
    private Stage stage;
    public void switchToMainScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Main.class.getResource("mainScene.fxml"));
        stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
        scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAccScene(ActionEvent event, Parent root) throws IOException {
//        root = FXMLLoader.load(Main.class.getResource("accountScene.fxml"));
        stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
        scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.show();
    }
}
