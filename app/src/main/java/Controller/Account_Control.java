package Controller;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    // check email existed or not
    public boolean CheckExisted(String email) throws Exception {
        boolean bCheck = false;
        try {
            int count = 0;
            PreparedStatement check = connect.prepareStatement("exec SP_CHECKEXISTED ?");
            check.setString(1, email);
            ResultSet resultSet = check.executeQuery();
            while (resultSet.next()) {
                count++;
            }
            if (count > 0) {
                return true;
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // check email input valid or not
    public boolean CheckEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    // get account id
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

    // create new account
    public boolean AddNewAccount(String sEmail, String sName, String sPassword) {
        try {
            PreparedStatement account = connect.prepareStatement("exec SP_ACCOUNT_INSERT ?, ?,?");
            account.setString(1, sName);
            account.setString(2, sEmail);
            account.setString(3, sPassword);
            int i = account.executeUpdate();
            if (i == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

