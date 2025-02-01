/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;


public class FactureMere {
    private int mois;
    private int annees;
    private int id_maison;
    private String nom_maison;
    private double montant;
    private int etatPaye;

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

    public int getId_maison() {
        return id_maison;
    }

    public void setId_maison(int id_maison) {
        this.id_maison = id_maison;
    }

    public String getNom_maison() {
        return nom_maison;
    }

    public void setNom_maison(String nom_maison) {
        this.nom_maison = nom_maison;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getEtatPaye() {
        return etatPaye;
    }

    public void setEtatPaye(int etatPaye) {
        this.etatPaye = etatPaye;
    }
    
    

    public FactureMere() {
    }
    
    
}
