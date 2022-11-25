package Untility;

import static Untility.DBContext.getConnection;
import java.sql.*;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnectionCuaVo {

    private static final String USERNAME = "sa";
    private static final String PASSWORD = "06122003";
    private static final String SERVER = "NGUYENVO0612\\SQLEXPRESS";
    private static final String PORT = "1433";
    private static final String DATABASE_NAME = "DUAN1";
    private static final boolean USING_SSL = true;

    private static String CONNECT_STRING;

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            StringBuilder connectStringBuilder = new StringBuilder();
            connectStringBuilder.append("jdbc:sqlserver://")
                    .append(SERVER).append(":").append(PORT).append(";")
                    .append("databaseName=").append(DATABASE_NAME).append(";")
                    .append("user=").append(USERNAME).append(";")
                    .append("password=").append(PASSWORD).append(";");
            if (USING_SSL) {
                connectStringBuilder.append("encrypt=true;trustServerCertificate=true;");
            }
            CONNECT_STRING = connectStringBuilder.toString();
            System.out.println("Connect String có dạng: " + CONNECT_STRING);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnectionCuaVo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(CONNECT_STRING);
    }

    private Connection _connection;
    private PreparedStatement _preparedStatement;
    private ResultSet _resultSet;

    public ResultSet executeQuery(String sql, Object... args) {
        try {
            _connection = getConnection();
            _preparedStatement = _connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                _preparedStatement.setObject(i + 1, args[i]);
            }
            _resultSet = _preparedStatement.executeQuery();

        } catch (Exception ex) {
            System.out.println("Lỗi truy vấn câu lệnh: " + sql);
        }
        return _resultSet;
    }

    public Integer executeUpdate(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedstatement = null;
        Integer affectedRows = null;

        try {
            connection = getConnection();
            preparedstatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedstatement.setObject(i + 1, args[i]);
            }
            affectedRows = preparedstatement.executeUpdate();

        } catch (Exception ex) {
            System.out.println("Lỗi thực thi câu lệnh: " + sql);
            ex.printStackTrace();
        }
        return affectedRows;
    }

  public static void main(String[] args) throws Exception {
        Connection conn = getConnection();
        DatabaseMetaData dbmt = conn.getMetaData();
        System.out.println(dbmt.getDriverName());
        System.out.println(dbmt.getDatabaseProductName());
        System.out.println(dbmt.getDatabaseProductVersion());
    }

}
