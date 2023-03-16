package bank.fx.bank;

import bank.fx.bank.Controller.popupWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class Account {
    public int account_number;
    public String account_name;
    public String account_type;
    public Double balance;
    public int is_active;
    private static final Random rand = new Random();

    public Account(String account_name, int account_number, String account_type, Double balance, int is_active) {
        this.account_name = account_name;
        this.account_number = account_number;
        this.account_type = account_type;
        this.balance = balance;
        this.is_active = is_active;
    }

    public static ArrayList<Account> get() throws SQLException {
        ResultSet accountSet = Database.get("select * from account where user_id = " + CurrentUser.id + ";");
        int i = 0;
        while (accountSet.next()) {
            i++;
        }
        ArrayList<Account> accounts = new ArrayList<>();
        while (i > 0) {
            accountSet.absolute(i);
            String account_name = accountSet.getString("account_name");
            int account_number = accountSet.getInt("account_number");
            String account_type = accountSet.getString("account_type");
            Double balance = accountSet.getDouble("balance");
            int is_active = accountSet.getInt("is_active");
            Account a = new Account(account_name, account_number, account_type, balance, is_active);
            accounts.add(a);
            i--;
        }
        return accounts;
    }

    public static int accNoGenerator() throws SQLException {
        int exist, val = rand.nextInt(90000000) + 10000000;
//        Database.grab("SELECT account_number FROM `bank_management`.`account`");
        ResultSet rs = Database.grab("SELECT account_number FROM `bank_management`.`account`");
        while (rs.next()) {
            exist = rs.getInt("account_number");
            if (exist == val) {
                val = rand.nextInt(90000000) + 10000000;
                rs.beforeFirst();
            }
        }
        return val;
    }
}
