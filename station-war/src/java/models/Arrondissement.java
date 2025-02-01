/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import utilitaire.UtilDB;

public class Arrondissement {
    private int id;
    private String nom;
    private String zone;

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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Arrondissement() {
    }
    
    public static String clobToString(Clob clob) {
        StringBuilder sb = new StringBuilder();
        try (Reader reader = clob.getCharacterStream();
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    
    // Méthode pour récupérer tous les arrondissements
    public static Vector<Arrondissement> getAll() {
        Vector<Arrondissement> valiny = new Vector<Arrondissement>();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL pour récupérer toutes les lignes de la table ARRONDISSEMENT
            String query = "SELECT ID, NOM, SDO_UTIL.TO_WKTGEOMETRY(ZONE) AS ZONE_WKT FROM ARRONDISSEMENT";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Exécuter la requête
            ResultSet rs = stmt.executeQuery();

            // Parcourir les résultats
            while (rs.next()) {
                Arrondissement arrondissement = new Arrondissement();
                arrondissement.setId(rs.getInt("ID"));
                arrondissement.setNom(rs.getString("NOM"));
                arrondissement.setZone(clobToString(rs.getClob("ZONE_WKT")));

                // Ajouter l'objet Arrondissement à la liste
                valiny.add(arrondissement);
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des arrondissements : " + e.getMessage());
            e.printStackTrace();
        }
        return valiny;
    }
}
