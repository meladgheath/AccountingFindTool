package org.example;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {


        Workbook workbook = null;
        FileOutputStream fos = null ;

        try {
            FileInputStream fileInputStream = new FileInputStream("/home/melad/melo.xlsx");
              workbook = new XSSFWorkbook(fileInputStream   );
                fileInputStream.close();


            Sheet sheet = workbook.getSheetAt(0);

            int count = sheet.getLastRowNum() + 1 ;

            int start = 1 ;

            for (int i = start ; i < count ; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(5);

               cell.setCellType(CellType.STRING);
               if (cell.toString().length() == 15)
                System.out.println(cell + "   "+cell.toString().length());
               if (cell.toString().trim().length() == 14)
                   System.out.println("0"+cell);
               Cell c = row.createCell(8);
               c.setCellValue("checked here . . . ");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            fos = new FileOutputStream("/home/melad/melo.xlsx") ;
            workbook.write(fos);
            fos.close();
            workbook.close();
        }
    }
}