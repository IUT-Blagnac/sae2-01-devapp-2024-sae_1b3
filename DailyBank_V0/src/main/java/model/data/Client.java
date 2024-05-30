package model.data;

public class Client {

    public int idNumCli;
    public String nom, prenom, adressePostale, email, telephone;
    public String estInactif;
    public int idAg;

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

    public Client(Client c) {
        this(c.idNumCli, c.nom, c.prenom, c.adressePostale, c.email, c.telephone, c.estInactif, c.idAg);
    }

    public Client() {
        this(-1000, null, null, null, null, null, "Non", -1000);
    }

    public int getIdNumCli() {
        return idNumCli;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAdressePostale() {
        return adressePostale;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEstInactif() {
        return estInactif;
    }

    public int getIdAg() {
        return idAg;
    }

    @Override
    public String toString() {
        return "[" + this.idNumCli + "]  " + this.nom.toUpperCase() + " " + this.prenom + "(" + this.email + ")  {"
                + this.telephone + "}";
    }
}
