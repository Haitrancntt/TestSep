package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Thanh Huy on 7/29/2016.
 */
public class Report_Control {
    private Connection connection;

    public Report_Control(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Integer> GetNumberOfTask(int accId, String date)

    {
        ArrayList<Integer> arrayList = new ArrayList<>();
        try {
            PreparedStatement queryTotal = connection.prepareStatement("exec sp_excutedtask_count_total ?, ?");
            queryTotal.setInt(1, accId);
            queryTotal.setString(2, date);
            ResultSet rs = queryTotal.executeQuery();
            while (rs.next()) {
                arrayList.add(rs.getInt("total"));
            }
            PreparedStatement queryDone = connection.prepareStatement("exec sp_excutedtask_count_done ?, ?");
            queryDone.setInt(1, accId);
            queryDone.setString(2, date);
            ResultSet rs1 = queryDone.executeQuery();
            while (rs1.next()) {
                arrayList.add(rs1.getInt("done"));
            }
            PreparedStatement queryNotDone = connection.prepareStatement("exec sp_excutedtask_count_not_done ?, ?");
            queryNotDone.setInt(1, accId);
            queryNotDone.setString(2, date);
            ResultSet rs2 = queryNotDone.executeQuery();
            while (rs2.next()) {
                arrayList.add(rs2.getInt("notdone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
