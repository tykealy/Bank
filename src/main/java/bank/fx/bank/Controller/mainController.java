package bank.fx.bank.Controller;

import bank.fx.bank.Database;
import bank.fx.bank.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class mainController extends sceneController{
    @FXML
    public TextField idLabel;
    @FXML
    public VBox vBox;
    @FXML
    private Label welcomeText;

    Stage stage;

    public void login(ActionEvent event) throws IOException, SQLException {
        String id = idLabel.getText();
        //
        try {
            int checkUser=Integer.parseInt(id);
            ResultSet rs = Database.get("select count(user_id) from account where user_id="+checkUser);
            while (rs.next()) {
                if (rs.getInt(1) == 0) {
                    idLabel.setText("");
                    welcomeText.setTextFill(Color.RED);
                    welcomeText.setText("User does not exist");
                } else {
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
                    Parent root = loader.load();

                    accountController accountCtrl = loader.getController();
                    accountCtrl.setUserIdLabel(id);

                    super.switchToAccScene(event, root);
                }
            }
        } catch (NumberFormatException ne) {
            idLabel.setText("");
            welcomeText.setTextFill(Color.RED);
            welcomeText.setText("Invalid Input");
        }
        // this problem of users do not exist should be fixed in the login
    }

    public void exit(ActionEvent event) {
        stage = (Stage) vBox.getScene().getWindow();
        stage.close();
    }
}