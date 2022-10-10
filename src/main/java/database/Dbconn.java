package database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

public class Dbconn {
    /*
    * Подключение к БД
    * */
    private static volatile Dbconn instance;
    private static Connection conn;
    private static Statement statmt;
    private ResultSet rst;

    private Dbconn() {
        //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }

    public static Dbconn getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            synchronized (Dbconn.class){
                if (instance == null) {
                    instance = new Dbconn();
                }
            }
        }
        return instance;
    }

    public ResultSet Recordset (String sql) throws SQLException {
        rst = statmt.executeQuery(sql);
        return rst;
    }

    private void setConn() {
        Context ctx;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/SSC-Data");
            conn = ds.getConnection();
            //conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkConn() throws SQLException {
        if (conn == null) {
            this.setConn();
        }else if (conn.isClosed()){
            conn = null;
            this.setConn();
        }
    }

    public void setStatmt () throws SQLException {
        statmt = conn.createStatement();
    }

    public boolean Exec (String sql) throws SQLException {
        return statmt.execute(sql);
    }

    public void closeRecordSet() throws SQLException {
        rst.close();
    }

    public void closeStatement() throws SQLException {
        statmt.close();
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }
}