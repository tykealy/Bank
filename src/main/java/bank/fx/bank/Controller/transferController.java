package bank.fx.bank.Controller;

import bank.fx.bank.Account;
import bank.fx.bank.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class transferController {
    @FXML
    TextField transferAmount, receiverNo, transferMessage;
    @FXML
    Label transferLabel, receiverLabel;
    ResultSet rs; PreparedStatement ps;
    Account cAcc;
    private double balance = 0, amt = 0, result = 0;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

    public void setCurrentAccount(Account a) {
        cAcc = a;
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
                amt = Double.parseDouble(transferAmount.getText());
                int receiver = Integer.parseInt(receiverNo.getText());
                rs = Database.get("select count(account_number), account_name from account where account_number=" + receiver);
                while (rs.next()) {
                    if (rs.getInt(1) == 0) {
                        receiverLabel.setTextFill(Color.RED);
                        receiverLabel.setText("User does not exist");
                        receiverNo.setText("");
                    } else if (receiver == cAcc.account_number) {
                        receiverLabel.setTextFill(Color.RED);
                        receiverLabel.setText("Current Account");
                        receiverNo.setText("");
                    } else {
                        /* remove balance */
                        username = rs.getString(2);
                        rs = Database.get("select balance from account where account_number=" + cAcc.account_number);
                        while (rs.next()) {
                            balance = rs.getDouble(1);
                        }
                        if (amt <= balance) {
                            result = balance - amt;
                            ps = Database.set("update account set balance=? where account_number=" + cAcc.account_number);
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
                                ps = Database.set("insert into transfer(account_number, message, amount, receiver_id, date, time) " +
                                        "values (" + cAcc.account_number + ", \"" + transferMessage.getText() + "\", " + amt +
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
}
