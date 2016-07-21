package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Class.Task;

/**
 * Created by Thanh Huy on 7/14/2016.
 */
public class Task_Control {
    Connection connection;

    public Task_Control(Connection connection) {
        this.connection = connection;
    }

    //LOAD LIST TASK
    public ArrayList<Task> LoadList(int acc_id) {
        ArrayList<Task> list = new ArrayList<Task>();
        try {
            PreparedStatement query = connection.prepareStatement("exec sp_task_load " + acc_id);
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                String name = rs.getString("TaskName");
                String tag_name = rs.getString("TagName");
                Task task = new Task(tag_name, name);
                list.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    //CHECK EXISTED TASK
    public boolean CheckExistedTask(int acc_id, String taskName) {
        boolean b = false;
        try {
            PreparedStatement query = connection.prepareStatement("sp_task_checkexisted ?, ?");
            query.setString(1, taskName);
            query.setInt(2, acc_id);
            ResultSet resultSet = query.executeQuery();
            int count = 0;
            while (resultSet.next()) {
                count++;
            }
            if (count != 0) {
                b = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    // ADD TASK
    public boolean AddTask(String name, int tagId, int accountID) {
        boolean b = false;
        try {
            PreparedStatement query = connection.prepareStatement("exec sp_task_insert ?, ?, ?");
            query.setString(1, name);
            query.setInt(2, tagId);
            query.setInt(3, accountID);
            int i = query.executeUpdate();
            if (i == 1) {
                b = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    // EDIT TASK
    public boolean EditTask(String Name, int ID, int TagId) {
        try {
            //  PreparedStatement Pedit = connect.prepareStatement("exec SP_TAG_UPDATE "+SID+",'"+Name+"'");
            PreparedStatement Pedit = connection.prepareStatement("exec SP_TASK_UPDATE ?,?,?");
            Pedit.setInt(1, ID);
            Pedit.setInt(2, TagId);
            Pedit.setString(3, Name);
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

    //GET TASK ID BY NAME
    public int GetTaskIDByName(String name, int acc_id) throws Exception {
        try {
            PreparedStatement query = connection.prepareStatement("select id from task where name = '" + name + "' and account_id = " + acc_id);
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

    // DELETE TASK
    public boolean DeleteTag(int SID) {
        try {
            PreparedStatement delete = connection.prepareStatement("exec SP_TASK_DELETE ?");
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
}
