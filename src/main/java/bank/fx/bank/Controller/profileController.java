package bank.fx.bank.Controller;

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class profileController extends sceneController implements Initializable {
    @FXML
    private TextField firstName, lastName, phone, email;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstName.setText(CurrentUser.firstName);
        lastName.setText(CurrentUser.lastName);
        phone.setText(CurrentUser.phone);
        email.setText(CurrentUser.email);
        // throw new UnsupportedOperationException("Unimplemented method 'initialize'");
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

    public void toTransfer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("transferScene.fxml"));
        Parent root = loader.load();
        super.switchToTransferScene(event, root);
    }

    public void update() throws SQLException {
        String fname = firstName.getText();
        String lname = lastName.getText();
        String tel = phone.getText();
        String mail = email.getText();

        PreparedStatement stmt = Database.update(
                "update users set first_name = ?, last_name = ?, phone = ?, email = ? where phone = ? ;");
        stmt.setString(1, fname);
        stmt.setString(2, lname);
        stmt.setString(3, tel);
        stmt.setString(4, mail);
        stmt.setString(5, CurrentAccount.phone);
        stmt.executeUpdate();
        ResultSet rs = Database
                .get("select * from users where email = '" + CurrentUser.email + "';");
        rs.absolute(1);
        CurrentUser.setCurrentUser(rs);
    }
}
