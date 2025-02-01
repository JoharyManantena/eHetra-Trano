/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import annexe.Produit;
import bean.CGenUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pompe.Pompe;
import utilitaire.UtilDB;

/**
 *
 * @author Belouh
 */
public class EncaissementData {
    private String id;
    private String idant;
    private String idact;
    private String qty;
    private String daty;
    private String idpompe;
    private String idutilisateur;
    private String pompe;
    private String utilisateur;
    private int[] quantiteVendue;
    private double sommetotal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdant() {
        return idant;
    }

    public void setIdant(String idant) {
        this.idant = idant;
    }

    public String getIdact() {
        return idact;
    }

    public void setIdact(String idact) {
        this.idact = idact;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDaty() {
        return daty;
    }

    public void setDaty(String daty) {
        this.daty = daty;
    }

    public String getIdpompe() {
        return idpompe;
    }

    public void setIdpompe(String idpompe) {
        this.idpompe = idpompe;
    }

    public String getIdutilisateur() {
        return idutilisateur;
    }

    public void setIdutilisateur(String idutilisateur) {
        this.idutilisateur = idutilisateur;
    }
    
    
    
    
    
    public EncaissementData(){}
 
    public static List<EncaissementData> getEncaissements() {
        List<EncaissementData> encaissements = new ArrayList<>();
        Connection connection = null;

        try {
            // Initialiser la connexion
            String url = "jdbc:oracle:thin:@localhost:1521:DBCOURS";
            String user = "mystation"; // Remplacez par votre utilisateur Oracle
            String password = "mystation"; // Remplacez par votre mot de passe
            connection = DriverManager.getConnection(url, user, password);

            // Requête SQL pour récupérer toutes les lignes
            String query = "SELECT ID, IDANT, IDACT, QTY, TO_CHAR(DATY, 'YYYY-MM-DD') AS DATY, IDPOMPE, IDUTILISATEUR FROM PRELEVEMENT_QUANTITY"; // Remplacez par le nom de votre table
            try (PreparedStatement pstmt = connection.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                 
                while (rs.next()) {
                    double sommeTemp = 0;
                    EncaissementData encaissement = new EncaissementData();
                    encaissement.setId(rs.getString("ID"));
                    encaissement.setIdant(rs.getString("IDANT"));
                    encaissement.setIdact(rs.getString("IDACT"));
                    encaissement.setQty(rs.getString("QTY"));
                    encaissement.setDaty(rs.getString("DATY"));
                    encaissement.setIdpompe(rs.getString("IDPOMPE"));
                    encaissement.setIdutilisateur(rs.getString("IDUTILISATEUR"));
                    int[] avant = Traitement.getQuantiteProduit(rs.getString("IDANT"));
                    int[] apres = Traitement.getQuantiteProduit(rs.getString("IDACT"));
                    
                    int[] vendue = new int[avant.length];
                    for (int i = 0; i < avant.length; i++) {
                        vendue[i] = avant[i] - apres[i];
                    }
                    try {
                        Connection conn = new UtilDB().GetConn("gallois","gallois");
                        Produit[] produits = (Produit[]) CGenUtil.rechercher(new Produit(),"select * from produit where idcategorie ='CTG000103'",conn);
                        Pompe tempPompe = ((Pompe[])CGenUtil.rechercher(new Pompe(), "select * from pompe where id ='"+rs.getString("IDPOMPE")+"'", conn))[0];
                        encaissement.setPompe(tempPompe.getVal());
                        encaissement.setUtilisateur(Traitement.getLoginUser(conn,rs.getString("IDUTILISATEUR") ));
                        
                        for (int i = 0; i < produits.length; i++) {
                            sommeTemp = sommeTemp + (vendue[i] * produits[i].getPuVente());
                        }
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    encaissement.setQuantiteVendue(vendue);
                    encaissement.setSommetotal(sommeTemp);
                    // Ajouter l'objet EncaissementData à la liste
                    encaissements.add(encaissement);
                    
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
        
        return encaissements;
    }

    public String getPompe() {
        return pompe;
    }

    public void setPompe(String pompe) {
        this.pompe = pompe;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public int[] getQuantiteVendue() {
        return quantiteVendue;
    }

    public void setQuantiteVendue(int[] quantiteVendue) {
        this.quantiteVendue = quantiteVendue;
    }

    public double getSommetotal() {
        return sommetotal;
    }

    public void setSommetotal(double sommetotal) {
        this.sommetotal = sommetotal;
    }
    
    
}
