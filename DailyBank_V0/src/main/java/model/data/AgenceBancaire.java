package model.data;
/**
 * Représente une agence bancaire.
 */
public class AgenceBancaire {

    /** L'identifiant de l'agence bancaire. */
    public int idAg;

    /** Le nom de l'agence bancaire. */
    public String nomAg;

    /** L'adresse postale de l'agence bancaire. */
    public String adressePostaleAg;

    /** L'identifiant de l'employé chef d'agence. */
    public int idEmployeChefAg;

    /**
     * Constructeur avec tous les attributs.
     * @param idAg L'identifiant de l'agence bancaire.
     * @param nomAg Le nom de l'agence bancaire.
     * @param adressePostaleAg L'adresse postale de l'agence bancaire.
     * @param idEmployeChefAg L'identifiant de l'employé chef d'agence.
     */
    public AgenceBancaire(int idAg, String nomAg, String adressePostaleAg, int idEmployeChefAg) {
        super();
        this.idAg = idAg;
        this.nomAg = nomAg;
        this.adressePostaleAg = adressePostaleAg;
        this.idEmployeChefAg = idEmployeChefAg;
    }

    /**
     * Constructeur de copie.
     * @param ag L'objet AgenceBancaire à copier.
     */
    public AgenceBancaire(AgenceBancaire ag) {
        this(ag.idAg, ag.nomAg, ag.adressePostaleAg, ag.idEmployeChefAg);
    }

    /** Constructeur par défaut, initialise les attributs à des valeurs par défaut. */
    public AgenceBancaire() {
        this(-1000, null, null, -1000);
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères de l'objet AgenceBancaire.
     * @return La représentation sous forme de chaîne de caractères de l'objet AgenceBancaire.
     */
    @Override
    public String toString() {
        return "AgenceBancaire [idAg=" + this.idAg + ", nomAg=" + this.nomAg + ", adressePostaleAg="
                + this.adressePostaleAg + ", idEmployeChefAg=" + this.idEmployeChefAg + "]";
    }
}
