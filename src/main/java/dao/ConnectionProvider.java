package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    private static Connection con;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            ConnectionProvider.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/contacts", "root", "Root@12345");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws Exception {
        if (con == null) {
            throw new Exception("Cannot Get Connection\nThere is some problem in Connection Provider");
        }
        return con;
    }

}
