package model.data;

public class PrelevementAutomatique {

    public int idPrelev;
    public int idNumCompte;
    public double montant;
    public String dateRecurrente;
    public String beneficiaire;

    public PrelevementAutomatique(int idPrelev, int idNumCompte, double montant, String dateRecurrente, String beneficiaire) {
        this.idPrelev = idPrelev;
        this.idNumCompte = idNumCompte;
        this.montant = montant;
        this.dateRecurrente = dateRecurrente;
        this.beneficiaire = beneficiaire;
    }

    public int getIdPrelev() {
        return idPrelev;
    }

    public void setIdPrelev(int idPrelev) {
        this.idPrelev = idPrelev;
    }

    public int getIdNumCompte() {
        return idNumCompte;
    }

    public void setIdNumCompte(int idNumCompte) {
        this.idNumCompte = idNumCompte;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getDateRecurrente() {
        return dateRecurrente;
    }

    public void setDateRecurrente(String dateRecurrente) {
        this.dateRecurrente = dateRecurrente;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    @Override
    public String toString() {
        return "PrelevementAutomatique [idPrelev=" + idPrelev + ", idNumCompte=" + idNumCompte + ", montant=" + montant
                + ", dateRecurrente=" + dateRecurrente + ", beneficiaire=" + beneficiaire + "]";
    }
}
