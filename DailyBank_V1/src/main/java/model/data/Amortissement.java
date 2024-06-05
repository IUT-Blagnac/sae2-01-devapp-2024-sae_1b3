package model.data;

/**
 * Représente une ligne d'amortissement pour un crédit.
 * Contient des informations sur le mois, le capital restant, l'intérêt, le capital remboursé,
 * la mensualité et l'assurance.
 * 
 * @autor Théo
 */
public class Amortissement {
    private int mois;
    private double capitalRestant;
    private double interet;
    private double capitalRembourse;
    private double mensualite;
    private double assurance;

    /**
     * Constructeur pour créer une ligne d'amortissement.
     * 
     * @param mois le numéro du mois
     * @param capitalRestant le capital restant dû après le paiement de ce mois
     * @param interet l'intérêt payé pour ce mois
     * @param capitalRembourse le capital remboursé ce mois-ci
     * @param mensualite le montant total de la mensualité
     * @param assurance le coût de l'assurance pour ce mois
     */
    public Amortissement(int mois, double capitalRestant, double interet, double capitalRembourse, double mensualite, double assurance) {
        this.mois = mois;
        this.capitalRestant = capitalRestant;
        this.interet = interet;
        this.capitalRembourse = capitalRembourse;
        this.mensualite = mensualite;
        this.assurance = assurance;
    }

    public int getMois() {
        return mois;
    }

    public double getCapitalRestant() {
        return capitalRestant;
    }

    public double getInteret() {
        return interet;
    }

    public double getCapitalRembourse() {
        return capitalRembourse;
    }

    public double getMensualite() {
        return mensualite;
    }

    public double getAssurance() {
        return assurance;
    }
}
