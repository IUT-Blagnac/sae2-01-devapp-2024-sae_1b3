package model.data;

/**
 * Représente un client de la banque.
 */
public class Client {

    /** L'identifiant unique du client. */
    public int idNumCli;

    /** Le nom du client. */
    public String nom;

    /** Le prénom du client. */
    public String prenom;

    /** L'adresse postale du client. */
    public String adressePostale;

    /** L'email du client. */
    public String email;

    /** Le numéro de téléphone du client. */
    public String telephone;

    /** Indique si le client est inactif. */
    public String estInactif;

    /** L'identifiant de l'agence à laquelle le client est rattaché. */
    public int idAg;

    /**
     * Constructeur avec tous les attributs.
     * @param idNumCli L'identifiant unique du client.
     * @param nom Le nom du client.
     * @param prenom Le prénom du client.
     * @param adressePostale L'adresse postale du client.
     * @param email L'email du client.
     * @param telephone Le numéro de téléphone du client.
     * @param estInactif Indique si le client est inactif.
     * @param idAg L'identifiant de l'agence à laquelle le client est rattaché.
     */
    public Client(int idNumCli, String nom, String prenom, String adressePostale, String email, String telephone,
                  String estInactif, int idAg) {
        super();
        this.idNumCli = idNumCli;
        this.nom = nom;
        this.prenom = prenom;
        this.adressePostale = adressePostale;
        this.email = email;
        this.telephone = telephone;
        this.estInactif = estInactif;
        this.idAg = idAg;
    }

    /**
     * Constructeur de copie.
     * @param c L'objet Client à copier.
     */
    public Client(Client c) {
        this(c.idNumCli, c.nom, c.prenom, c.adressePostale, c.email, c.telephone, c.estInactif, c.idAg);
    }

    /** Constructeur par défaut, initialise les attributs à des valeurs par défaut. */
    public Client() {
        this(-1000, null, null, null, null, null, "Non", -1000);
    }

    /**
     * Renvoie l'identifiant unique du client.
     * @return L'identifiant unique du client.
     */
    public int getIdNumCli() {
        return idNumCli;
    }

    /**
     * Renvoie le nom du client.
     * @return Le nom du client.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Renvoie le prénom du client.
     * @return Le prénom du client.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Renvoie l'adresse postale du client.
     * @return L'adresse postale du client.
     */
    public String getAdressePostale() {
        return adressePostale;
    }

    /**
     * Renvoie l'email du client.
     * @return L'email du client.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Renvoie le numéro de téléphone du client.
     * @return Le numéro de téléphone du client.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Indique si le client est inactif.
     * @return {@code true} si le client est inactif, {@code false} sinon.
     */
    public String getEstInactif() {
        return estInactif;
    }

    /**
     * Renvoie l'identifiant de l'agence à laquelle le client est rattaché.
     * @return L'identifiant de l'agence.
     */
    public int getIdAg() {
        return idAg;
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères du client.
     * @return La représentation sous forme de chaîne de caractères du client.
     */
    @Override
    public String toString() {
        return "[" + this.idNumCli + "]  " + this.nom.toUpperCase() + " " + this.prenom + "(" + this.email + ")  {"
                + this.telephone + "}";
    }
}
