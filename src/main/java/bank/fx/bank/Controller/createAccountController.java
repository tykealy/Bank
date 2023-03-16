package bank.fx.bank.Controller;

import bank.fx.bank.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static bank.fx.bank.Account.accNoGenerator;

public class createAccountController extends sceneController implements Initializable {
    @FXML
    public Label errorAccName;
    @FXML
    public TextField accNameField;
    @FXML
    public ChoiceBox<String> accountType;
    String[] accType = {"Default", "Saving", "Salary"};
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    int id;

    public void setUserId(int id){
        this.id = id;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountType.getItems().addAll(accType);
        accountType.setValue("Default");
    }

    @FXML
    public void create(ActionEvent event) {
        if (accNameField.getText().isBlank()) {
            errorAccName.setText("Please fill in your account name");
        } else {
            try {
                alert.setTitle("Account Creation");
                alert.setHeaderText("Account create successfully");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    String sqlString = """
                        INSERT INTO `bank_management`.`account`
                        (`account_number`, `user_id`, `account_name`, `account_type`)
                         VALUES (?, ?, ?, ?);
                                        """;
                    try {
                        PreparedStatement stmt = Database.create(sqlString);
                        stmt.setInt(1, accNoGenerator());
                        stmt.setInt(2, id);
                        stmt.setString(3, accNameField.getText());
                        stmt.setString(4, accountType.getValue());
                        stmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
                    Parent root = loader.load();
                    accountController accCtrl = loader.getController();
                    accCtrl.getCurrentUser();
                    accCtrl.getCurrentDeposit();
                    super.switchToAccScene(event, root);
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void toAccount(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("accountScene.fxml"));
        Parent root = loader.load();
        accountController accountCtrl = loader.getController();
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
}
