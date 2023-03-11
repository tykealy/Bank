package bank.fx.bank;

import java.sql.*;

public class Database {
    static Connection con;

    public static void connect() throws SQLException {
        if (con == null) {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bank_management", "root", "0603");
        }
    }

     public static ResultSet grab(String sqlString) throws SQLException {
         connect();
         Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
         return stmt.executeQuery(sqlString);
     }

    public static PreparedStatement create(String sqlString) throws SQLException {
        connect();
        PreparedStatement ps = con.prepareStatement(sqlString);
        return ps;
    }

    public static PreparedStatement update(String sqlString) throws SQLException {
        connect();
        PreparedStatement ps = con.prepareStatement(sqlString);
        return ps;
    }

    public static PreparedStatement destroy(String sqlString) throws SQLException {
        connect();
        PreparedStatement ps = con.prepareStatement(sqlString);
        return ps;
    }

    public static ResultSet get(String sqlString) throws SQLException {
        connect();
        PreparedStatement ps = con.prepareStatement(sqlString);
        ResultSet rs = ps.executeQuery();
        return rs;
    }

    public static PreparedStatement set(String sqlString) throws SQLException {
        connect();
        PreparedStatement ps = con.prepareStatement(sqlString);
        return ps;
    }
}
