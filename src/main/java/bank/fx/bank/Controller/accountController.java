package bank.fx.bank.Controller;

import bank.fx.bank.Account;
import bank.fx.bank.CurrentUser;
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
//    protected int userId = CurrentUser.id;
    Account cAcc;
    @FXML
    private double balance = 0, amt = 0, result = 0;
    private ResultSet rs;
    private PreparedStatement ps;
    String[] transactionChoice = {"Deposit", "Withdraw", "Transfer"};
    ArrayList<String> deposit = new ArrayList<>();
    ArrayList<String> withdraw = new ArrayList<>();
    ArrayList<String> transfer = new ArrayList<>();
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    ArrayList<Account> accounts;
    ArrayList<String> accountNumbers;

    public void initializeUser() throws SQLException {
        accounts = CurrentUser.getAccounts();
        cAcc = accounts.get(0);
    }

    public void getCurrentUser() throws SQLException {
        accounts = CurrentUser.getAccounts();
        accountSwitch.getItems().clear();
        ArrayList<String> accountNumbers = new ArrayList<>();
        for (Account account : accounts) {
            accountNumbers.add(String.valueOf(account.account_number));
        }
        try {
            accountSwitch.getItems().addAll(accountNumbers);
            accountSwitch.setValue(String.valueOf(cAcc.account_number));
            accountSwitch.setOnAction(this::getAccount);
        } catch (NullPointerException e) {
            System.out.println("Don't mind me");
        }
        displayInfo();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert.setContentText("Please confirm the transaction");
        try {
            transactionType.getItems().addAll(transactionChoice);
            transactionType.setOnAction(this::getData);
            accountSwitch.getItems().addAll(accountNumbers);
            accountSwitch.setOnAction(this::getAccount);
        } catch (NullPointerException ignored) {
            /* Just ignore it */
        }
    }

    @FXML
    protected void displayInfo() throws SQLException {
//        getCurrentUser();
        rs = Database.get("select account_name, balance, account_type " +
                "from account " +
                "where account_number=" + cAcc.account_number);
        if (rs.next()) {
            accountName.setText(rs.getString(1));
            accountBalance.setText(" $" + rs.getDouble(2));
            accountType.setText(rs.getString(3));
        }
    }

    private void getAccount(ActionEvent event) {
        int currentAccNo=0;
        try {
            currentAccNo = Integer.parseInt(accountSwitch.getValue());
        } catch (NumberFormatException ignored) {}
        if (currentAccNo == 0) {
        } else {
            try {
                ResultSet rs = Database.get("select * from account where account_number="+currentAccNo);
                rs.absolute(1);
                cAcc.account_number = rs.getInt("account_number");
                cAcc.account_name = rs.getString("account_name");
                cAcc.account_type = rs.getString("account_type");
                cAcc.balance = rs.getDouble("balance");
//                System.out.println(cAcc.account_number);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
                            "SELECT account.account_number, account.account_name, deposit.amount, deposit.date, deposit.time FROM account " +
                            "INNER JOIN deposit ON account.account_number=deposit.account_no " +
                            "HAVING account_number=" + cAcc.account_number + " ORDER BY deposit.date DESC, deposit.time DESC;" +
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
                            "SELECT account.account_number, account.account_name, withdraw.amount, withdraw.date, withdraw.time FROM account " +
                            "INNER JOIN withdraw ON account.account_number=withdraw.account_no " +
                            "HAVING account_number=" + cAcc.account_number + " ORDER BY withdraw.date DESC, withdraw.time DESC;" +
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
                    rs = Database.get("SELECT account.account_number, account.account_name, transfer.amount, transfer.message," +
                            " transfer.receiver_id , transfer.date, transfer.time FROM account " +
                            "INNER JOIN transfer ON account.account_number=transfer.account_no " +
                            "HAVING account_number="+cAcc.account_number+" ORDER BY transfer.date desc, transfer.time desc;");
                    while (rs.next()) {
                        currentReceiverId = rs.getInt(5);
                        ResultSet rs2 = Database.get("SELECT DISTINCT account.account_name, transfer.receiver_id FROM account " +
                                "INNER JOIN transfer ON account.account_number=transfer.receiver_id " +
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

//    @FXML
//    public void toAccount(ActionEvent event) throws IOException, SQLException {
//        FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
//        Parent root = loader.load();
//        accountController accountCtrl = loader.getController();
//        accountCtrl.initializeUser();
//        super.switchToAccScene(event,root);
//    }

    @FXML
    public void toDeposit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("depositScene.fxml"));
        Parent root = loader.load();
        depositController depositCtrl = loader.getController();
        depositCtrl.setCurrentAccount(cAcc);
        super.switchToDepositScene(event, root);
    }

    @FXML
    public void toWithdraw(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("withdrawScene.fxml"));
        Parent root = loader.load();
        withdrawController withdrawCtrl = loader.getController();
        withdrawCtrl.setCurrentAccount(cAcc);
        super.switchToWithdrawScene(event, root);
    }
}
