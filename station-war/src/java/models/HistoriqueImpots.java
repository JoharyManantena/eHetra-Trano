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


public class HistoriqueImpots {
    private int id;
    private int annees;
    private int nbMois;
    private int id_maison;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public HistoriqueImpots() {
    }
    
    public HistoriqueImpots save() {
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL pour insérer les données dans la table
            String query = "INSERT INTO HISTORIQUEIMPOTS (ID,ANNEES, NBMOISPAYER, ID_MAISON) VALUES (HISTOIMPOTSSEQ.NEXTVAL,?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Associer les paramètres de la requête avec les attributs de l'objet
            stmt.setInt(1, this.getAnnees());
            stmt.setInt(2, this.getNbMois());
            stmt.setInt(3, this.getId_maison());

            // Exécuter la requête
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("HistoriqueImpots insérée avec succès !");
            }

            // Fermer la connexion
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de l'insertion de la HistoriqueImpots : " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }

    public static Vector<HistoriqueImpots> getImpotsMaison(int annees, int idMaison) {
        Vector<HistoriqueImpots> valiny = new Vector<>();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL pour récupérer les données correspondantes
            String query = "SELECT ID, ANNEES, NBMOISPAYER, ID_MAISON FROM HISTORIQUEIMPOTS WHERE ANNEES = ? AND ID_MAISON = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Associer les paramètres de la requête
            stmt.setInt(1, annees);
            stmt.setInt(2, idMaison);

            // Exécuter la requête et parcourir les résultats
            ResultSet  rs = stmt.executeQuery();
            while (rs.next()) {
                HistoriqueImpots impots = new HistoriqueImpots();

                // Assigner les valeurs récupérées aux attributs de l'objet
                impots.setId(rs.getInt("ID"));
                impots.setAnnees(rs.getInt("ANNEES"));
                impots.setNbMois(rs.getInt("NBMOISPAYER"));
                impots.setId_maison(rs.getInt("ID_MAISON"));

                // Ajouter l'objet à la liste
                valiny.add(impots);
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des impôts : " + e.getMessage());
            e.printStackTrace();
        }

        return valiny;
    }

    public static int etatPayer(int mois,int id_maison,int annees){
        int valiny = 0;
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
            stmt.setInt(2, id_maison);

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
            System.out.println("Erreur lors de la récupération des impôts : " + e.getMessage());
            e.printStackTrace();
        }
        if(mois <= nbMois){
            valiny = 1;
        }
        return valiny;
    }
}
