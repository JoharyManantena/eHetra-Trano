/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import utilitaire.UtilDB;


public class RechercheHistoImpots {
    private int annees;
    private int nbMois;
    private int id_maison;
    private double prix_mois;
    private String nom_mois;

    public int getAnnees() {
        return annees;
    }

    public void setAnnees(int annees) {
        this.annees = annees;
    }

    public int getNbMois() {
        return nbMois;
    }

    public void setNbMois(int nbMois) {
        this.nbMois = nbMois;
    }

    public int getId_maison() {
        return id_maison;
    }

    public void setId_maison(int id_maison) {
        this.id_maison = id_maison;
    }

    public double getPrix_mois() {
        return prix_mois;
    }

    public void setPrix_mois(double prix_mois) {
        this.prix_mois = prix_mois;
    }

    public String getNom_mois() {
        return nom_mois;
    }

    public void setNom_mois(String nom_mois) {
        this.nom_mois = nom_mois;
    }
    

    public RechercheHistoImpots() {
    }
    
    public static Vector<RechercheHistoImpots> getImpotsMaison(int annees, int idMaison) {
        Vector<RechercheHistoImpots> result = new Vector<>();
        String[] lesMois = {"Mois Invalide","Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aôut", "Septembre", "Octobre", "Novembre", "Decembre"};
        int nbMois = 0;
        
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL pour récupérer les données correspondantes
            String query = "SELECT SUM(NBMOISPAYER) AS Total_MoisPayer " +
                           "FROM HISTORIQUEIMPOTS h " +
                           "WHERE ANNEES = ? AND ID_MAISON = ? " +
                           "GROUP BY ID_MAISON, ANNEES";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Associer les paramètres de la requête
            stmt.setInt(1, annees);
            stmt.setInt(2, idMaison);

            // Exécuter la requête et parcourir les résultats
            ResultSet  rs = stmt.executeQuery();
            while (rs.next()) {
                nbMois = rs.getInt("Total_MoisPayer");
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des impôts dans  : " + e.getMessage());
            e.printStackTrace();
        }
        
        Maison maison = Maison.getById(idMaison);
        
        for (int i = 1; i <= nbMois; i++) {
            Facture fact = maison.facturer(annees, i);
            RechercheHistoImpots temp = new RechercheHistoImpots();
            temp.setId_maison(idMaison);
            temp.setAnnees(annees);
            temp.setNbMois(nbMois);
            temp.setNom_mois(lesMois[i]);
            temp.setPrix_mois(fact.getFactMere().getMontant());
            result.add(temp);
        }
        return result;
    }

}
