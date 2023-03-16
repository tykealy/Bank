package bank.fx.bank.Controller;

import bank.fx.bank.CurrentAccount;
import bank.fx.bank.CurrentUser;
import bank.fx.bank.Database;
import bank.fx.bank.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class depositController extends sceneController {
    @FXML
    public TextField depositAmount;
    @FXML
    public Label depositLabel, currentAccNo;
    private double result = 0;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    int cAccNo;

    public void setCurrentAccount(int a) {
        cAccNo = a;
        currentAccNo.setText(String.valueOf(cAccNo));
    }

    @FXML
    protected void deposit() throws SQLException {
        try {
            double amt = Double.parseDouble(depositAmount.getText());
            if (amt == 0) {
                depositAmount.setText("");
                depositLabel.setText("");
            } else {
                ResultSet rs = Database.get("select balance from account where account_number=" + cAccNo);
                while (rs.next()) {
                    double balance = rs.getDouble(1);
                    result = balance + amt;
                }
                PreparedStatement ps = Database.set("update account set balance=? where account_number=" + cAccNo);
                ps.setDouble(1, result);
                alert.setHeaderText("Deposit Amount: $" + amt);

                if (alert.showAndWait().get() == ButtonType.OK) {
                    ps.executeUpdate();
                    depositLabel.setTextFill(Color.GREEN);
                    depositLabel.setText("Deposit Successfully");
                    depositAmount.setText("");
                    // add to deposit table
                    ps = Database.set("insert into deposit(user_id, account_no, amount, date, time) " +
                            "values (" + CurrentUser.id + ", " + cAccNo + ", " + amt + ", \"" + LocalDate.now()
                            + "\", \"" +
                            LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + "\")");
                    ps.executeUpdate();
                }
            }
        } catch (NumberFormatException ne) {
            depositLabel.setTextFill(Color.RED);
            depositLabel.setText("Invalid amount");
            depositAmount.setText("");
        }
    }

    public void toAccount(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
        Parent root = loader.load();
        accountController accountCtrl = loader.getController();
        accountCtrl.setCurrentUser(cAccNo);
        accountCtrl.getCurrentUser();
        accountCtrl.getCurrentDeposit();
        super.switchToAccScene(event, root);
    }

    public void toWithdraw(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("withdrawScene.fxml"));
        Parent root = loader.load();
        withdrawController withdrawCtrl = loader.getController();
        withdrawCtrl.setCurrentAccount(cAccNo);
        super.switchToWithdrawScene(event, root);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        super.switchToLoginScene(event);
    }

    public void toTransfer(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("transferScene.fxml"));
        Parent root = loader.load();
        transferController transferCtrl = loader.getController();
        transferCtrl.setCurrentAccount(CurrentAccount.account_number);
        transferCtrl.setAccountSwitch();
        super.switchToTransferScene(event, root);
    }

    public void toProfile(ActionEvent event) {
    }
}
