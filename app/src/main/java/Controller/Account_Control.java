package Controller;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Thanh Huy on 7/9/2016.
 */
public class Account_Control {
    Connection connect;

    // connect database
    public Account_Control(Connection connection) {
        this.connect = connection;
    }

    // check username
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

    // Check user login
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

    // check email input valid or not
    public boolean CheckEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public int GetAccountID(String username) throws Exception {
        try {
            PreparedStatement query = connect.prepareStatement("select Id from Account where Username = '" + username + "'");
            ResultSet rs = query.executeQuery();
            int i = 0;
            while (rs.next()) {
                i = rs.getInt("Id");
            }
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("query sai ");
        }
    }
}

