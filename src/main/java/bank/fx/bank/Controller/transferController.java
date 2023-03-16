package bank.fx.bank.Controller;

import bank.fx.bank.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class transferController extends sceneController implements Initializable {
    @FXML
    TextField transferAmount, receiverNo, transferMessage;
    @FXML
    Label transferLabel, receiverLabel;
    @FXML
    ChoiceBox <String>  accountTypeChoice;
    ResultSet rs; PreparedStatement ps;
    int cAccNo;
    private double balance = 0;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

    public void setCurrentAccount(int a) {
        cAccNo = a;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    public void transfer() throws SQLException {
        String username;
        try {
            if (transferAmount.getText().equals("")) {
                transferLabel.setTextFill(Color.RED);
                transferLabel.setText("Specify an amount");
            } else if (receiverNo.getText().equals("")) {
                receiverLabel.setTextFill(Color.RED);
                receiverLabel.setText("Specify the receiver");
            } else {
                double amt = Double.parseDouble(transferAmount.getText());
                int receiver = Integer.parseInt(receiverNo.getText());
                rs = Database.get("select count(account_number), account_name from account where account_number=" + receiver);
                while (rs.next()) {
                    if (rs.getInt(1) == 0) {
                        receiverLabel.setTextFill(Color.RED);
                        receiverLabel.setText("User does not exist");
                        receiverNo.setText("");
                    } else if (receiver == cAccNo) {
                        receiverLabel.setTextFill(Color.RED);
                        receiverLabel.setText("Current Account");
                        receiverNo.setText("");
                    } else {
                        /* remove balance */
                        username = rs.getString(2);
                        rs = Database.get("select balance from account where account_number=" + cAccNo);
                        while (rs.next()) {
                            balance = rs.getDouble(1);
                        }
                        if (amt <= balance) {
                            double result = balance - amt;
                            ps = Database.set("update account set balance=? where account_number=" + cAccNo);
                            ps.setDouble(1, result);
                            alert.setHeaderText("Transfer Amount: $" + amt + "\nReceiver Name: " + username);

                            if (alert.showAndWait().get() == ButtonType.OK) {
                                ps.executeUpdate();

                                /* add to receiver */
                                rs = Database.get("select balance from account where account_number=" + receiver);
                                while (rs.next()) {
                                    balance = rs.getDouble(1);
                                    result = balance + amt;
                                }
                                ps = Database.set("update account set balance=? where account_number=" + receiver);
                                ps.setDouble(1, result);
                                ps.executeUpdate();
                                transferLabel.setTextFill(Color.GREEN);
                                transferLabel.setText("Transfer Successfully");
                                transferAmount.setText("");
                                receiverNo.setText("");
                                receiverLabel.setText("");
                                /* add to transfer table */
                                ps = Database.set("insert into transfer(user_id, account_no, message, amount, receiver_id, date, time) " +
                                        "values (" + CurrentUser.id + ", " + cAccNo + ", \"" + transferMessage.getText() + "\", " + amt +
                                        ", " + receiver + ", \"" + LocalDate.now() + "\", \"" +
                                        LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + "\")");
                                ps.executeUpdate();
                                transferMessage.setText("");
                            }
                        } else {
                            transferLabel.setTextFill(Color.RED);
                            transferLabel.setText("Over account balance");
                            transferAmount.setText("");
                        }
                    }
                }
            }
        } catch (NumberFormatException ne) {
            transferLabel.setTextFill(Color.RED);
            transferLabel.setText("Invalid input");
            transferAmount.setText("");
            receiverNo.setText("");
        }
    }

    @FXML
    public void toAccount(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
        Parent root = loader.load();
        accountController accountCtrl = loader.getController();
        accountCtrl.setCurrentUser(cAccNo);
        accountCtrl.getCurrentUser();
        accountCtrl.getCurrentDeposit();
        super.switchToAccScene(event,root);
    }

    @FXML
    public void toDeposit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("depositScene.fxml"));
        Parent root = loader.load();
        depositController depositCtrl = loader.getController();
        depositCtrl.setCurrentAccount(CurrentAccount.account_number);
        super.switchToDepositScene(event, root);
    }

    @FXML
    public void toWithdraw(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("depositScene.fxml"));
        Parent root = loader.load();
        withdrawController withdrawCtrl = loader.getController();
        withdrawCtrl.setCurrentAccount(cAccNo);
        super.switchToWithdrawScene(event, root);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        super.switchToLoginScene(event);
    }

    @FXML
    public void toPopupConfirmTransfer() throws IOException{
        double amount = Double.parseDouble(transferAmount.getText());
        String receiver = receiverNo.getText();
        String receiverName = "Sengheng";
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("confirmTransferScene.fxml"));
        Parent root = loader.load();
        confirmTransferController controller = loader.getController();
        controller.popupConfirmTransfer(root);
        controller.setTransferInfo(amount,receiver,receiverName);
    }

}
