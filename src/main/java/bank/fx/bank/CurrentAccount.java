package bank.fx.bank;

import java.sql.ResultSet;

public class CurrentAccount {
    public static int account_number;
    public static String account_name, account_type, phone;
    public static double balance;

    public static void setCurrentAccount(int account_number) {
        try {
            ResultSet rs = Database.get("select * from account where account_number = " + account_number + ";");
            rs.absolute(1);
            CurrentAccount.account_number = rs.getInt("account_number");
            CurrentAccount.account_name = rs.getString("account_name");
            CurrentAccount.account_type = rs.getString("account_type");
            CurrentAccount.balance = rs.getDouble("balance");
            CurrentAccount.phone = rs.getString("phone");
        } catch (Exception e) {
            System.out.println("error");
        }
    }
}
