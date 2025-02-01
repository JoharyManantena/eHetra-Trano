/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import bean.CGenUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utilitaire.UtilDB;


public class ConfPrixMetreCarre {
    private int id;
    private double prix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public ConfPrixMetreCarre() {
    }
    
    public static ConfPrixMetreCarre getConf() {
        ConfPrixMetreCarre valiny = new ConfPrixMetreCarre();
        try {
            // Établir une connexion avec la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");
            String query = "SELECT * FROM CONFPRIXMETRECARRE WHERE id = 1";

            // Préparer et exécuter la requête
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Traiter le résultat
            if (rs.next()) {
                valiny.setId(rs.getInt("ID"));
                valiny.setPrix(rs.getDouble("PRIX"));
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur dans la fonction getConf() de la classe ConfPrixMetreCarre : " + e.getMessage());
        }
        return valiny;
    }
}
