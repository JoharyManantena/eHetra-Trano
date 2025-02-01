/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.VenteData;
import stock.MvtStock;
import utilitaire.UtilDB;
import vente.Vente;

/**
 *
 * @author Belouh
 */
@WebServlet(name = "VenteServlet", urlPatterns = {"/VenteServlet"})
public class VenteServlet extends HttpServlet {

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
        
        try {
            Connection conn = new UtilDB().GetConn("gallois","gallois");
            Vente vente = venteData.genererVente();
            vente.createObjectMultiple("1060", conn);
            vente.createMvtStockSortie("1060", conn);
            vente.validerObject("1060", conn);
            vente.payer("1060", conn);
            vente.payerObject("1060", conn);
            conn.close();
        } catch (Exception ex) {
            Logger.getLogger(VenteServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
