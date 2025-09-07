package org.example.demo;


import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet ;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class Execl {


    private static Sheet sheet ;
    private static Workbook workbook ;
    public static Sheet getFileExeclSheet(FileInputStream fis) throws IOException {

        workbook = new XSSFWorkbook(fis);
        sheet  = workbook.getSheetAt(0);
        fis.close();

        return sheet;
    }


    public static void save(FileOutputStream fos) throws IOException, SQLException {
        workbook.write(fos);
        fos.close();
        OracleDatabase.closeConnection();
    }
    public static void close() throws IOException {
        workbook.close();
    }
}