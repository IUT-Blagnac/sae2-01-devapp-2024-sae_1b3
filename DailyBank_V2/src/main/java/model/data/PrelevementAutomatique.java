package model.data;

/**
 * Représente une entité de prélèvement automatique.
 */
public class PrelevementAutomatique {

    /** L'ID du prélèvement automatique. */
    public int idPrelev;

    /** L'ID du compte bancaire associé au prélèvement automatique. */
    public int idNumCompte;

    /** Le montant du prélèvement automatique. */
    public double montant;

    /** La date récurrente du prélèvement automatique. */
    public String dateRecurrente;

    /** Le bénéficiaire du prélèvement automatique. */
    public String beneficiaire;

    /**
     * Construit un nouvel objet PrelevementAutomatique avec les détails spécifiés.
     *
     * @param idPrelev L'ID du prélèvement automatique.
     * @param idNumCompte L'ID du compte bancaire associé au prélèvement automatique.
     * @param montant Le montant du prélèvement automatique.
     * @param dateRecurrente La date récurrente du prélèvement automatique.
     * @param beneficiaire Le bénéficiaire du prélèvement automatique.
     */
    public PrelevementAutomatique(int idPrelev, int idNumCompte, double montant, String dateRecurrente, String beneficiaire) {
        this.idPrelev = idPrelev;
        this.idNumCompte = idNumCompte;
        this.montant = montant;
        this.dateRecurrente = dateRecurrente;
        this.beneficiaire = beneficiaire;
    }

    /**
     * Récupère l'ID du prélèvement automatique.
     *
     * @return L'ID du prélèvement automatique.
     */
    public int getIdPrelev() {
        return idPrelev;
    }

    /**
     * Définit l'ID du prélèvement automatique.
     *
     * @param idPrelev L'ID du prélèvement automatique.
     */
    public void setIdPrelev(int idPrelev) {
        this.idPrelev = idPrelev;
    }

    /**
     * Récupère l'ID du compte bancaire associé au prélèvement automatique.
     *
     * @return L'ID du compte bancaire associé au prélèvement automatique.
     */
    public int getIdNumCompte() {
        return idNumCompte;
    }

    /**
     * Définit l'ID du compte bancaire associé au prélèvement automatique.
     *
     * @param idNumCompte L'ID du compte bancaire associé au prélèvement automatique.
     */
    public void setIdNumCompte(int idNumCompte) {
        this.idNumCompte = idNumCompte;
    }

    /**
     * Récupère le montant du prélèvement automatique.
     *
     * @return Le montant du prélèvement automatique.
     */
    public double getMontant() {
        return montant;
    }

    /**
     * Définit le montant du prélèvement automatique.
     *
     * @param montant Le montant du prélèvement automatique.
     */
    public void setMontant(double montant) {
        this.montant = montant;
    }

    /**
     * Récupère la date récurrente du prélèvement automatique.
     *
     * @return La date récurrente du prélèvement automatique.
     */
    public String getDateRecurrente() {
        return dateRecurrente;
    }

    /**
     * Définit la date récurrente du prélèvement automatique.
     *
     * @param dateRecurrente La date récurrente du prélèvement automatique.
     */
    public void setDateRecurrente(String dateRecurrente) {
        this.dateRecurrente = dateRecurrente;
    }

    /**
     * Récupère le bénéficiaire du prélèvement automatique.
     *
     * @return Le bénéficiaire du prélèvement automatique.
     */
    public String getBeneficiaire() {
        return beneficiaire;
    }

    /**
     * Définit le bénéficiaire du prélèvement automatique.
     *
     * @param beneficiaire Le bénéficiaire du prélèvement automatique.
     */
    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'objet PrelevementAutomatique.
     *
     * @return Une représentation sous forme de chaîne de caractères de l'objet PrelevementAutomatique.
     */
    @Override
    public String toString() {
        return "| Prélèvement N°=" + idPrelev + " | Numéro Compte=" + idNumCompte + " | montant=" + montant
                + " | dateRecurrente=" + dateRecurrente + " | beneficiaire=" + beneficiaire +" |";
    }
}
