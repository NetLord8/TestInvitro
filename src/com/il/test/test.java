package com.il.test;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Created by ulmasov_im on 06.06.2016.
 */
@WebServlet(name = "test" , urlPatterns = "/test")
public class test extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List items = upload.parseRequest(request);
                Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        System.out.println("filename:- " + fileName);
                        //FileInputStream file = new FileInputStream(new File("D:\\temp\\Onni.xlsm"));


                        XSSFWorkbook workbook = new XSSFWorkbook(item.getInputStream());
                        XSSFSheet sheet = workbook.getSheetAt(0);
                        Iterator<Row> rows = sheet.iterator();
                        PrintWriter out=response.getWriter();

                        while(rows.hasNext())
                        {
                            Row row=rows.next();
                            Cell cell=row.getCell(1);
                            int cellType = cell.getCellType();
                            switch (cellType) {
                                case Cell.CELL_TYPE_STRING:
                                    System.out.print(cell.getStringCellValue() + "=");
                                    out.println(cell.getStringCellValue() + "=");
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    System.out.print("[" + cell.getNumericCellValue() + "]");
                                    out.println("[" + cell.getNumericCellValue() + "]");
                                    break;

                                case Cell.CELL_TYPE_FORMULA:
                                    System.out.print("[" + cell.getNumericCellValue() + "]");
                                    out.println("[" + cell.getNumericCellValue() + "]");
                                    break;
                                default:
                                    System.out.print("|");

                                    out.println("|");
                                    break;
                            }
                            //out.println(cell.getStringCellValue());
                        }
                        out.print("huy");
                        out.close();

                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/plain; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        FileInputStream file = new FileInputStream(new File("D:\\temp\\Onni.xlsm"));


        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        PrintWriter out=response.getWriter();
        //=response.getOutputStream();
        while(rows.hasNext())
        {
            Row row=rows.next();
            Cell cell=row.getCell(1);
            int cellType = cell.getCellType();
            switch (cellType) {
                case Cell.CELL_TYPE_STRING:
                    System.out.print(cell.getStringCellValue() + "=");
                    out.println(cell.getStringCellValue() + "=");
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    System.out.print("[" + cell.getNumericCellValue() + "]");
                    out.println("[" + cell.getNumericCellValue() + "]");
                    break;

                case Cell.CELL_TYPE_FORMULA:
                    System.out.print("[" + cell.getNumericCellValue() + "]");
                    out.println("[" + cell.getNumericCellValue() + "]");
                    break;
                default:
                    System.out.print("|");

                    out.println("|");
                    break;
            }
            //out.println(cell.getStringCellValue());
        }
        out.print("huy");
        out.close();
    }
}
