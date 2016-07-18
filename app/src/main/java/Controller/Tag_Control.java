package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Thanh Huy on 7/9/2016.
 */
public class Tag_Control {
    Connection connect;
    private int output;

    public Tag_Control(Connection connection) {
        connect = connection;
    }

    // ADD TAG
    public boolean AddTag(String name, int Acc_Id) {
        try {
            PreparedStatement query = connect.prepareStatement("exec SP_TAG_INSERT ?, ?");
            query.setString(1, name);
            query.setInt(2, Acc_Id);
            int i = query.executeUpdate();
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

    //LOAD ALL LIST TAG
    public ArrayList<String> LoadList(int Acc_Id) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            PreparedStatement query = connect.prepareStatement("Select * from Tag where Account_Id = " + Acc_Id + " order by name");
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // EDIT TAG
    public boolean EditTag(String Name, int SID) {
        try {
            //  PreparedStatement Pedit = connect.prepareStatement("exec SP_TAG_UPDATE "+SID+",'"+Name+"'");
            PreparedStatement Pedit = connect.prepareStatement("exec SP_TAG_UPDATE ?,?");
            Pedit.setInt(1, SID);
            Pedit.setString(2, Name);
            int i = Pedit.executeUpdate();
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

    // DELETE TAG
    public boolean DeleteTag(int SID) {
        try {
            PreparedStatement delete = connect.prepareStatement("exec SP_TAG_DELETE ?");
            delete.setInt(1, SID);
            int i = delete.executeUpdate();
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

    //GET TAG ID
    // SAI CMNR
    public int GetTagId(String name) {
        int output = 0;
        try {
            PreparedStatement get = connect.prepareStatement("exec SP_GetTagId ?");
            get.setString(1, name);
            // int i = get.executeUpdate();
            ResultSet rs = get.executeQuery();
            while (rs.next()) {
                output = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    //CHECK EXISTED NAME
    public boolean CheckTagExisted(String NameTag) {
        try {
            PreparedStatement check = connect.prepareStatement("exec SP_CheckExistedTag ?");
            int iCount = 0;
            check.setString(1, NameTag);
            ResultSet resultSet = check.executeQuery();
            while (resultSet.next()) {
                iCount++;
            }
            if (iCount > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int GetTagIDByName(String name, int acc_id) throws Exception {
        try {
            PreparedStatement query = connect.prepareStatement("select id from tag where name = '" + name + "' and account_id = " + acc_id);
            ResultSet resultSet = query.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                i = resultSet.getInt("id");
            }
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("query sai");
        }
    }
}
