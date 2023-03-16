package bank.fx.bank.Controller;

import bank.fx.bank.*;
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

public class withdrawController extends sceneController {
    @FXML
    public TextField withdrawAmount;
    @FXML
    public Label withdrawLabel, currentAccNo, remainingBalance;
    private double balance = 0;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    int cAccNo;
    public void setCurrentAccount(int a) {
        cAccNo = a;
        currentAccNo.setText(String.valueOf(cAccNo));
    }

    @FXML
    protected void withdraw(ActionEvent event) throws SQLException {
        try {
            double amt = Double.parseDouble(withdrawAmount.getText());
            if (amt == 0) {
                withdrawAmount.setText("");
                withdrawLabel.setText("");
            } else {
                ResultSet rs = Database.get("select balance from account where account_number=" + cAccNo);
                while (rs.next()) {
                    balance = rs.getDouble(1);
                }
                if (amt <= balance) {
                    double result = balance - amt;
                    PreparedStatement ps = Database.set("update account set balance=? where account_number=" + cAccNo);
                    ps.setDouble(1, result);
                    alert.setHeaderText("Withdrawn Amount: $" + amt);

                    if (alert.showAndWait().get() == ButtonType.OK) {
                        ps.executeUpdate();
                        withdrawLabel.setTextFill(Color.GREEN);
                        remainingBalance.setTextFill(Color.GREEN);
                        withdrawLabel.setText("Withdraw Successfully");
                        remainingBalance.setText("Remaining Balance: $"+result);
                        withdrawAmount.setText("");
                        /* add to withdraw table */
                        ps = Database.set("insert into withdraw(user_id, account_no, amount, date, time) " +
                                "values (" + CurrentUser.id + ", " + cAccNo + ", " + amt + ", \"" + LocalDate.now() + "\", \"" +
                                LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + "\")");
                        ps.executeUpdate();
                    }
                } else {
                    withdrawLabel.setTextFill(Color.RED);
                    withdrawLabel.setText("Over account balance");
                    withdrawAmount.setText("");
                }
            }
        } catch (NumberFormatException ne) {
            withdrawLabel.setTextFill(Color.RED);
            withdrawLabel.setText("Invalid amount");
            withdrawAmount.setText("");
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
        depositCtrl.setCurrentAccount(cAccNo);
        super.switchToDepositScene(event, root);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        super.switchToLoginScene(event);
    }
}
