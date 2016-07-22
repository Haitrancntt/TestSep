package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Class.Time;

/**
 * Created by Thanh Huy on 7/23/2016.
 */
public class Time_Control {
    Connection connection;

    public Time_Control(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Time> LoadTime(int accId, String date) {
        ArrayList<Time> arrayList = new ArrayList<Time>();
        try {
            PreparedStatement query = connection.prepareStatement("exec sp_excutedtask_load ?,?");
            query.setInt(1, accId);
            query.setString(2, date);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String tagName = resultSet.getString("TagName");
                String taskName = resultSet.getString("TaskName");
                int esHrs = resultSet.getInt("EsHour");
                int esMin = resultSet.getInt("EsMinute");
                int acHrs = resultSet.getInt("AcHour");
                int acMin = resultSet.getInt("AcMinute");
                String start, end;
                if (resultSet.getTime("Start") == null) {
                    start = "";
                } else {
                    start = resultSet.getTime("Start").toString();
                }
                if (resultSet.getTime("End") == null) {
                    end = "";
                } else {
                    end = resultSet.getTime("End").toString();
                }
                int run = resultSet.getInt("Running");
                int done = resultSet.getInt("Done");
                Time time = new Time(id, tagName, taskName, esHrs, esMin, acHrs, acMin, start, end, run, done);
                arrayList.add(time);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
