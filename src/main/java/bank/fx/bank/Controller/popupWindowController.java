package bank.fx.bank.Controller;

import bank.fx.bank.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class popupWindowController {
    @FXML
    public Label accountName, accountNumber;

    public void popupCard(int accNo, String accName) throws IOException {
        Stage popupWindow = new Stage();
        Parent root = FXMLLoader.load(Main.class.getResource("CardScene.fxml"));
        Scene scene = new Scene(root);
        popupWindow.setScene(scene);
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.show();
        accountNumber.setText(String.valueOf(accNo));
        accountName.setText(accName);
    }
}
