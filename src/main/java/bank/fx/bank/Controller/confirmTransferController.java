package bank.fx.bank.Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class confirmTransferController {
    @FXML
    Label amountConfirm,accountNoConfirm,receiverConfirm;

    public void popupConfirmTransfer(Parent root) throws IOException {
        Stage popupWindow = new Stage();
        Scene scene = new Scene(root);
        popupWindow.setScene(scene);
        popupWindow.initModality(Modality.WINDOW_MODAL);
        popupWindow.show();
    }
    public void setTransferInfo(double amount, String accNo, String receiverName){
        amountConfirm.setText(String.valueOf(amount));
        accountNoConfirm.setText(accNo);
        receiverConfirm.setText(receiverName);
    }
}
