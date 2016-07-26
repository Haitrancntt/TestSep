package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Class.TimePassData;
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

    // ADD CURRENT TIME
    public boolean AddCurrentTimeStar(int iExceId) {
        boolean b = false;
        try {
            PreparedStatement query = connection.prepareStatement("exec SP_EXCUTEDTASK_START ?");
            query.setInt(1, iExceId);
            int i = query.executeUpdate();
            if (i == 1) {
                b = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    // END TIME
    public boolean AddTimeEnd(int iExceId) {
        boolean b = false;
        try {
            PreparedStatement add = connection.prepareStatement("exec SP_EXCUTEDTASK_END ?");
            add.setInt(1, iExceId);
            int i = add.executeUpdate();
            if (i == 1) {
                b = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    //LOAD LIST EXECUTED TASK
    public TimePassData LoadList(int idExceTask) {
        TimePassData list = null;
        try {
            PreparedStatement load = connection.prepareStatement("exec SP_EXCUTEDTASK_LOAD_ONE ? ");
            load.setInt(1, idExceTask);
            ResultSet rs = load.executeQuery();
            while (rs.next()) {
                String name = rs.getString("TaskName");
                String tag_name = rs.getString("TagName");
                String start, end;
                if (rs.getTime("Start") == null) {
                    start = "";
                } else {
                    start = rs.getTime("Start").toString();
                }
                if (rs.getTime("End") == null) {
                    end = "";
                } else {
                    end = rs.getTime("End").toString();
                }
                int idExce = rs.getInt("ID");
                int esHrs = rs.getInt("EsHour");
                int esMin = rs.getInt("EsMinute");
                int acHrs = rs.getInt("AcHour");
                int acMin = rs.getInt("AcMinute");
             list = new TimePassData(idExce, name, tag_name, start, end, esHrs, esMin, acHrs, acMin);
              //  list.add(timePassData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
