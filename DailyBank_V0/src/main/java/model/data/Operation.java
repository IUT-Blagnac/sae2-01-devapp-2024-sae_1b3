package model.data;

import java.sql.Date;

/**
 * Représente une opération bancaire effectuée sur un compte.
 */
public class Operation {

    /** L'identifiant unique de l'opération. */
    public int idOperation;

    /** Le montant de l'opération. */
    public double montant;

    /** La date de l'opération. */
    public Date dateOp;

    /** La date de valeur de l'opération. */
    public Date dateValeur;

    /** L'identifiant unique du compte sur lequel l'opération a été effectuée. */
    public int idNumCompte;

    /** L'identifiant du type d'opération. */
    public String idTypeOp;

    /**
     * Constructeur avec tous les attributs.
     * @param idOperation L'identifiant unique de l'opération.
     * @param montant Le montant de l'opération.
     * @param dateOp La date de l'opération.
     * @param dateValeur La date de valeur de l'opération.
     * @param idNumCompte L'identifiant unique du compte sur lequel l'opération a été effectuée.
     * @param idTypeOp L'identifiant du type d'opération.
     */
    public Operation(int idOperation, double montant, Date dateOp, Date dateValeur, int idNumCompte, String idTypeOp) {
        super();
        this.idOperation = idOperation;
        this.montant = montant;
        this.dateOp = dateOp;
        this.dateValeur = dateValeur;
        this.idNumCompte = idNumCompte;
        this.idTypeOp = idTypeOp;
    }

    /**
     * Constructeur de copie.
     * @param o L'objet Operation à copier.
     */
    public Operation(Operation o) {
        this(o.idOperation, o.montant, o.dateOp, o.dateValeur, o.idNumCompte, o.idTypeOp);
    }

    /** Constructeur par défaut, initialise les attributs à des valeurs par défaut. */
    public Operation() {
        this(-1000, 0, null, null, -1000, null);
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères de l'opération.
     * @return La représentation sous forme de chaîne de caractères de l'opération.
     */
    @Override
    public String toString() {
        return this.dateOp + " : " + String.format("%25s", this.idTypeOp) + " "
                + String.format("%10.02f", this.montant);
    }

}
