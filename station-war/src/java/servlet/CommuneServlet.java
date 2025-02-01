/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Commune;


@MultipartConfig
@WebServlet(name = "CommuneServlet", urlPatterns = {"/CommuneServlet"})
public class CommuneServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        // Exemple de récupération des arrondissements depuis la base de données
        Vector<Commune> commune = Commune.getAll();
        
        // Convertir la liste d'arrondissements en JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(commune);
        
        // Répondre avec le JSON
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
