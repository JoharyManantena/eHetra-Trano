/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.VenteData;
import utilitaire.UtilDB;
import avoir.AvoirFC;
import avoir.AvoirFCFille;
import vente.Vente;

/**
 *
 * @author Belouh
 */
@WebServlet(name = "AnnulerVenteServlet", urlPatterns = {"/AnnulerVenteServlet"})
public class AnnulerVenteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // Convertir le contenu JSON en chaîne de caractères
        String jsonString = jsonBuffer.toString();
        Gson gson = new Gson();
        VenteData venteData = gson.fromJson(jsonString, VenteData.class);
        venteData.setClientId("CLI000044");
        try {
            Connection conn = new UtilDB().GetConn("gallois","gallois");
            Vente vente = venteData.genererVente();
            AvoirFC avoire = Vente.transformerFactureToAvoir(vente);
            for (int i = 0; i < vente.getVenteDetails().length; i++) {
                AvoirFCFille avoireFille = Vente.transformerFactureToAvoirFille(vente.getVenteDetails()[i]);
                avoireFille.createObject("1060", conn);
            }
            avoire.createObject("1060", conn);
            conn.close();
        } catch (Exception ex) {
            Logger.getLogger(VenteServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
