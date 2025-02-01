/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import static models.Arrondissement.clobToString;
import utilitaire.UtilDB;


public class Maison {
    private int id;
    private String nom;
    private double longueur;
    private double largeur;  
    private int nb_etage;
    private double log;   
    private double lat;
    private double impots;
    private int toit;
    private int mur;
    private String propio;
    
    public Maison() {
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the longueur
     */
    public double getLongueur() {
        return longueur;
    }

    /**
     * @param longueur the longueur to set
     */
    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }

    /**
     * @return the largeur
     */
    public double getLargeur() {
        return largeur;
    }

    /**
     * @param largeur the largeur to set
     */
    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    /**
     * @return the nb_etage
     */
    public int getNb_etage() {
        return nb_etage;
    }

    /**
     * @param nb_etage the nb_etage to set
     */
    public void setNb_etage(int nb_etage) {
        this.nb_etage = nb_etage;
    }

    /**
     * @return the log
     */
    public double getLog() {
        return log;
    }

    /**
     * @param log the log to set
     */
    public void setLog(double log) {
        this.log = log;
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }
    
    public double getImpots() {
        return impots;
    }

    public int getToit() {
        return toit;
    }

    public void setToit(int toit) {
        this.toit = toit;
    }

    public int getMur() {
        return mur;
    }

    public void setMur(int mur) {
        this.mur = mur;
    }

    public String getPropio() {
        return propio;
    }

    public void setPropio(String propio) {
        this.propio = propio;
    }
    
    public void setImpots(double impots){
        this.impots = impots;
    }

    public double getSurfaceHabitable(){
        return (this.getLongueur() * this.getLargeur() * this.getNb_etage());
    } 
    
    public Maison save() {
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL pour insérer les données dans la table
            String query = "INSERT INTO MAISON (ID,NOM, LONGUEUR, LARGEUR, NB_ETAGE, LOG, LAT, IMPOTS, TOIT, MUR) VALUES (maison_seq.NEXTVAL,?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Associer les paramètres de la requête avec les attributs de l'objet
            stmt.setString(1, this.getNom());
            stmt.setDouble(2, this.getLongueur());
            stmt.setDouble(3, this.getLargeur());
            stmt.setInt(4, this.getNb_etage());
            stmt.setDouble(5, this.getLog());
            stmt.setDouble(6, this.getLat());
            stmt.setDouble(7, this.getImpots());
            stmt.setInt(8,this.getToit());
            stmt.setInt(9,this.getMur());

            // Exécuter la requête
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Maison insérée avec succès !");
            }

            // Fermer la connexion
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de l'insertion de la maison : " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }

    public static Maison getById(int id) {
        Maison maison = new Maison();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL pour récupérer une maison par ID
            String query = "SELECT * FROM MAISON WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Associer le paramètre de la requête
            stmt.setInt(1, id);

            // Exécuter la requête
            ResultSet rs = stmt.executeQuery();

            // Si une maison est trouvée
            if (rs.next()) {
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
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de la maison : " + e.getMessage());
            e.printStackTrace();
        }
        return maison;
    }

    
    public static Vector<Maison> getAll() {
        Vector<Maison> maisons = new Vector<Maison>();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL pour récupérer toutes les maisons
            String query = "SELECT * FROM MAISON";
            PreparedStatement stmt = conn.prepareStatement(query);

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
                maison.setPropio(Proprietaire.getProp(maison.getId()).getNom());
                maisons.add(maison);
            }
            

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des maisons : " + e.getMessage());
            e.printStackTrace();
        }
        return maisons;
    }
    
    
    public Commune getCommune() {
        Commune valiny = new Commune();
        try {
            
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL avec SDO_CONTAINS et transformation de ZONE en WKT
            String query = 
                "SELECT c.ID, c.NOM, SDO_UTIL.TO_WKTGEOMETRY(c.ZONE) AS ZONE " +
                "FROM COMMUNE c " +
                "WHERE SDO_CONTAINS( " +
                "    c.ZONE, " +
                "    SDO_GEOMETRY(2001, 4326, SDO_POINT_TYPE(?, ?, NULL), NULL, NULL) " +
                ") = 'TRUE'";

            
            PreparedStatement stmt = conn.prepareStatement(query);

            
            stmt.setDouble(1, this.getLog());
            stmt.setDouble(2, this.getLat());

            // Exécuter la requête
            ResultSet rs = stmt.executeQuery();

            // Si une commune correspond, remplir l'objet Commune
            if (rs.next()) {
                valiny.setId(rs.getInt("ID"));
                valiny.setNom(rs.getString("NOM"));
                valiny.setZone(Arrondissement.clobToString(rs.getClob("ZONE")));
            } else {
                System.out.println("Aucune commune trouvée pour les coordonnées : log=" + this.getLog() + ", lat=" + this.getLat());
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de la commune : " + e.getMessage());
            e.printStackTrace();
        }
        return valiny;
    }

    
    public Arrondissement getArrodissement(){
        Arrondissement valiny = new Arrondissement();
        try {
            // Établir la connexion à la base de données
            Connection conn = new UtilDB().GetConn("gallois", "gallois");

            // Requête SQL avec SDO_CONTAINS
            String query = 
                "SELECT c.ID, c.NOM, SDO_UTIL.TO_WKTGEOMETRY(c.ZONE) AS ZONE " +
                "FROM COMMUNE c " +
                "WHERE SDO_CONTAINS( " +
                "    c.ZONE, " +
                "    SDO_GEOMETRY(2001, 4326, SDO_POINT_TYPE(?, ?, NULL), NULL, NULL) " +
                ") = 'TRUE'";

            // Préparer la requête
            PreparedStatement stmt = conn.prepareStatement(query);

            // Passer les paramètres log et lat
            stmt.setDouble(1, this.getLog());
            stmt.setDouble(2, this.getLat());

            // Exécuter la requête
            ResultSet rs = stmt.executeQuery();

            // Si une commune correspond, remplir l'objet Commune
            if (rs.next()) {
                valiny.setId(rs.getInt("ID"));
                valiny.setNom(rs.getString("NOM"));
                valiny.setZone(Arrondissement.clobToString(rs.getClob("ZONE")));
            } else {
                System.out.println("Aucune arrodissement trouvée pour les coordonnées : log=" + this.getLog() + ", lat=" + this.getLat());
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de la commune : " + e.getMessage());
            e.printStackTrace();
        }
        return valiny;
    }
    
    public Facture facturer(int annees, int mois){
        Facture valiny = new Facture();
        ConfPrixMetreCarre prixMetreCarre = ConfPrixMetreCarre.getConf();
        int idcommune = this.getCommune().getId();
        MateriauxValue materielToit = MateriauxValue.getMateriel(mois, annees, idcommune, this.getToit());
        MateriauxValue materielMur = MateriauxValue.getMateriel(mois, annees, idcommune, this.getMur());
        FactureFille factFille = new FactureFille();
        factFille.setPu(prixMetreCarre.getPrix());
        factFille.setSurfaceTotal(getSurfaceHabitable());
        factFille.setCoefficientMure(materielMur.getCoefficient());
        factFille.setCoefficientToit(materielToit.getCoefficient());
        Proprietaire prop = Proprietaire.getProp(this.getId());
        factFille.setProprietaire(prop.getNom());
        FactureMere factMere = new FactureMere();
        factMere.setMois(mois);
        factMere.setAnnees(annees);
        factMere.setId_maison(this.getId());
        factMere.setNom_maison(this.getNom());
        int etatPayer = HistoriqueImpots.etatPayer(mois, this.getId(), annees);
        factMere.setEtatPaye(etatPayer);
        double montant = factFille.getSurfaceTotal() * factFille.getPu()* factFille.getCoefficientMure() * factFille.getCoefficientToit();
        factMere.setMontant(montant);
        
        valiny.setFactMere(factMere);
        valiny.setFactFille(factFille);
        return valiny;
    }
    
    
}
