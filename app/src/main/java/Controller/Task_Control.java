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
}
