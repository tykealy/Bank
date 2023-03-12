package bank.fx.bank.Controller;

import bank.fx.bank.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class sceneController {
    private Parent root;
    private Scene scene;
    private Stage stage;

    public void switchToMainScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Main.class.getResource("mainScene.fxml"));
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAccScene(ActionEvent event, Parent root) throws IOException {
        // root = FXMLLoader.load(Main.class.getResource("testAccountScene.fxml"));
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLoginScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Main.class.getResource("loginScene.fxml"));
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRegisterScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Main.class.getResource("signupScene.fxml"));
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void exit(ActionEvent event, Pane pane) {
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
    }

}
