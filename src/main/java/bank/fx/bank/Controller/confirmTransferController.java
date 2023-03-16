package bank.fx.bank.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class confirmTransferController extends sceneController implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Button confirmBtn;
    @FXML
    Label amountConfirm, accountNoConfirm, receiverConfirm;
    boolean check;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmBtn.setOnAction(this::confirm);
    }

    public void popupConfirmTransfer(Parent root, double amount, int accNo, String receiverName) throws IOException {
        Stage popupWindow = new Stage();
        Scene scene = new Scene(root);
        popupWindow.setScene(scene);
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        amountConfirm.setText("$"+ amount);
        accountNoConfirm.setText(String.valueOf(accNo));
        receiverConfirm.setText(receiverName);
        popupWindow.showAndWait();
    }

    public void setTransferInfo(double amount, int accNo, String receiverName){
        amountConfirm.setText(String.valueOf(amount));
        accountNoConfirm.setText(String.valueOf(accNo));
        receiverConfirm.setText(receiverName);
    }

    public void cancel(ActionEvent event) {
        super.exit(event, anchorPane);
    }

    public void confirm(ActionEvent event) {
        check = true;
        super.exit(event, anchorPane);
    }

    public boolean getCheck() {
        return check;
    }
}
