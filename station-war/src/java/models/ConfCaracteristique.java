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


public class ConfCaracteristique {
    private int id;
    private String typeParametre ;
    private String nomCaracteristique ;
    private double coefficient ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeParametre() {
        return typeParametre;
    }

    public void setTypeParametre(String typeParametre) {
        this.typeParametre = typeParametre;
    }

    public String getNomCaracteristique() {
        return nomCaracteristique;
    }

    public void setNomCaracteristique(String nomCaracteristique) {
        this.nomCaracteristique = nomCaracteristique;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public ConfCaracteristique() {
    }

    public static Vector<ConfCaracteristique> getConf(String typeparametre) {
        Vector<ConfCaracteristique> valiny = new Vector<>();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");
            String query = "SELECT * FROM CONFCARACTERISTIQUE c  WHERE TYPEPARAMETRE = ?";

            // Préparer et exécuter la requête
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, typeparametre);
            ResultSet rs = stmt.executeQuery();

            // Parcourir les résultats et remplir le vecteur
            while (rs.next()) {
                ConfCaracteristique conf = new ConfCaracteristique();
                conf.setId(rs.getInt("ID"));
                conf.setTypeParametre(rs.getString("TYPEPARAMETRE"));
                conf.setNomCaracteristique(rs.getString("NOMCARACTERISTIQUE"));
                conf.setCoefficient(rs.getDouble("COEFFICIENT"));
                valiny.add(conf);
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur dans la fonction getConf() de la classe ConfCaracteristique : " + e.getMessage());
            e.printStackTrace();
        }
        return valiny;
    }
    
    public static ConfCaracteristique getCaracteristique(String caracteristique) {
        ConfCaracteristique valiny = new ConfCaracteristique();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");
            String query = "SELECT * FROM CONFCARACTERISTIQUE c  WHERE NOMCARACTERISTIQUE = ?";

            // Préparer et exécuter la requête
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, caracteristique);
            ResultSet rs = stmt.executeQuery();

            // Parcourir les résultats et remplir le vecteur
            while (rs.next()) {
                valiny.setId(rs.getInt("ID"));
                valiny.setTypeParametre(rs.getString("TYPEPARAMETRE"));
                valiny.setNomCaracteristique(rs.getString("NOMCARACTERISTIQUE"));
                valiny.setCoefficient(rs.getDouble("COEFFICIENT"));
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur dans la fonction getConf() de la classe ConfCaracteristique : " + e.getMessage());
            e.printStackTrace();
        }
        return valiny;
    }

    
}
