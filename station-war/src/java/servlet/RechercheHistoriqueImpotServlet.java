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
import models.RechercheHistoImpots;

/**
 *
 * @author Belouh
 */
@MultipartConfig
@WebServlet(name = "RechercheHistoriqueImpotServlet", urlPatterns = {"/RechercheHistoriqueImpotServlet"})
public class RechercheHistoriqueImpotServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            int idMaisonPayerListe = Integer.parseInt(request.getParameter("idMaisonPayerListe"));
            int anneeListe = Integer.parseInt(request.getParameter("anneeListe"));
            Vector<RechercheHistoImpots> liste = RechercheHistoImpots.getImpotsMaison(anneeListe, idMaisonPayerListe);
            String json = new Gson().toJson(liste);
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
 
        }  catch (Exception e) {
            System.out.println("Erreur dans le servlet HistoriqueImpotServlet (methode Get):" + e.getMessage());
            // En cas d'erreur, renvoyer une réponse JSON avec un statut d'erreur
            String errorResponse = "{\"status\":\"error\",\"message\":\"Une erreur s'est produite lors de l'affichage de liste de la historique impots.\"}";
            response.getWriter().write(errorResponse);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
