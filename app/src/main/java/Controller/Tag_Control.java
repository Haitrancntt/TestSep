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

    public Tag_Control(Connection connection) {
        connect = connection;
    }

    public boolean AddTag(String name, int Acc_Id) {
        try {
            PreparedStatement query = connect.prepareStatement("exec SP_TAG_INSERT " + "?, ?");
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

    public ArrayList<String> LoadList(int Acc_Id) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            PreparedStatement query = connect.prepareStatement("Select * from Tag where Account_Id = " + Acc_Id);
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
