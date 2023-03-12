package bank.fx.bank.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NavigationDashbaord  implements Initializable  {
    @FXML
    private Button Deposit;

    @FXML
    private Button account;

    @FXML
    private Button logoutBtn;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button transection;

    @FXML
    private Button withdraw;


   
    @FXML
    void HandAction1(ActionEvent event) throws IOException {
     AnchorPane view = new FXMLLoader().load(getClass().getResource("accountScene.fxml"));
        mainPane.setCenter(view);


    }

    @FXML
    void HandAction2(ActionEvent event) throws IOException {




    }

    @FXML
    void HandAction3(ActionEvent event) {



    }

    @FXML
    void HandAction5(ActionEvent event) {

    }



    public void logoutBtn(ActionEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}


