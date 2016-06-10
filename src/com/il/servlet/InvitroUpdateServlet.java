package com.il.servlet;

import com.il.bean.InvitroRow;
import com.il.util.UIFormatter;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.oracore.OracleType;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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

/**
 * Created by ulmasov_im on 09.06.2016.
 */
@WebServlet(name = "InvitroUpdateServlet", urlPatterns = "/updateinvitro")
public class InvitroUpdateServlet extends HttpServlet  {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/plain; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        Connection conn=null;
        PrintWriter out = response.getWriter();
       String mc=request.getParameter("mc");
        String to=request.getParameter("to");
        String from=request.getParameter("from");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dateFrom = LocalDate.parse(from, formatter);
        LocalDate dateTo = LocalDate.parse(to, formatter);
        int imc=Integer.parseInt(mc);

        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/medtest");
            conn = ds.getConnection();

            Statement statement = conn.createStatement();
            String sql = "call proc_zalivka_invitro(?,?,?,?)";
            CallableStatement call=conn.prepareCall(sql);
            call.setDate("bill_dat_from",java.sql.Date.valueOf(dateFrom));
            call.setDate("bill_dat_to",java.sql.Date.valueOf(dateFrom));
            call.setInt("bill_dep",imc);
            call.registerOutParameter("rc1", OracleTypes.CURSOR);
            call.execute();
            out.println(UIFormatter.getHighlightMessage("Платежи за период с "+from+" по "+to+"привязаны"));



        }
        catch (Exception e) {
            out.println(UIFormatter.getErrorMessage("Ошибка "+e.getMessage()));
        }
        finally {
            try {
                conn.close();
            } catch (SQLException e) {
                out.println(UIFormatter.getErrorMessage("Ошибка "+e.getMessage()));
            }
        }

        out.close();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
