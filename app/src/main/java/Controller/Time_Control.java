package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Class.Time;
import Class.TimePassData;

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

    //INSERT NEW TIME TO RUN
    public boolean InsertNewTime(int taskId, int accountId, String date, int esTime) {
        boolean b = false;
        try {
            PreparedStatement query = connection.prepareStatement("exec SP_EXCUTEDTASK_INSERT ?, ?, ?, ?");
            query.setInt(1, taskId);
            query.setInt(2, accountId);
            query.setString(3, date);
            query.setInt(4, esTime);
            int i = query.executeUpdate();
            if (i == 1)
                b = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }


    //SELECT STATUS OF TASK
    public int SelectStatus(int idTask) throws Exception {
        try {
            PreparedStatement select = connection.prepareStatement("select Status from Excuted_Task where Id =" + idTask);
            ResultSet result = select.executeQuery();
            int i = 0;
            while (result.next()) {
                i = result.getInt("Status");
            }
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.toString());
        }
    }

    // DELETE EXCUTED TASK WITH STATUS !=1
    public boolean DeleteExecutedTask(int id) {
        try {
            PreparedStatement delete = connection.prepareStatement("exec SP_EXCUTEDTIME_DEL ?");
            delete.setInt(1, id);
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

    // EDIT EXECUTED TASK WITH STATUS !=1
    public boolean EditExecutedTask(int id, int time) {
        try {
            PreparedStatement Pedit = connection.prepareStatement("exec SP_EXCUTEDTIME_UPDATE ?,?");
            Pedit.setInt(1, id);
            Pedit.setInt(2, time);
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

    //CHECK RUNNING
    public boolean CheckRunningTask(int id) {
        boolean c = false;
        try {
            PreparedStatement Pedit = connection.prepareStatement("exec EXCUTEDTASK_RUNNING ?");
            Pedit.setInt(1, id);
            ResultSet result = Pedit.executeQuery();
            int i = 0;
            while (result.next()) {
                i++;
            }
            if (i != 0) {
                c = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
}
