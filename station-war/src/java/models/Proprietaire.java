/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utilitaire.UtilDB;


public class Proprietaire {
    private int id;
    private String nom;
    private int id_maison;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId_maison() {
        return id_maison;
    }

    public void setId_maison(int id_maison) {
        this.id_maison = id_maison;
    }

    public Proprietaire() {
    }
    
    public static Proprietaire getProp(int id_maison){
        Proprietaire valiny = new Proprietaire();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL avec SDO_CONTAINS et transformation de ZONE en WKT
            String query ="SELECT ID,nom AS NOM_PROPRIO,id_maison " +
                "FROM PROPRIETAIRE p " +
                "WHERE  id_maison = ? ";

            // Préparer la requête
            PreparedStatement stmt = conn.prepareStatement(query);

            // Passer les paramètres log et lat
            stmt.setInt(1, id_maison);

            // Exécuter la requête
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                valiny.setId(rs.getInt("ID"));
                valiny.setNom(rs.getString("NOM_PROPRIO"));
                valiny.setId_maison(id_maison);
            } else {
                System.out.println("Aucune proprietaire trouvée pour le maison "+id_maison);
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de la proprietaire dans Proprietaire: " + e.getMessage());
            e.printStackTrace();
        }
        return valiny;
    }
    
}
