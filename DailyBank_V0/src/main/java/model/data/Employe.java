package model.data;

/**
 * Représente un employé de l'agence bancaire.
 */
public class Employe {

    /** L'identifiant unique de l'employé. */
    public int idEmploye;

    /** Le nom de l'employé. */
    public String nom;

    /** Le prénom de l'employé. */
    public String prenom;

    /** Les droits d'accès de l'employé. */
    public String droitsAccess;

    /** Le nom d'utilisateur de l'employé pour se connecter. */
    public String login;

    /** Le mot de passe de l'employé pour se connecter. */
    public String motPasse;

    /** L'identifiant unique de l'agence à laquelle l'employé est associé. */
    public int idAg;

    /**
     * Constructeur avec tous les attributs.
     * @param idEmploye L'identifiant unique de l'employé.
     * @param nom Le nom de l'employé.
     * @param prenom Le prénom de l'employé.
     * @param droitsAccess Les droits d'accès de l'employé.
     * @param login Le nom d'utilisateur de l'employé pour se connecter.
     * @param motPasse Le mot de passe de l'employé pour se connecter.
     * @param idAg L'identifiant unique de l'agence à laquelle l'employé est associé.
     */
    public Employe(int idEmploye, String nom, String prenom, String droitsAccess, String login, String motPasse,
                   int idAg) {
        super();
        this.idEmploye = idEmploye;
        this.nom = nom;
        this.prenom = prenom;
        this.droitsAccess = droitsAccess;
        this.login = login;
        this.motPasse = motPasse;
        this.idAg = idAg;
    }

    /**
     * Constructeur de copie.
     * @param e L'objet Employe à copier.
     */
    public Employe(Employe e) {
        this(e.idEmploye, e.nom, e.prenom, e.droitsAccess, e.login, e.motPasse, e.idAg);
    }

    /** Constructeur par défaut, initialise les attributs à des valeurs par défaut. */
    public Employe() {
        this(-1000, null, null, null, null, null, -1000);
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères de l'employé.
     * @return La représentation sous forme de chaîne de caractères de l'employé.
     */
    @Override
    public String toString() {
        return "Employe [idEmploye=" + this.idEmploye + ", nom=" + this.nom + ", prenom=" + this.prenom
                + ", droitsAccess=" + this.droitsAccess + ", login=" + this.login + ", motPasse=" + this.motPasse
                + ", idAg=" + this.idAg + "]";
    }

    /**
     * Renvoie l'identifiant unique de l'employé.
     * @return L'identifiant unique de l'employé.
     */
    public int getIdEmploye() {
        return idEmploye;
    }

    /**
     * Renvoie le nom de l'employé.
     * @return Le nom de l'employé.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Renvoie le prénom de l'employé.
     * @return Le prénom de l'employé.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Renvoie les droits d'accès de l'employé.
     * @return Les droits d'accès de l'employé.
     */
    public String getDroitsAccess() {
        return droitsAccess;
    }

    /**
     * Renvoie le nom d'utilisateur de l'employé pour se connecter.
     * @return Le nom d'utilisateur de l'employé.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Renvoie le mot de passe de l'employé pour se connecter.
     * @return Le mot de passe de l'employé.
     */
    public String getMotPasse() {
        return motPasse;
    }

    /**
     * Renvoie l'identifiant unique de l'agence à laquelle l'employé est associé.
     * @return L'identifiant unique de l'agence.
     */
    public int getIdAg() {
        return idAg;
    }

    /**
     * Modifie le nom de l'employé.
     * @param nom Le nouveau nom de l'employé.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Modifie le prénom de l'employé.
     * @param prenom Le nouveau prénom de l'employé.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Modifie les droits d'accès de l'employé.
     * @param droitsAccess Les nouveaux droits d'accès de l'employé.
     */
    public void setDroitsAccess(String droitsAccess) {
        this.droitsAccess = droitsAccess;
    }

    /**
     * Modifie le nom d'utilisateur de l'employé.
     * @param login Le nouveau nom d'utilisateur de l'employé.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Modifie le mot de passe de l'employé.
     * @param motPasse Le nouveau mot de passe de l'employé.
     */
    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    /**
     * Modifie l'identifiant unique de l'agence à laquelle l'employé est associé.
     * @param idAg Le nouvel identifiant unique de l'agence.
     */
    public void setIdAg(int idAg) {
        this.idAg = idAg;
    }

}
