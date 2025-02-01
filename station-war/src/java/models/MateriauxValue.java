/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utilitaire.UtilDB;


public class MateriauxValue {
    private int id;
    private int mois;
    private int annees;
    private double coefficient;
    private int id_caracteristique;
    private int id_commune;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMois() {
        return mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public int getAnnees() {
        return annees;
    }

    public void setAnnees(int annees) {
        this.annees = annees;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public int getId_caracteristique() {
        return id_caracteristique;
    }

    public void setId_caracteristique(int id_caracteristique) {
        this.id_caracteristique = id_caracteristique;
    }

    public int getId_commune() {
        return id_commune;
    }

    public void setId_commune(int id_commune) {
        this.id_commune = id_commune;
    }

    public MateriauxValue() {
    }
    
    public static MateriauxValue getMateriel(int mois, int annees, int id_commune, int id_caracteristique) {
        MateriauxValue valiny = new MateriauxValue();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL pour récupérer le matériel correspondant
            String query = "SELECT ID, MOIS, ANNEES, COEFFICIENT, ID_CARACTERISTIQUE, ID_COMMUNE " +
                           "FROM MATERIAUXVALUE " +
                           "WHERE MOIS = ? AND ANNEES = ? AND ID_COMMUNE = ? AND ID_CARACTERISTIQUE = ?";

            // Préparer la requête
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, mois);
            stmt.setInt(2, annees);
            stmt.setInt(3, id_commune);
            stmt.setInt(4, id_caracteristique);

            // Exécuter la requête
            ResultSet rs = stmt.executeQuery();

            // Parcourir le résultat
            if (rs.next()) {
                valiny.setId(rs.getInt("ID"));
                valiny.setMois(rs.getInt("MOIS"));
                valiny.setAnnees(rs.getInt("ANNEES"));
                valiny.setCoefficient(rs.getDouble("COEFFICIENT"));
                valiny.setId_caracteristique(rs.getInt("ID_CARACTERISTIQUE"));
                valiny.setId_commune(rs.getInt("ID_COMMUNE"));
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du matériel : " + e.getMessage());
            e.printStackTrace();
        }

        return valiny;
    }


}
