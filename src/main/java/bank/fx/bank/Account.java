package bank.fx.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Account {
    public String account_number;
    public String account_name;
    public String account_type;
    public Double balance;
    public int is_active;

    public Account(String account_name, String account_number, String account_type, Double balance, int is_active) {
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
        ArrayList<Account> accounts = new ArrayList<Account>();
        while (i > 0) {
            accountSet.absolute(i);
            String account_name = accountSet.getString("account_name");
            String account_number = accountSet.getString("account_number");
            String account_type = accountSet.getString("account_type");
            Double balance = accountSet.getDouble("balance");
            int is_active = accountSet.getInt("is_active");
            Account a = new Account(account_name, account_number, account_type, balance, is_active);
            accounts.add(a);
            i--;
        }
        return accounts;
    }
}
