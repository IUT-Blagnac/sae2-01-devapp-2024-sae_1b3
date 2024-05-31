package model.data;

/**
 * Représente un type d'opération bancaire.
 */
public class TypeOperation {

    /** L'identifiant du type d'opération. */
    public String idTypeOp;

    /**
     * Constructeur avec un seul paramètre.
     * @param idTypeOp L'identifiant du type d'opération.
     */
    public TypeOperation(String idTypeOp) {
        super();
        this.idTypeOp = idTypeOp;
    }

    /**
     * Constructeur de copie.
     * @param to L'objet TypeOperation à copier.
     */
    public TypeOperation(TypeOperation to) {
        this(to.idTypeOp);
    }

    /** Constructeur par défaut, initialise l'attribut à null. */
    public TypeOperation() {
        this((String) null);
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères du type d'opération.
     * @return La représentation sous forme de chaîne de caractères du type d'opération.
     */
    @Override
    public String toString() {
        return "TypeOperation [idTypeOp=" + this.idTypeOp + "]";
    }

}
