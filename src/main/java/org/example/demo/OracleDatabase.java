package org.example.demo;

import java.sql.*;

public class OracleDatabase {


        private static String url = "jdbc:oracle:thin:@WAH_PRD" ;
        private static String username = "wahrep_104734" ;
        private static String password = "19251666@moonman";
        private static Connection con = null ;

        public static Connection getConnection () throws ClassNotFoundException, SQLException {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("the class is good ");

            con = DriverManager.getConnection(url,username, password);
            System.out.println("the connection was successfully. . .");

            return con ;
        }
        public static void closeConnection() throws SQLException {
            con.close();
        }


}
