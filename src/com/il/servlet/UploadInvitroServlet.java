package com.il.servlet;

import com.il.bean.InvitroRow;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import java.sql.*;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ulmasov_im on 07.06.2016.
 */
@WebServlet(name = "UploadInvitroServlet" , urlPatterns = "/upload")
public class UploadInvitroServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/plain; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        PrintWriter out = response.getWriter();
        String fileName="";
        ArrayList<InvitroRow> uploadList=null;
        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List items = upload.parseRequest(request);
                Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    if (!item.isFormField()) {
                        fileName = item.getName();
                        String ext=fileName.substring(fileName.lastIndexOf(".")+1);
                        Workbook workbook;
                        if(ext.length()==3)
                            workbook = new HSSFWorkbook(item.getInputStream());
                        else
                            workbook = new XSSFWorkbook(item.getInputStream());
                        uploadList=getUploadList(workbook);


                    }
                }
            } catch (FileUploadException e) {
                out.println("Ошибка "+e.getMessage()+"</br>");
            } catch (Exception e) {
                out.println("Ошибка "+e.getMessage()+"</br>");
            }
            try {
                Context initContext = new InitialContext();
                Context envContext = (Context) initContext.lookup("java:comp/env");
                DataSource ds = (DataSource) envContext.lookup("jdbc/medtest");
                Connection conn = ds.getConnection();

                Statement statement = conn.createStatement();
                String sql = "delete from rsr_bill_invitro";
                CallableStatement call=conn.prepareCall(sql);
                call.execute();
                out.println("Старые данные очищены</br>");
                PreparedStatement ps=conn.prepareStatement("INSERT INTO SOLUTION_MED.RSR_BILL_INVITRO ( DATE_TEST, NSS, INZ, FIO_PAT, CODE_TEST, TEXT_TEST, COUNT_TEST, PRICE_INVITRO) \n" +
                        "VALUES ( ?,?,?,?,?,?,?,?)");
                for (InvitroRow irow: uploadList) {
                    ps.setDate(1, java.sql.Date.valueOf(irow.getAnalyzDate()));
                    ps.setString(2,irow.getNss());
                    ps.setDouble(3,irow.getInz());
                    ps.setString(4,irow.getPatient());
                    ps.setString(5,irow.getCode());
                    ps.setString(6,irow.getAnalyz());
                    ps.setDouble(7,irow.getaCount());
                    ps.setDouble(8,irow.getPrice());
                    ps.execute();
                }
                out.println("Новые данные из файла <b>"+fileName+"</b> загружены</br>");
                ResultSet rs = statement.executeQuery("select count(rbi.inz) as count_test,sum(rbi.price_invitro) as sum_test from rsr_bill_invitro rbi");


                while (rs.next()) {
                    out.println("Загружено <b>"+rs.getInt(1) +"</b> анализов на общую сумму <b>" +rs.getDouble(2)+"</b></br>");

                }
                conn.close();

            } catch (NamingException e) {
                out.println("Ошибка "+e.getMessage()+"</br>");
            } catch (SQLException e) {
                out.println("Ошибка "+e.getMessage()+"</br>");
            }
            out.println("------------------------------------------------</br>");
            out.close();

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

                writer.println("Only POST requests allowed");

    }
    private ArrayList<InvitroRow> getUploadList(Workbook workbook){
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        int pos=0;
        ArrayList<InvitroRow> uploadList=new ArrayList<InvitroRow>();
        while (rows.hasNext()) {

            Row row = rows.next();
            pos++;

            if (pos<15)continue;

            Cell cell = row.getCell(8);


            if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) continue;
            InvitroRow irow=new InvitroRow();
            //добавляем дату из отчета
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate res = LocalDate.parse(row.getCell(1).getStringCellValue(), formatter);
            irow.setAnalyzDate(res);

            irow.setNss(row.getCell(7).getStringCellValue());

            irow.setInz(row.getCell(8).getNumericCellValue());

            irow.setPatient(row.getCell(9).getStringCellValue());

            //////////////////////////////////////////////////////////////////////////////

            switch (row.getCell(11).getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    irow.setCode(row.getCell(11).getStringCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    irow.setCode(""+((int)row.getCell(11).getNumericCellValue()));
                    break;
            }

            irow.setAnalyz(row.getCell(12).getStringCellValue());

            irow.setaCount(row.getCell(14).getNumericCellValue());

            irow.setPrice(row.getCell(15).getNumericCellValue());

            uploadList.add(irow);
        }

        return uploadList;
    }
}
