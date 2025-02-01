/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import annexe.Produit;
import bean.CGenUtil;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import utilitaire.UtilDB;


public class Traitement {
    public static int[] getStockDispo(String dateDeb,String dateFin) throws Exception{
        SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date sqlDateDeb = new java.sql.Date(da.parse(dateDeb).getTime());
        java.sql.Date sqlDateFin = new java.sql.Date(da.parse(dateFin).getTime());
        Connection conn = new UtilDB().GetConn("gallois","gallois");
        Produit[] produits = (Produit[]) CGenUtil.rechercher(new Produit(),"select * from produit where idcategorie ='CTG000083'",conn);
        int[] valiny = new int[produits.length];
        for (int i = 0; i < valiny.length; i++) {
            valiny[i] = getTotalEntree(conn, produits[i].getId(), sqlDateDeb, sqlDateFin) - getTotalSortie(conn, produits[i].getId(), sqlDateDeb, sqlDateFin);
        }
        return valiny;
    }
    
    public static int getTotalEntree(Connection connection, String idProduit, Date dateDebut, Date dateFin) throws SQLException {
        String query = "SELECT SUM(ENTREE) AS total_entree " +
                       "FROM ETATSTOCK " +
                       "WHERE IDPRODUIT = ? " +
                       "AND DATY BETWEEN ? AND ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Définir les paramètres
            pstmt.setString(1, idProduit);
            pstmt.setDate(2, dateDebut);
            pstmt.setDate(3, dateFin);

            // Exécuter la requête
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Renvoyer la somme des entrées
                    return rs.getInt("total_entree");
                }
            }
        }
        return 0;
    }
    
    public static int getTotalSortie(Connection connection, String idProduit, Date dateDebut, Date dateFin) throws SQLException {
        String query = "SELECT SUM(SORTIE) AS total_sortie " +
                       "FROM MVTSTOCKFILLELIB " +
                       "WHERE IDPRODUIT = ? " +
                       "AND DATY BETWEEN ? AND ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Définir les paramètres
            pstmt.setString(1, idProduit);
            pstmt.setDate(2, dateDebut);
            pstmt.setDate(3, dateFin);

            // Exécuter la requête
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Renvoyer la somme des sorties
                    return rs.getInt("total_sortie");
                }
            }
        }

        return 0;
    }
    
    public static int[] getStockDispoLubrifiant() throws Exception{
        String dateDeb ="2020-11-11";
        String dateFin="2030-11-11";
        SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date sqlDateDeb = new java.sql.Date(da.parse(dateDeb).getTime());
        java.sql.Date sqlDateFin = new java.sql.Date(da.parse(dateFin).getTime());
        Connection conn = new UtilDB().GetConn("gallois","gallois");
        Produit[] produits = (Produit[]) CGenUtil.rechercher(new Produit(),"select * from produit where idcategorie ='CTG000103'",conn);
        int[] valiny = new int[produits.length];
        for (int i = 0; i < valiny.length; i++) {
            valiny[i] = getTotalEntree(conn, produits[i].getId(), sqlDateDeb, sqlDateFin) - getTotalSortie(conn, produits[i].getId(), sqlDateDeb, sqlDateFin);
        }
        return valiny;
    }
    
    public static int[] getStockDispoLubrifiant(String dateDeb,String dateFin) throws Exception{
        SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date sqlDateDeb = new java.sql.Date(da.parse(dateDeb).getTime());
        java.sql.Date sqlDateFin = new java.sql.Date(da.parse(dateFin).getTime());
        Connection conn = new UtilDB().GetConn("gallois","gallois");
        Produit[] produits = (Produit[]) CGenUtil.rechercher(new Produit(),"select * from produit where idcategorie ='CTG000103'",conn);
        int[] valiny = new int[produits.length];
        for (int i = 0; i < valiny.length; i++) {
            valiny[i] = getTotalEntree(conn, produits[i].getId(), sqlDateDeb, sqlDateFin) - getTotalSortie(conn, produits[i].getId(), sqlDateDeb, sqlDateFin);
        }
        return valiny;
    }
    
    public static String getLoginUser(Connection connection, String refuser) {
        String loginUser = null;

        // Requête SQL pour récupérer le LOGINUSER
        String query = "SELECT LOGINUSER FROM UTILISATEUR WHERE REFUSER = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, refuser);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    loginUser = rs.getString("LOGINUSER");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return loginUser;
    }

    public static int[] getQuantiteProduit(String idPrelevement) {
        int[] quantites = null;
        Connection connection = null;

        try {
            // Initialiser la connexion
            String url = "jdbc:oracle:thin:@localhost:1521:DBCOURS";
            String user = "mystation"; // Remplacez par votre utilisateur Oracle
            String password = "mystation"; // Remplacez par votre mot de passe
            connection = DriverManager.getConnection(url, user, password);

            // Requête SQL
            String query = "SELECT QUANTITE_PRODUIT FROM STOCKLUBBELOUH WHERE ID_PRELEVEMENT = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, idPrelevement);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Récupérer la chaîne de quantités
                        String quantiteProduitStr = rs.getString("QUANTITE_PRODUIT");
                        
                        // Convertir la chaîne en tableau d'entiers
                        String[] quantiteStrArray = quantiteProduitStr.split("_");
                        quantites = new int[quantiteStrArray.length];
                        
                        for (int i = 0; i < quantiteStrArray.length; i++) {
                            quantites[i] = Integer.parseInt(quantiteStrArray[i]);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer la connexion
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return quantites;
    }
}
