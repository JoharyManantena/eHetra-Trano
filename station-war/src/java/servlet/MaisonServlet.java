/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.ConfCaracteristique;
import models.ConfPrixMetreCarre;
import models.Maison;

/**
 *
 * @author Belouh
 */
@MultipartConfig
@WebServlet(name = "MaisonServlet", urlPatterns = {"/MaisonServlet"})
public class MaisonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String nom = request.getParameter("nom");
            double longueur = Double.parseDouble(request.getParameter("longueur"));
            double largeur = Double.parseDouble(request.getParameter("largeur"));
            int nbEtage = Integer.parseInt(request.getParameter("nbEtage"));
            String mur = request.getParameter("mur");
            String toit = request.getParameter("toit");
            double latitude = Double.parseDouble(request.getParameter("latitude"));
            double longitude = Double.parseDouble(request.getParameter("longitude"));
            
            //Ajout des autres caracteristique
            ConfCaracteristique confMur = ConfCaracteristique.getCaracteristique(mur);
            ConfCaracteristique conftoit = ConfCaracteristique.getCaracteristique(toit);
            ConfPrixMetreCarre prixMetreCarre = ConfPrixMetreCarre.getConf();
            

            // Instancier et initialiser l'objet Maison
            Maison maison = new Maison();
            maison.setNom(nom);
            maison.setLongueur(longueur);
            maison.setLargeur(largeur);
            maison.setNb_etage(nbEtage);
            maison.setLat(latitude);
            maison.setLog(longitude);
            maison.setImpots(0);
            maison.setMur(confMur.getId());
            maison.setToit(conftoit.getId());
            
            
            maison.save();
            
            String jsonResponse = String.format(
                Locale.US,
                "{\"status\":\"success\",\"nom\":\"%s\",\"longueur\":%.2f,\"largeur\":%.2f,\"nb_etage\":%d,\"log\":%.6f,\"lat\":%.6f}",
                maison.getNom(), maison.getLongueur(), maison.getLargeur(),
                maison.getNb_etage(), maison.getLog(), maison.getLat()
            );

            // Envoyer la réponse JSON à l'Ajax
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            System.out.println("Erreur dans le servlet MaisonServlet :" + e.getMessage());
            // En cas d'erreur, renvoyer une réponse JSON avec un statut d'erreur
            String errorResponse = "{\"status\":\"error\",\"message\":\"Une erreur s'est produite lors de l'enregistrement de la maison.\"}";
            response.getWriter().write(errorResponse);
        }
    }

    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
