package org.jan.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDataBase {
    private static String url = "jdbc:mysql://localhost:3306/jugueteria_jdbc";
    private static String username = "root";
    private static String password = "janka";
    private static Connection connection;
    public static  Connection getInstance() throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }
}
