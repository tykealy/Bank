package bank.fx.bank.Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class popupWindowController {
    @FXML
    public Label accountName, accountNumber;

    public void popupCard(Parent root) throws IOException {
        Stage popupWindow = new Stage();
        Scene scene = new Scene(root);
        popupWindow.setScene(scene);
        popupWindow.initModality(Modality.WINDOW_MODAL);
        popupWindow.show();
    }

    public void setCardInfo(int accNo, String accName) {
        accountNumber.setText(String.valueOf(accNo));
        accountName.setText(accName);
    }
}
