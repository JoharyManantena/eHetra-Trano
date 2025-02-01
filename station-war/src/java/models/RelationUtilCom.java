/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utilitaire.UtilDB;


public class RelationUtilCom {
    private int id;
    private int refuser;
    private int idcommune;
    private String nomCommune;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRefuser() {
        return refuser;
    }

    public void setRefuser(int refuser) {
        this.refuser = refuser;
    }

    public int getIdcommune() {
        return idcommune;
    }

    public void setIdcommune(int idcommune) {
        this.idcommune = idcommune;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public RelationUtilCom() {
    }
    
    public static RelationUtilCom getCommune(int refuser){
        RelationUtilCom valiny = new RelationUtilCom();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL avec SDO_CONTAINS et transformation de ZONE en WKT
            String query =
                    "SELECT r.ID AS ID, r.IDCOMMUNE as ID_COMMUNE,r.REFUSER AS REFUSER,c.NOM AS NOM_COMMUNE " +
                    "FROM RELATIONUTILCOM r " +
                    "JOIN COMMUNE c ON c.id = r.IDCOMMUNE " +
                    "WHERE REFUSER = ?";

            // Préparer la requête
            PreparedStatement stmt = conn.prepareStatement(query);

            // Passer les paramètres log et lat
            stmt.setInt(1, refuser);

            // Exécuter la requête
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                valiny.setId(rs.getInt("ID"));
                valiny.setIdcommune(rs.getInt("ID_COMMUNE"));
                valiny.setNomCommune(rs.getString("NOM_COMMUNE"));
                valiny.setRefuser(refuser);
            } else {
                System.out.println("Aucune commune trouvée pour le refuser "+refuser);
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de la commune dans RelationUtilCom: " + e.getMessage());
            e.printStackTrace();
        }
        return valiny;
    }
}
