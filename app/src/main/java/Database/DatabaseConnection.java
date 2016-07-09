package Database;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Thanh Huy on 7/1/2016.
 */
public class DatabaseConnection {
    private String ip, db, un, pass;
    private Connection connection;

    public DatabaseConnection() {
        //ip = "135.234.238.136";
        ip = "cmu.vanlanguni.edu.vn";
        db = "huyle32";
        un = "huyle32";
        pass = "123456";
    }

    @SuppressLint("NewAPI")
    public void Connect() throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connection = null;
        String S_url = null;
        String driver = null;
        try {
            driver = "net.sourceforge.jtds.jdbc.Driver";
            Class.forName(driver).newInstance();
            S_url = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + pass + ";";
            connection = (Connection) DriverManager.getConnection(S_url);
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
            throw new Exception("Khong tim thay Class");
        } catch (SQLException e) {
            throw new Exception("Khong tim thay SQL");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

