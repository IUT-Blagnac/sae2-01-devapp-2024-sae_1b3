package model.data;

/**
 * Représente un compte courant d'un client.
 */
public class CompteCourant {

    /** L'identifiant unique du compte courant. */
    public int idNumCompte;

    /** Le montant du découvert autorisé pour le compte courant. */
    public int debitAutorise;

    /** Le solde actuel du compte courant. */
    public double solde;

    /** Indique si le compte courant est clôturé (O pour ouvert, N pour clôturé). */
    public String estCloture;

    /** L'identifiant unique du client associé au compte courant. */
    public int idNumCli;

    /**
     * Constructeur avec tous les attributs.
     * @param idNumCompte L'identifiant unique du compte courant.
     * @param debitAutorise Le montant du découvert autorisé pour le compte courant.
     * @param solde Le solde actuel du compte courant.
     * @param estCloture Indique si le compte courant est clôturé (O pour ouvert, N pour clôturé).
     * @param idNumCli L'identifiant unique du client associé au compte courant.
     */
    public CompteCourant(int idNumCompte, int debitAutorise, double solde, String estCloture, int idNumCli) {
        super();
        this.idNumCompte = idNumCompte;
        this.debitAutorise = debitAutorise;
        this.solde = solde;
        this.estCloture = estCloture;
        this.idNumCli = idNumCli;
    }

    /**
     * Constructeur de copie.
     * @param cc L'objet CompteCourant à copier.
     */
    public CompteCourant(CompteCourant cc) {
        this(cc.idNumCompte, cc.debitAutorise, cc.solde, cc.estCloture, cc.idNumCli);
    }

    /** Constructeur par défaut, initialise les attributs à des valeurs par défaut. */
    public CompteCourant() {
        this(0, 0, 0, "N", -1000);
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères du compte courant.
     * @return La représentation sous forme de chaîne de caractères du compte courant.
     */
    @Override
    public String toString() {
        String s = "" + String.format("%05d", this.idNumCompte) + " : Solde=" + String.format("%12.02f", this.solde)
                + "  ,  Découvert Autorise=" + String.format("%8d", this.debitAutorise);
        if (this.estCloture == null) {
            s = s + " (Cloture)";
        } else {
            s = s + (this.estCloture.equals("N") ? " (Ouvert)" : " (Cloture)");
        }
        return s;
    }

    /**
     * Renvoie l'identifiant unique du compte courant.
     * @return L'identifiant unique du compte courant.
     */
    public int getIdNumCompte() {
        return idNumCompte;
    }

    /**
     * Renvoie le montant du découvert autorisé pour le compte courant.
     * @return Le montant du découvert autorisé pour le compte courant.
     */
    public int getDebitAutorise() {
        return debitAutorise;
    }

    /**
     * Renvoie le solde actuel du compte courant.
     * @return Le solde actuel du compte courant.
     */
    public double getSolde() {
        return solde;
    }

    /**
     * Indique si le compte courant est clôturé (O pour ouvert, N pour clôturé).
     * @return {@code true} si le compte courant est clôturé, {@code false} s'il est ouvert.
     */
    public String getEstCloture() {
        return estCloture;
    }

    /**
     * Renvoie l'identifiant unique du client associé au compte courant.
     * @return L'identifiant unique du client associé au compte courant.
     */
    public int getIdNumCli() {
        return idNumCli;
    }

}
