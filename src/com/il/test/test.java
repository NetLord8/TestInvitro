package com.il.test;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

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


                        Workbook workbook = new XSSFWorkbook(item.getInputStream());
                        Sheet sheet = workbook.getSheetAt(0);
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
        PrintWriter writer = response.getWriter();
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/medtest");
            Connection conn = ds.getConnection();

            Statement statement = conn.createStatement();
            String sql = "select sysdate+1 as s from dual";
            ResultSet rs = statement.executeQuery(sql);

            int count = 1;
            while (rs.next()) {
                writer.println(rs.getDate("s"));

            }
        } catch (NamingException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
