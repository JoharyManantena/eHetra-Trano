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
import models.Facture;
import models.Maison;
import models.RechercheArrondissement;


@MultipartConfig
@WebServlet(name = "CalculerFactureServlet", urlPatterns = {"/CalculerFactureServlet"})
public class CalculerFactureServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        Vector<Facture> listeFacture= new Vector<Facture>();
        int moisFacture = Integer.parseInt(request.getParameter("moisFacture"));
        int anneeFacture = Integer.parseInt(request.getParameter("anneeFacture"));
        int idCommune =  Integer.parseInt(request.getParameter("idCom"));
        Vector<Maison> listeMaison = Commune.getMaisons(idCommune);
        
        for (int i = 0; i < listeMaison.size(); i++) {
            Maison maison = listeMaison.elementAt(i);
            listeFacture.add(maison.facturer(anneeFacture, moisFacture));
        }
        // Convertir la liste d'arrondissements en JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(listeFacture);
        
        // Répondre avec le JSON
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
