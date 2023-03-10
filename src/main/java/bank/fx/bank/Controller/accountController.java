package bank.fx.bank.Controller;

import bank.fx.bank.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class accountController extends sceneController {
    @FXML
    private Label userIdLabel, depositLabel, withdrawLabel, receiverLabel, transferLabel, accountInfo;
    @FXML
    private TextField transferMessage, depositAmount, withdrawAmount, transferAmount, receiverNo;
    protected int userId;
    private double balance = 0, amt = 0, result = 0;
    private ResultSet rs;
    private PreparedStatement ps;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

    public void setUserIdLabel(String id) {
        alert.setContentText("Please confirm the transaction");
        userIdLabel.setText(id);
        userId = Integer.parseInt(userIdLabel.getText());
        userIdLabel.setText("");
    }

    @FXML
    protected void displayInfo() throws SQLException {
        rs = Database.get("select account_name, balance, account_type " +
                "from account " +
                "where user_id=" + userId);
        while (rs.next()) {
            accountInfo.setText(
                "User: " + rs.getString(1) +
                " | Balance: $" + rs.getDouble(2) +
                " | Type: " + rs.getString(3)
            );
        }
    }

    @FXML
    protected void deposit() throws SQLException {
        // authenticate

        // action
        try {
            amt = Double.parseDouble(depositAmount.getText());
            if (amt == 0) {
                depositAmount.setText("");
                depositLabel.setText("");
            } else {
                rs = Database.get("select balance from account where user_id=" + userId);
                while (rs.next()) {
                    balance = rs.getDouble(1);
                    result = balance + amt;
                }
                ps = Database.set("update account set balance=? where user_id=" + userId);
                ps.setDouble(1, result);
                alert.setHeaderText("Deposit Amount: $" + amt);

                if (alert.showAndWait().get() == ButtonType.OK) {
                    ps.executeUpdate();
                    depositLabel.setTextFill(Color.GREEN);
                    depositLabel.setText("Deposit Successfully");
                    depositAmount.setText("");
                    //add to deposit table
                    ps = Database.set("insert into deposit(user_id, amount, date, time) " +
                            "values (" + userId + ", " + amt + ", \"" + LocalDate.now() + "\", \"" +
                            LocalTime.now().truncatedTo(ChronoUnit.SECONDS) +"\")");
                    ps.executeUpdate();
                }
            }
        } catch (NumberFormatException ne) {
            depositLabel.setTextFill(Color.RED);
            depositLabel.setText("Invalid amount");
            depositAmount.setText("");
        }
    }

    @FXML
    protected void withdraw() throws SQLException {
        // authenticate

        // action
        try {
            amt = Double.parseDouble(withdrawAmount.getText());
            if (amt == 0) {
                withdrawAmount.setText("");
                withdrawLabel.setText("");
            } else {
                rs = Database.get("select balance from account where user_id=" + userId);
                while (rs.next()) {
                    balance = rs.getDouble(1);
                }
                if (amt <= balance) {
                    result = balance - amt;
                    ps = Database.set("update account set balance=? where user_id=" + userId);
                    ps.setDouble(1, result);
                    alert.setHeaderText("Withdrawn Amount: $" + amt);

                    if (alert.showAndWait().get() == ButtonType.OK) {
                        ps.executeUpdate();
                        withdrawLabel.setTextFill(Color.GREEN);
                        withdrawLabel.setText("Withdraw Successfully");
                        withdrawAmount.setText("");
                        // add to withdraw table
                        ps = Database.set("insert into withdraw(user_id, amount, date, time) " +
                                "values (" + userId + ", " + amt + ", \"" + LocalDate.now() + "\", \"" +
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
                rs = Database.get("select count(user_id), account_name from account where user_id=" + receiver);
                while (rs.next()) {
                    if (rs.getInt(1) == 0) {
                        receiverLabel.setTextFill(Color.RED);
                        receiverLabel.setText("User does not exist");
                        receiverNo.setText("");
                    } else if (receiver == userId) {
                        receiverLabel.setTextFill(Color.RED);
                        receiverLabel.setText("Current Account");
                        receiverNo.setText("");
                    } else {
                        // remove balance
                        username = rs.getString(2);
                        rs = Database.get("select balance from account where user_id=" + userId);
                        while (rs.next()) {
                            balance = rs.getDouble(1);
                        }
                        if (amt <= balance) {
                            result = balance - amt;
                            ps = Database.set("update account set balance=? where user_id=" + userId);
                            ps.setDouble(1, result);
                            alert.setHeaderText("Transfer Amount: $" + amt + "\nReceiver Name: " + username);

                            if (alert.showAndWait().get() == ButtonType.OK) {
                                ps.executeUpdate();

                                // add to receiver
                                rs = Database.get("select balance from account where user_id=" + receiver);
                                while (rs.next()) {
                                    balance = rs.getDouble(1);
                                    result = balance + amt;
                                }
                                ps = Database.set("update account set balance=? where user_id=" + receiver);
                                ps.setDouble(1, result);
                                ps.executeUpdate();
                                transferLabel.setTextFill(Color.GREEN);
                                transferLabel.setText("Transfer Successfully");
                                transferAmount.setText("");
                                receiverNo.setText("");
                                receiverLabel.setText("");
                                // add to transfer table
                                ps = Database.set("insert into transfer(user_id, message, amount, receiver_id, date, time) " +
                                        "values (" + userId + ", \"" + transferMessage.getText() + "\", " + amt +
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
    public void logout(ActionEvent event) throws IOException {
        super.switchToLoginScene(event);
    }
}
