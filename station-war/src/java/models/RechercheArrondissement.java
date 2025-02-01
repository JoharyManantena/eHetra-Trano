/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.util.Vector;
import utilitaire.UtilDB;


public class RechercheArrondissement {
    private int id;
    private String nomArrondissement;
    private int annees;
    private int nbMaison;
    private double sommeNonPayer;
    private double sommePayer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomArrondissement() {
        return nomArrondissement;
    }

    public void setNomArrondissement(String nomArrondissement) {
        this.nomArrondissement = nomArrondissement;
    }

    public int getAnnees() {
        return annees;
    }

    public void setAnnees(int annees) {
        this.annees = annees;
    }

    public int getNbMaison() {
        return nbMaison;
    }

    public void setNbMaison(int nbMaison) {
        this.nbMaison = nbMaison;
    }

    public double getSommeNonPayer() {
        return sommeNonPayer;
    }

    public void setSommeNonPayer(double sommeNonPayer) {
        this.sommeNonPayer = sommeNonPayer;
    }

    public double getSommePayer() {
        return sommePayer;
    }

    public void setSommePayer(double sommePayer) {
        this.sommePayer = sommePayer;
    }

    public RechercheArrondissement() {
    }
    
    public static Vector<RechercheArrondissement> filtreParAnnees(int annees) {
        Vector<RechercheArrondissement> valiny = new Vector<RechercheArrondissement>();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête mise à jour pour récupérer les arrondissements, maisons et impôts
            String query = "SELECT " +
                           "a.id AS arrondissement_id, " +
                           "a.nom AS arrondissement_nom, " +
                           "COUNT(m.id) AS nbMaison, " +
                           "SUM(h.nbMoisPayer * m.impots) AS sommePayer, " +
                           "SUM((12 - h.nbMoisPayer) * m.impots) AS sommeNonPayer " +
                           "FROM Arrondissement a " +
                           "JOIN Maison m " +
                           "  ON SDO_RELATE(a.zone, " +
                           "    SDO_GEOMETRY(2001, 4326, SDO_POINT_TYPE(m.log, m.lat, NULL), NULL, NULL), " +
                           "    'mask=ANYINTERACT') = 'TRUE' " +
                           "LEFT JOIN ( " +
                           "  SELECT id_maison, " +
                           "         SUM(nbMoisPayer) AS nbMoisPayer " +
                           "  FROM HistoriqueImpots " +
                           "  WHERE annees = ? " +
                           "  GROUP BY id_maison " +
                           ") h ON m.id = h.id_maison " +
                           "GROUP BY a.id, a.nom";

            // Préparer la requête
            java.sql.PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, annees); // Remplacement de l'année souhaitée

            // Exécuter la requête
            java.sql.ResultSet rs = stmt.executeQuery();

            // Traiter les résultats
            while (rs.next()) {
                RechercheArrondissement arrondissement = new RechercheArrondissement();
                arrondissement.setId(rs.getInt("arrondissement_id"));
                arrondissement.setNomArrondissement(rs.getString("arrondissement_nom"));
                arrondissement.setAnnees(annees);
                arrondissement.setNbMaison(rs.getInt("nbMaison"));
                arrondissement.setSommePayer(rs.getDouble("sommePayer"));
                arrondissement.setSommeNonPayer(rs.getDouble("sommeNonPayer"));

                // Ajouter à la liste
                valiny.add(arrondissement);
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Erreur lors du filtrage des arrondissements : " + e.getMessage());
            e.printStackTrace();
        }
        return valiny;
    }

}
