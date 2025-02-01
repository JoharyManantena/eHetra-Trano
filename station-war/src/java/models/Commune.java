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


public class Commune {
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

    public Commune() {
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
    public static Vector<Commune> getAll() {
        Vector<Commune> valiny = new Vector<Commune>();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL pour récupérer toutes les lignes de la table ARRONDISSEMENT
            String query = "SELECT ID, NOM, SDO_UTIL.TO_WKTGEOMETRY(ZONE) AS ZONE_WKT FROM COMMUNE";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Exécuter la requête
            ResultSet rs = stmt.executeQuery();

            // Parcourir les résultats
            while (rs.next()) {
                Commune commune = new Commune();
                commune.setId(rs.getInt("ID"));
                commune.setNom(rs.getString("NOM"));
                commune.setZone(clobToString(rs.getClob("ZONE_WKT")));

                // Ajouter l'objet Arrondissement à la liste
                valiny.add(commune);
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des commune : " + e.getMessage());
            e.printStackTrace();
        }
        return valiny;
    }

    public static Vector<Maison> getMaisons(int idCommune) {
        Vector<Maison> valiny = new Vector<>();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL pour récupérer les maisons dans une commune basée sur la géométrie
            String query =
                "SELECT m.ID, m.NOM, m.LONGUEUR, m.LARGEUR, m.NB_ETAGE, m.LOG, m.LAT, m.IMPOTS, m.TOIT , m.MUR  " +
                "FROM MAISON m, COMMUNE c " +
                "WHERE c.ID = ? " +
                "AND SDO_CONTAINS(c.ZONE, SDO_GEOMETRY(2001, 4326, SDO_POINT_TYPE(m.LOG, m.LAT, NULL), NULL, NULL)) = 'TRUE'";

            PreparedStatement stmt = conn.prepareStatement(query);

            // Définir le paramètre
            stmt.setInt(1, idCommune);

            // Exécuter la requête
            ResultSet rs = stmt.executeQuery();

            // Parcourir les résultats
            while (rs.next()) {
                Maison maison = new Maison();
                maison.setId(rs.getInt("ID"));
                maison.setNom(rs.getString("NOM"));
                maison.setLongueur(rs.getDouble("LONGUEUR"));
                maison.setLargeur(rs.getDouble("LARGEUR"));
                maison.setNb_etage(rs.getInt("NB_ETAGE"));
                maison.setLog(rs.getDouble("LOG"));
                maison.setLat(rs.getDouble("LAT"));
                maison.setImpots(rs.getDouble("IMPOTS"));
                maison.setToit(rs.getInt("TOIT"));
                maison.setMur(rs.getInt("MUR"));
                valiny.add(maison);
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des listes des maisons : " + e.getMessage());
            e.printStackTrace();
        }
        return valiny;
    }


}
