package co.istad.jdbc.project.config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {


    private static Connection conn;


    public static Connection getInstance() {
        return conn;
    }

    public static void init() {
        if (conn == null) {
            try {

                Class.forName("org.postgresql.Driver");


                String url = "jdbc:postgresql://localhost:5432/postgres";
                String user = "postgres";
                String password = "lily123";

                conn = DriverManager.getConnection(url, user, password);

            } catch (ClassNotFoundException e) {
                System.out.println("class not found: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("SQL error: " + e.getMessage());
            }
        } else {
            System.out.println("Connection already initialized");
        }
    }


}

