package bank.fx.bank.Controller;

import bank.fx.bank.Database;
import bank.fx.bank.Main;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class accountController extends sceneController implements Initializable {
    @FXML
    public ChoiceBox<String> transactionType, accountSwitch;
    @FXML
    private Label userIdLabel, depositLabel, withdrawLabel, receiverLabel, transferLabel, accountName, accountBalance, accountType;
    @FXML
    private TextField transferMessage, depositAmount, withdrawAmount, transferAmount, receiverNo;
    @FXML
    private ListView<String> transaction;
    protected int userId = 4;
    @FXML
    private double balance = 0, amt = 0, result = 0;
    private ResultSet rs;
    private PreparedStatement ps;
    String[] transactionChoice = {"Deposit", "Withdraw", "Transfer"};
    ArrayList<String> deposit = new ArrayList<>();
    ArrayList<String> withdraw = new ArrayList<>();
    ArrayList<String> transfer = new ArrayList<>();

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

    public void setUserIdLabel(String id) throws SQLException {
        /* Initialized Alert */
        alert.setContentText("Please confirm the transaction");
        userIdLabel.setText(id);
        /* Set current user id */
        userId = Integer.parseInt(userIdLabel.getText());
        userIdLabel.setText("");
        displayInfo();
    }

    @FXML
    protected void displayInfo() throws SQLException {
        rs = Database.get("select account_name, balance, account_type " +
                "from account " +
                "where user_id=" + userId);
        if (rs.next()) {
            accountName.setText(rs.getString(1));
            accountBalance.setText(" $" + rs.getDouble(2));
            accountType.setText(rs.getString(3));
        }
    }

    @FXML
    protected void deposit() throws SQLException {
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
                    // add to deposit table
                    ps = Database.set("insert into deposit(user_id, amount, date, time) " +
                            "values (" + userId + ", " + amt + ", \"" + LocalDate.now() + "\", \"" +
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

    @FXML
    protected void withdraw() throws SQLException {
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
                        /* add to withdraw table */
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
                        /* remove balance */
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

                                /* add to receiver */
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
                                /* add to transfer table */
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            accountName.setText("");
            accountBalance.setText("");
            accountType.setText("");
            transactionType.getItems().addAll(transactionChoice);
            transactionType.setOnAction(this::getData);
            displayInfo();
        } catch (NullPointerException ignored) {
            /* Just ignore it */
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getData(ActionEvent event) {
        String choice = transactionType.getValue();
        switch (choice) {
            case "Deposit" -> {
                transaction.getItems().clear();
                deposit.clear();
                try {
                    rs = Database.get("" +
                            "SELECT account.user_id, account.account_name, deposit.amount, deposit.date, deposit.time FROM account " +
                            "INNER JOIN deposit ON account.user_id=deposit.user_id " +
                            "HAVING user_id=" + userId + " ORDER BY deposit.date DESC, deposit.time DESC;" +
                            "");
                    while (rs.next()) {
                        if (deposit.size() < 10) {
                            deposit.add("Name: " + rs.getString(2) + "    | Amount: $" + rs.getDouble(3)
                                    + "\nDate: "+rs.getString(4)+" | Time: "+rs.getString(5));
                        } else {
                            break;
                        }
                    }
                    transaction.getItems().addAll(deposit);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (NullPointerException e) {
                    System.out.println("Sh*t Not Again!");
                }
            }
            case "Withdraw" -> {
                transaction.getItems().clear();
                withdraw.clear();
                try {
                    rs = Database.get("" +
                            "SELECT account.user_id, account.account_name, withdraw.amount, withdraw.date, withdraw.time FROM account " +
                            "INNER JOIN withdraw ON account.user_id=withdraw.user_id " +
                            "HAVING user_id=" + userId + " ORDER BY withdraw.date DESC, withdraw.time DESC;" +
                            "");
                    while (rs.next()) {
                        if (withdraw.size() < 10) {
                            withdraw.add("Name: " + rs.getString(2) + "    | Amount: $" + rs.getDouble(3)
                                    + "\nDate: "+rs.getString(4)+" | Time: "+rs.getString(5));
                        } else {
                            break;
                        }
                    }
                    transaction.getItems().addAll(withdraw);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (NullPointerException e) {
                    System.out.println("Sh*t Not Again!");
                }
            }
            case "Transfer" -> {
                transaction.getItems().clear();
                transfer.clear();
                try {
                    int currentReceiverId;
                    String receiverName = null;
                    rs = Database.get("SELECT account.user_id, account.account_name, transfer.amount, transfer.message," +
                            " transfer.receiver_id , transfer.date, transfer.time FROM account " +
                            "INNER JOIN transfer ON account.user_id=transfer.user_id " +
                            "HAVING user_id="+userId+" ORDER BY transfer.date desc, transfer.time desc;");
                    while (rs.next()) {
                        currentReceiverId = rs.getInt(5);
                        ResultSet rs2 = Database.get("SELECT DISTINCT account.account_name, transfer.receiver_id FROM account " +
                                "INNER JOIN transfer ON account.user_id=transfer.receiver_id " +
                                "HAVING receiver_id=" + currentReceiverId);
                        if (rs2.next()) {
                            receiverName = rs2.getString(1);
                        }
                        if (transfer.size() < 10) {
                            transfer.add("Name: " + rs.getString(2) + "    | Amount: $" + rs.getDouble(3)
                                    + "\nMessage: " +rs.getString(4)
                                    + "\nDate: "+rs.getString(6)+" | Time: "+rs.getString(7) +
                                    "\nReceiver: "+receiverName
                                );
                        } else {
                            break;
                        }
                    }
                    transaction.getItems().addAll(transfer);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (NullPointerException e) {
                    System.out.println("Sh*t Not Again!");
                }
            }
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        super.switchToLoginScene(event);
    }

    @FXML
    public void toAccount(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
        Parent root = loader.load();
        super.switchToAccScene(event,root);
    }

    @FXML
    public void toDeposit(ActionEvent event) throws IOException {
        super.switchToDepositScene(event);
    }

    @FXML
    public void toWithdraw(ActionEvent event) throws IOException {
        super.switchToWithdrawScene(event);
    }
}
