package bank.fx.bank.Controller;

import bank.fx.bank.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class accountController extends sceneController {
    @FXML
    public Label userIdLabel;
    @FXML
    private TextField depositAmount;
    @FXML
    private TextField withdrawAmount;
    @FXML
    private TextField transferAmount;
    @FXML
    protected TextField receiverNo;
    @FXML
    private Label depositLabel;
    @FXML
    private Label withdrawLabel;
    @FXML
    private Label receiverLabel;
    @FXML
    private Label transferLabel;
    @FXML
    private Label accountInfo;
    protected int userId;
    private double balance = 0;
    private double amt = 0;
    private double result = 0;
    private ResultSet rs;
    private PreparedStatement ps;

    public void setUserIdLabel(String id) {
        userIdLabel.setText(id);
        userId = Integer.parseInt(userIdLabel.getText());
        userIdLabel.setText("");
    }

    @FXML
    protected void displayInfo() throws SQLException {
        rs = Database.get("select users.user_id, users.name, account.balance " +
                "from users " +
                "inner join account on users.user_id=account.user_id " +
                "having users.user_id=" + userId);
        while (rs.next()) {
            accountInfo.setText(
                    "User: " + rs.getString(2) +
                            " | Balance: $" + rs.getDouble(3));
        }
    }

    @FXML
    protected void withdraw() throws SQLException {
        // authenticate

        // action
        if (withdrawAmount.getText().equals("")) {
            withdrawLabel.setTextFill(Color.RED);
            withdrawLabel.setText("Invalid Amount");
            withdrawAmount.setText("");
        } else {
            amt = Double.parseDouble(withdrawAmount.getText());
            rs = Database.get("select balance from account where user_id=" + userId);
            while (rs.next()) {
                balance = rs.getDouble(1);
            }
            if (amt <= balance) {
                result = balance - amt;
                ps = Database.set("update account set balance=? where user_id=" + userId);
                ps.setDouble(1, result);
                ps.executeUpdate();
                withdrawLabel.setTextFill(Color.GREEN);
                withdrawLabel.setText("Withdraw Successfully");
                withdrawAmount.setText("");
            } else {
                withdrawLabel.setTextFill(Color.RED);
                withdrawLabel.setText("Over account balance");
                withdrawAmount.setText("");
            }
        }
    }

    @FXML
    protected void deposit() throws SQLException {
        // authenticate

        // action
        if (depositAmount.getText().equals("")) {
            depositLabel.setTextFill(Color.RED);
            depositLabel.setText("Invalid Amount");
            depositAmount.setText("");
        } else {
            amt = Double.parseDouble(depositAmount.getText());
            rs = Database.get("select balance from account where user_id=" + userId);
            while (rs.next()) {
                balance = rs.getDouble(1);
                result = balance + amt;
            }
            ps = Database.set("update account set balance=? where user_id=" + userId);
            ps.setDouble(1, result);
            ps.executeUpdate();
            depositLabel.setTextFill(Color.GREEN);
            depositLabel.setText("Deposit Successfully");
            depositAmount.setText("");
        }
    }

    @FXML
    public void transfer() throws SQLException {
        if (transferAmount.getText().equals("")) {
            transferLabel.setTextFill(Color.RED);
            transferLabel.setText("Specify an amount");
        } else if (receiverNo.getText().equals("")) {
            receiverLabel.setTextFill(Color.RED);
            receiverLabel.setText("Specify the receiver");
        } else {
            amt = Double.parseDouble(transferAmount.getText());
            int receiver = Integer.parseInt(receiverNo.getText());
            rs = Database.get("select count(user_id) from account where user_id=" + receiver);
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
                    rs = Database.get("select balance from account where user_id=" + userId);
                    while (rs.next()) {
                        balance = rs.getDouble(1);
                    }
                    if (amt <= balance) {
                        result = balance - amt;
                        ps = Database.set("update account set balance=? where user_id=" + userId);
                        ps.setDouble(1, result);
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
                        receiverLabel.setText("");
                    } else {
                        transferLabel.setTextFill(Color.RED);
                        transferLabel.setText("Over account balance");
                        transferAmount.setText("");
                    }
                }
            }
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        super.switchToMainScene(event);
    }
}
