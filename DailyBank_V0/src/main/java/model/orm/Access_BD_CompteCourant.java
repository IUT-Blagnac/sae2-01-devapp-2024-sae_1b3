package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.data.CompteCourant;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

/**
 *
 * Classe d'accès aux CompteCourant en BD Oracle.
 *
 */
public class Access_BD_CompteCourant {

	public Access_BD_CompteCourant() {
	}

	
/**
	 * Recherche des CompteCourant d'un client à partir de son id.
	 *
	 * @param idNumCli id du client dont on cherche les comptes
	 * @return Tous les CompteCourant de idNumCli (ou liste vide)
	 * @throws DataAccessException        Erreur d'accès aux données (requête mal
	 *                                    formée ou autre)
	 * @throws DatabaseConnexionException Erreur de connexion
	 * @author Thomas CEOLIN
	 */
	public ArrayList<CompteCourant> getCompteCourants(int idNumCli, boolean isVirement, int idNumCpt)
			throws DataAccessException, DatabaseConnexionException {

		ArrayList<CompteCourant> alResult = new ArrayList<>();
		PreparedStatement pst;
		
		try {
			Connection con = LogToDatabase.getConnexion();
			String query;
			if(isVirement && idNumCpt != -1) {
				query = "SELECT * FROM CompteCourant where IDNUMCLI = ? AND IDNUMCOMPTE <> ?";
				query += " ORDER BY idNumCompte";
				pst = con.prepareStatement(query);
				pst.setInt(1, idNumCli);
				pst.setInt(2, idNumCpt);
			} else {
				query = "SELECT * FROM CompteCourant where idNumCli = ?";
				query += " ORDER BY idNumCompte";
				pst = con.prepareStatement(query);
				pst.setInt(1, idNumCli);
			}

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int idNumCompte = rs.getInt("idNumCompte");
				int debitAutorise = rs.getInt("debitAutorise");
				double solde = rs.getDouble("solde");
				String estCloture = rs.getString("estCloture");
				int idNumCliTROUVE = rs.getInt("idNumCli");

				alResult.add(new CompteCourant(idNumCompte, debitAutorise, solde, estCloture, idNumCliTROUVE));
			}
			rs.close();
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.CompteCourant, Order.SELECT, "Erreur accès", e);
		}

		return alResult;
	}



	/**
	 * Recherche d'un CompteCourant à partir de son id (idNumCompte).
	 *
	 * @param idNumCompte id du compte (clé primaire)
	 * @return Le compte ou null si non trouvé
	 * @throws RowNotFoundOrTooManyRowsException La requête renvoie plus de 1 ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 */
	public CompteCourant getCompteCourant(int idNumCompte)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {
			CompteCourant cc;

			Connection con = LogToDatabase.getConnexion();

			String query = "SELECT * FROM CompteCourant where" + " idNumCompte = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idNumCompte);

			System.err.println(query);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				int idNumCompteTROUVE = rs.getInt("idNumCompte");
				int debitAutorise = rs.getInt("debitAutorise");
				double solde = rs.getDouble("solde");
				String estCloture = rs.getString("estCloture");
				int idNumCliTROUVE = rs.getInt("idNumCli");

				cc = new CompteCourant(idNumCompteTROUVE, debitAutorise, solde, estCloture, idNumCliTROUVE);
			} else {
				rs.close();
				pst.close();
				return null;
			}

			if (rs.next()) {
				throw new RowNotFoundOrTooManyRowsException(Table.CompteCourant, Order.SELECT,
						"Recherche anormale (en trouve au moins 2)", null, 2);
			}
			rs.close();
			pst.close();
			return cc;
		} catch (SQLException e) {
			throw new DataAccessException(Table.CompteCourant, Order.SELECT, "Erreur accès", e);
		}
	}

	/**
	 * Mise à jour d'un CompteCourant.
	 *
	 * cc.idNumCompte (clé primaire) doit exister seul cc.debitAutorise est mis à
	 * jour cc.solde non mis à jour (ne peut se faire que par une opération)
	 * cc.idNumCli non mis à jour (un cc ne change pas de client)
	 *
	 * @param cc IN cc.idNumCompte (clé primaire) doit exister seul
	 * @throws RowNotFoundOrTooManyRowsException La requête modifie 0 ou plus de 1
	 *                                           ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 * @throws ManagementRuleViolation           Erreur sur le solde courant par
	 *                                           rapport au débitAutorisé (solde <
	 *                                           débitAutorisé)
	 * @author Yahya MAGAZ
	 */
	public void updateCompteCourant(CompteCourant cc) throws RowNotFoundOrTooManyRowsException, DataAccessException,
        DatabaseConnexionException, ManagementRuleViolation {
    try {
        // Récupération du compte avant modification
        CompteCourant cAvant = this.getCompteCourant(cc.idNumCompte);
        
        // Correction du signe du débit autorisé si nécessaire
        if (cc.debitAutorise > 0) {
            cc.debitAutorise = -cc.debitAutorise;
        }
        
        // Vérification des règles de gestion
        if (cAvant.solde < cc.debitAutorise) {
            throw new ManagementRuleViolation(Table.CompteCourant, Order.UPDATE,
                    "Erreur de règle de gestion : solde à découvert", null);
        }
        
        // Ouverture de la connexion à la base de données
        Connection con = LogToDatabase.getConnexion();

        // Préparation de la requête SQL
        String query = "UPDATE CompteCourant SET solde = ?, debitAutorise = ? WHERE idNumCompte = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setDouble(1, cc.solde); // Mise à jour du solde
        pst.setInt(2, cc.debitAutorise); // Mise à jour du débit autorisé
        pst.setInt(3, cc.idNumCompte); // Condition de mise à jour sur l'ID du compte

        // Exécution de la requête SQL
        int result = pst.executeUpdate();
        
        // Fermeture des ressources
        pst.close();
        
        // Vérification du nombre de lignes affectées
        if (result != 1) {
            con.rollback(); // Annulation des changements en cas d'échec
            throw new RowNotFoundOrTooManyRowsException(Table.CompteCourant, Order.UPDATE,
                    "Update anormal (update de moins ou plus d'une ligne)", null, result);
        }
        
        // Confirmation des changements
        con.commit();
        
    } catch (SQLException e) {
        // Gestion des exceptions SQL
        throw new DataAccessException(Table.CompteCourant, Order.UPDATE, "Erreur accès", e);
    }
}




	/**
	 * Insère un compte courant dans la base de données.
	 *
	 * @param compte Le compte courant à insérer.
	 * @throws DataAccessException      Si une erreur d'accès aux données survient.
	 * @throws DatabaseConnexionException Si une erreur de connexion à la base de données survient.
	 */
	public void insertCompte(CompteCourant compte) throws DataAccessException, DatabaseConnexionException {
		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "INSERT INTO CompteCourant (idNumCompte, debitAutorise, solde, estCloture, idNumCli) VALUES (?, ?, ?, ?, ?)";

			PreparedStatement pst = con.prepareStatement(query);
			System.out.println("le num compte insert est " + compte.idNumCompte);
			pst.setInt(1, compte.idNumCompte);
			pst.setInt(2, compte.debitAutorise);
			pst.setDouble(3, compte.solde);
			pst.setString(4, compte.estCloture);
			pst.setInt(5, compte.idNumCli);

			pst.executeUpdate();
			pst.close();
			con.commit();
		} catch (SQLException e) {
			throw new DataAccessException(Table.CompteCourant, Order.INSERT, "Erreur lors de l'insertion du compte", e);
		}
}


	/**
	 * Récupère tous les comptes courants depuis la base de données.
	 *
	 * @return Une liste contenant tous les comptes courants.
	 * @throws DataAccessException      Si une erreur d'accès aux données survient.
	 * @throws DatabaseConnexionException Si une erreur de connexion à la base de données survient.
	 * @author Théo Raban
	 */
	public ArrayList<CompteCourant> getTousLesComptes() throws DataAccessException, DatabaseConnexionException {
		ArrayList<CompteCourant> alResult = new ArrayList<>();

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM CompteCourant ORDER BY idNumCompte";

			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int idNumCompte = rs.getInt("idNumCompte");
				int debitAutorise = rs.getInt("debitAutorise");
				double solde = rs.getDouble("solde");
				String estCloture = rs.getString("estCloture");
				int idNumCli = rs.getInt("idNumCli");

				alResult.add(new CompteCourant(idNumCompte, debitAutorise, solde, estCloture, idNumCli));
			}
			rs.close();
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.CompteCourant, Order.SELECT, "Erreur accès", e);
		}

		return alResult;
	}

	/**
     * Suppression d'un CompteCourant.
     *
     * @param compte Le compte à supprimer
     * @throws DataAccessException        Erreur d'accès aux données (requête mal formée ou autre)
     * @throws DatabaseConnexionException Erreur de connexion
	 * @author Yahya MAGAZ
     */
    public void deleteCompteCourant(CompteCourant compte) throws DataAccessException, DatabaseConnexionException {
		// Supprimer les opérations associées au compte
		deleteOperationsForCompte(compte);
	
		// Supprimer le compte lui-même
		String query = "DELETE FROM CompteCourant WHERE idNumCompte = ?";
		try (Connection con = LogToDatabase.getConnexion(); PreparedStatement pst = con.prepareStatement(query)) {
			pst.setInt(1, compte.idNumCompte);
	
			// Ajout de logs pour le débogage
			System.out.println("Tentative de suppression du compte avec idNumCompte : " + compte.idNumCompte);
	
			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 0) {
				throw new DataAccessException(Table.CompteCourant, Order.DELETE, "Aucune ligne affectée par la suppression", null);
			} else {
				System.out.println("Compte supprimé avec succès, idNumCompte : " + compte.idNumCompte);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Affiche la pile d'appels pour le débogage
			throw new DataAccessException(Table.CompteCourant, Order.DELETE, "Erreur lors de la suppression du compte", e);
		}
	}
	
	/**
	 * Supprime toutes les opérations associées à un compte donné de la base de données.
	 *
	 * @param compte Le compte pour lequel les opérations doivent être supprimées.
	 * @throws DataAccessException      Si une erreur d'accès aux données survient.
	 * @throws DatabaseConnexionException Si une erreur de connexion à la base de données survient.
	 * @author Yahya MAGAZ
	 */
	private void deleteOperationsForCompte(CompteCourant compte) throws DataAccessException, DatabaseConnexionException {
		String query = "DELETE FROM Operation WHERE idNumCompte = ?";
		try (Connection con = LogToDatabase.getConnexion(); PreparedStatement pst = con.prepareStatement(query)) {
			pst.setInt(1, compte.idNumCompte);
	
			// Ajout de logs pour le débogage
			System.out.println("Tentative de suppression des opérations pour le compte avec idNumCompte : " + compte.idNumCompte);
	
			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 0) {
				System.out.println("Aucune opération associée au compte avec idNumCompte : " + compte.idNumCompte);
			} else {
				System.out.println("Opérations associées au compte avec idNumCompte : " + compte.idNumCompte + " supprimées avec succès");
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Affiche la pile d'appels pour le débogage
			throw new DataAccessException(Table.Operation, Order.DELETE, "Erreur lors de la suppression des opérations pour le compte", e);
		}
	}
	
	
}
