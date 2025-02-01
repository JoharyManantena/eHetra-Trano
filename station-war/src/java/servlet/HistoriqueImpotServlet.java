/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.HistoriqueImpots;

/**
 *
 * @author Belouh
 */
@MultipartConfig
@WebServlet(name = "HistoriqueImpotServlet", urlPatterns = {"/HistoriqueImpotServlet"})
public class HistoriqueImpotServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            int idMaisonImpots = Integer.parseInt(request.getParameter("idMaisonImpots"));
            int anneeImpots = Integer.parseInt(request.getParameter("anneeImpots"));
            int nbMoisImpots = Integer.parseInt(request.getParameter("nbMoisImpots"));
            
            HistoriqueImpots histo = new HistoriqueImpots();
            histo.setAnnees(anneeImpots);
            histo.setNbMois(nbMoisImpots);
            histo.setId_maison(idMaisonImpots);
            histo.save();
            
            String jsonResponse = "{\"status\":\"success\"}";
            // Envoyer la réponse JSON à l'Ajax
            response.getWriter().write(jsonResponse);
        }  catch (Exception e) {
            System.out.println("Erreur dans le servlet HistoriqueImpotServlet (methode Post):" + e.getMessage());
            // En cas d'erreur, renvoyer une réponse JSON avec un statut d'erreur
            String errorResponse = "{\"status\":\"error\",\"message\":\"Une erreur s'est produite lors de l'enregistrement de la historique impots.\"}";
            response.getWriter().write(errorResponse);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
