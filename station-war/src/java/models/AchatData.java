/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import annexe.Produit;
import bean.CGenUtil;
import java.sql.Connection;
import stock.EtatStockBelouh;
import utilitaire.UtilDB;

/**
 *
 * @author Belouh
 */
public class AchatData {
    private String date;
    private String produit;
    private int quantite;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public AchatData() {
    }
    
    public EtatStockBelouh genererAchat() throws Exception{
        EtatStockBelouh etat = new EtatStockBelouh();
        Connection conn = new UtilDB().GetConn("gallois","gallois");
        Produit produitBase = ((Produit[])CGenUtil.rechercher(new Produit(), "select * from produit where id = '"+this.getProduit()+"'",conn ))[0];
        etat.setIdProduit(produitBase.getId());
        etat.setIdMagasin("MAG000245");
        etat.setIdPoint("PNT000124");
        etat.setIdTypeProduit(produitBase.getIdTypeProduit());
        etat.setIdUnite(produitBase.getIdUnite());
        etat.setEntree(quantite);
        etat.setDaty(date);
        conn.close();
        return etat;
    }
    
}
