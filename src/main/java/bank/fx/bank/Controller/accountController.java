package bank.fx.bank.Controller;

import bank.fx.bank.Account;
import bank.fx.bank.CurrentAccount;
import bank.fx.bank.CurrentUser;
import bank.fx.bank.Database;
import bank.fx.bank.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class accountController extends sceneController implements Initializable {
    @FXML
    public ChoiceBox<String> transactionType, accountSwitch;
    @FXML
    private Label userIdLabel, depositLabel, withdrawLabel, receiverLabel, transferLabel, accountName, accountBalance,
            accountType;
    @FXML
    private TextField transferMessage, depositAmount, withdrawAmount, transferAmount, receiverNo;
    @FXML
    private ListView<String> transaction;
    Account cAcc;
    int cAccNo;
    String[] transactionChoice = { "Deposit", "Withdraw", "Transfer" };
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

    public void setCurrentUser(int a) {
        cAccNo = a;
    }

    @FXML
    public void getAccountInfo(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("CardScene.fxml"));
        Parent root = loader.load();
        popupWindowController controller = loader.getController();
        controller.popupCard(root);
        controller.setCardInfo(CurrentAccount.account_number, CurrentAccount.account_name);
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
            accountSwitch.setValue(String.valueOf(CurrentAccount.account_number));
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
            transactionType.setValue("Deposit");
            accountSwitch.getItems().addAll(accountNumbers);
            accountSwitch.setValue(String.valueOf(CurrentAccount.account_number));
        } catch (NullPointerException ignored) {
        }
    }

    @FXML
    protected void displayInfo() throws SQLException {
        accountName.setText(CurrentAccount.account_name);
        accountBalance.setText(" $" + CurrentAccount.balance);
        accountType.setText(CurrentAccount.account_type);
    }

    @FXML
    private void getAccount(ActionEvent event) {
        int currentAccNo = 0;
        try {
            currentAccNo = Integer.parseInt(accountSwitch.getValue());
        } catch (NumberFormatException ignored) {
        }
        if (currentAccNo != 0) {
            CurrentAccount.setCurrentAccount(currentAccNo);
            try {
                displayInfo();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            getCurrentDeposit();
        }
    }

    @FXML
    private void getData(ActionEvent event) {
        String choice = transactionType.getValue();
        ResultSet rs;
        switch (choice) {
            case "Deposit" -> {
                transaction.getItems().clear();
                deposit.clear();
                try {
                    rs = Database.get("" +
                            "SELECT account.account_number, account.account_name, deposit.amount, deposit.date, deposit.time FROM account "
                            +
                            "INNER JOIN deposit ON account.account_number=deposit.account_no " +
                            "HAVING account_number=" + CurrentAccount.account_number
                            + " ORDER BY deposit.date DESC, deposit.time DESC;" +
                            "");
                    while (rs.next()) {
                        if (deposit.size() < 10) {
                            deposit.add("Name: " + rs.getString(2) + "\nAmount: $" + rs.getDouble(3)
                                    + "\nDate: " + rs.getString(4) + " | Time: " + rs.getString(5));
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
                            "SELECT account.account_number, account.account_name, withdraw.amount, withdraw.date, withdraw.time FROM account "
                            +
                            "INNER JOIN withdraw ON account.account_number=withdraw.account_no " +
                            "HAVING account_number=" + CurrentAccount.account_number
                            + " ORDER BY withdraw.date DESC, withdraw.time DESC;" +
                            "");
                    while (rs.next()) {
                        if (withdraw.size() < 10) {
                            withdraw.add("Name: " + rs.getString(2) + "\nAmount: $" + rs.getDouble(3)
                                    + "\nDate: " + rs.getString(4) + " | Time: " + rs.getString(5));
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
                    rs = Database.get(
                            "SELECT account.account_number, account.account_name, transfer.amount, transfer.message," +
                                    " transfer.receiver_id , transfer.date, transfer.time FROM account " +
                                    "INNER JOIN transfer ON account.account_number=transfer.account_no " +
                                    "HAVING account_number=" + CurrentAccount.account_number
                                    + " ORDER BY transfer.date desc, transfer.time desc;");
                    while (rs.next()) {
                        currentReceiverId = rs.getInt(5);
                        ResultSet rs2 = Database
                                .get("SELECT DISTINCT account.account_name, transfer.receiver_id FROM account " +
                                        "INNER JOIN transfer ON account.account_number=transfer.receiver_id " +
                                        "HAVING receiver_id=" + currentReceiverId);
                        if (rs2.next()) {
                            receiverName = rs2.getString(1);
                        }
                        if (transfer.size() < 10) {
                            transfer.add("Name: " + rs.getString(2) + "\nAmount: $" + rs.getDouble(3)
                                    + "\nMessage: " + rs.getString(4)
                                    + "\nDate: " + rs.getString(6) + " | Time: " + rs.getString(7) +
                                    "\nReceiver: " + receiverName);
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

    public void getCurrentDeposit() {
        transaction.getItems().clear();
        deposit.clear();
        try {
            ResultSet rs = Database.get("" +
                    "SELECT account.account_number, account.account_name, deposit.amount, deposit.date, deposit.time FROM account "
                    +
                    "INNER JOIN deposit ON account.account_number=deposit.account_no " +
                    "HAVING account_number=" + CurrentAccount.account_number
                    + " ORDER BY deposit.date DESC, deposit.time DESC;" +
                    "");
            while (rs.next()) {
                if (deposit.size() < 10) {
                    deposit.add("Name: " + rs.getString(2) + "\nAmount: $" + rs.getDouble(3)
                            + "\nDate: " + rs.getString(4) + " | Time: " + rs.getString(5));
                } else {
                    break;
                }
            }
            transaction.getItems().addAll(deposit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        super.switchToLoginScene(event);
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

    public void createAccount(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("createAccountScene.fxml"));
        Parent root = loader.load();
        createAccountController createAccCtrl = loader.getController();
        createAccCtrl.setUserId(CurrentUser.id);
        super.switchToCreateAccountScene(event, root);
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
