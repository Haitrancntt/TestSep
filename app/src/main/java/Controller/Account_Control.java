package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Thanh Huy on 7/9/2016.
 */
public class Account_Control {
    Connection connect;

    public Account_Control(Connection connection) {
        this.connect = connection;
    }

    public boolean CheckUsername(String username) throws Exception {
        try {
            PreparedStatement query = connect.prepareStatement("select Username from Account where Username = '" + username + "'");
            ResultSet rs = query.executeQuery();
            boolean b = false;
            while (rs.next()) {
                String outputUsername = rs.getString("Username");
                if (outputUsername.equals(username))
                    b = true;
                else
                    b = false;
            }
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("query sai ");
        }
    }

    public boolean CheckLogin(String username, String password) throws Exception {

        try {
            PreparedStatement query = connect.prepareStatement("exec sp_account_checklogin '" + username + "'");
            ResultSet rs = query.executeQuery();
            boolean b = false;
            while (rs.next()) {
                String outputPassword = rs.getString("Password");
                if (password.equals(outputPassword))
                    b = true;
                else
                    b = false;
            }
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("query sai ");
        }
    }

    public boolean CheckEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}

