package org.example;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {


        Scanner scanner = new Scanner(System.in);


        Workbook workbook = null;
        FileOutputStream fos = null ;
        System.out.println("Enter the Path of file : ");
        String filePath = scanner.next();



        String URL = "jdbc:oracle:thin:@10.201.20.33:1521:WAHPRD";
        String USERNAME = "wahrep_104734";
        String PASSWORD = "19251666@moonman";


        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to Oracle database successfully!");

            statement = connection.createStatement();


            FileInputStream fileInputStream = new FileInputStream(filePath);
              workbook = new XSSFWorkbook(fileInputStream   );
                fileInputStream.close();


            Sheet sheet = workbook.getSheetAt(0);

            int count = sheet.getLastRowNum() + 1 ;

            int start = 1 ;

            for (int i = start ; i < count ; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(5);

               cell.setCellType(CellType.STRING);
               if (cell.toString().length() == 15){
                   String sqlQuery = "SELECT * FROM fcubsprd.sttm_cust_account where cust_ac_no = '"+cell.toString()+"'";
                   resultSet = statement.executeQuery(sqlQuery);

                   if (resultSet.next()) {
                       Cell c = row.createCell(8) ;
                       c.setCellValue(resultSet.getString("cust_ac_no"));
                   }

               }
               if (cell.toString().trim().length() == 14)
                System.out.println("0"+cell);
                String sqlQuery = "SELECT * FROM fcubsprd.sttm_cust_account where cust_ac_no = '"+cell.toString()+"'";
                resultSet = statement.executeQuery(sqlQuery);

                if (resultSet.next()) {
                    Cell c = row.createCell(8) ;
                    c.setCellValue(resultSet.getString("cust_ac_no"));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            if (workbook != null) {
                fos = new FileOutputStream(filePath);
                workbook.write(fos);
                fos.close();
                workbook.close();
                System.out.println("successfully fine . . . ");
            }
            else
                System.out.println("there problems is was there ");
        }
    }
}