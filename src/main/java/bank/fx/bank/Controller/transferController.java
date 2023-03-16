package bank.fx.bank.Controller;

import bank.fx.bank.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class transferController extends sceneController {
    @FXML
    TextField transferAmount, receiverNo, transferMessage;
    @FXML
    Label transferLabel, receiverLabel;
    @FXML
    ChoiceBox<String> accountTypeChoice;
    ResultSet rs;
    PreparedStatement ps;
    int cAccNo, receiver;
    private double balance = 0;
    boolean confirmation;
    String username;
    ArrayList<Account> accounts;

    public void setCurrentAccount(int a) {
        cAccNo = a;
        System.out.println(cAccNo);
    }

    public void setAccountSwitch() throws SQLException {
        accounts = CurrentUser.getAccounts();
        accountTypeChoice.getItems().clear();
        ArrayList<String> accountNumbers = new ArrayList<>();
        for (Account account : accounts) {
            accountNumbers.add(String.valueOf(account.account_number));
        }
        try {
            accountTypeChoice.getItems().addAll(accountNumbers);
            accountTypeChoice.setValue(String.valueOf(CurrentAccount.account_number));
        } catch (NullPointerException e) {
            System.out.println("Don't mind me");
        }
    }

    @FXML
    private void getAccount(ActionEvent event) {
        int currentAccNo = 0;
        try {
            currentAccNo = Integer.parseInt(accountTypeChoice.getValue());
        } catch (NumberFormatException ignored) {
        }
        if (currentAccNo != 0) {
            CurrentAccount.setCurrentAccount(currentAccNo);
        }
        System.out.println(CurrentAccount.account_number);
    }

    @FXML
    public void transfer() throws SQLException {
        try {
            if (transferAmount.getText().equals("")) {
                transferLabel.setTextFill(Color.RED);
                transferLabel.setText("Specify an amount");
            } else if (receiverNo.getText().equals("")) {
                receiverLabel.setTextFill(Color.RED);
                receiverLabel.setText("Specify the receiver");
            } else {
                double amt = Double.parseDouble(transferAmount.getText());
                receiver = Integer.parseInt(receiverNo.getText());
                rs = Database.get(
                        "select count(account_number), account_name from account where account_number=" + receiver);
                while (rs.next()) {
                    if (rs.getInt(1) == 0) {
                        receiverLabel.setTextFill(Color.RED);
                        receiverLabel.setText("User does not exist");
                        receiverNo.setText("");
                    } else if (receiver == CurrentAccount.account_number) {
                        receiverLabel.setTextFill(Color.RED);
                        receiverLabel.setText("Current Account");
                        receiverNo.setText("");
                    } else {
                        /* remove balance */
                        username = rs.getString(2);
                        rs = Database.get("select balance from account where account_number=" + CurrentAccount.account_number);
                        while (rs.next()) {
                            balance = rs.getDouble(1);
                        }
                        if (amt <= balance) {
                            double result = balance - amt;
                            ps = Database.set("update account set balance=? where account_number=" + CurrentAccount.account_number);
                            ps.setDouble(1, result);
                            try {
                                FXMLLoader loader = new FXMLLoader(Main.class.getResource("confirmTransferScene.fxml"));
                                Parent root = loader.load();
                                confirmTransferController controller = loader.getController();
                                controller.popupConfirmTransfer(root, amt, receiver, username);
                                confirmation = controller.getCheck();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            if (confirmation) {
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
                                ps = Database.set(
                                        "insert into transfer(user_id, account_no, message, amount, receiver_id, date, time) "
                                                +
                                                "values (" + CurrentUser.id + ", " + CurrentAccount.account_number + ", \""
                                                + transferMessage.getText() + "\", " + amt +
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
        accountCtrl.setCurrentUser(CurrentAccount.account_number);
        accountCtrl.getCurrentUser();
        accountCtrl.getCurrentDeposit();
        super.switchToAccScene(event, root);
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
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("withdrawScene.fxml"));
        Parent root = loader.load();
        withdrawController withdrawCtrl = loader.getController();
        withdrawCtrl.setCurrentAccount(CurrentAccount.account_number);
        super.switchToWithdrawScene(event, root);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        super.switchToLoginScene(event);
    }

    public void toProfile(ActionEvent event) {
    }
}
