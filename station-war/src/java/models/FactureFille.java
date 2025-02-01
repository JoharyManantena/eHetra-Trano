/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;


public class FactureFille {
    private double pu;
    private double surfaceTotal;
    private double coefficientMure = 0;
    private double coefficientToit = 0;
    private String proprietaire;

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) {
        this.pu = pu;
    }

    public double getSurfaceTotal() {
        return surfaceTotal;
    }

    public void setSurfaceTotal(double surfaceTotal) {
        this.surfaceTotal = surfaceTotal;
    }

    public double getCoefficientMure() {
        return coefficientMure;
    }

    public void setCoefficientMure(double coefficientMure) {
        this.coefficientMure = coefficientMure;
    }

    public double getCoefficientToit() {
        return coefficientToit;
    }

    public void setCoefficientToit(double coefficientToit) {
        this.coefficientToit = coefficientToit;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }
    
    public FactureFille() {
    }
    
    
}
