/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import bean.CGenUtil;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pompe.Pompe;
import utilitaire.UtilDB;

/**
 *
 * @author Belouh
 */
@WebServlet(name = "PompeServlet", urlPatterns = {"/PompeServlet"})
public class PompeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        try {
            Connection conn = new UtilDB().GetConn("gallois","gallois");
            Pompe[] pompe = (Pompe[]) CGenUtil.rechercher(new Pompe(),"select * from Pompe",conn);
            String json = new Gson().toJson(pompe);
            conn.close();
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (Exception e) {
            throw new ServletException("Erreur de base de données",e);
        }
    }
}
