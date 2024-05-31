package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.data.Employe;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

/**
 * Classe d'accès aux Employe en BD Oracle.
 */
public class Access_BD_Employe {

	public Access_BD_Employe() {
	}

	/**
	 * Recherche d'un employé par son login / mot de passe.
	 *
	 * @param login    login de l'employé recherché
	 * @param password mot de passe donné
	 * @return un Employe ou null si non trouvé
	 * @throws RowNotFoundOrTooManyRowsException La requête renvoie plus de 1 ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 */
	public Employe getEmploye(String login, String password)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {

		Employe employeTrouve;

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM Employe WHERE" + " login = ?" + " AND motPasse = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, login);
			pst.setString(2, password);

			ResultSet rs = pst.executeQuery();

			System.err.println(query);

			if (rs.next()) {
				int idEmployeTrouve = rs.getInt("idEmploye");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String droitsAccess = rs.getString("droitsAccess");
				String loginTROUVE = rs.getString("login");
				String motPasseTROUVE = rs.getString("motPasse");
				int idAgEmploye = rs.getInt("idAg");

				employeTrouve = new Employe(idEmployeTrouve, nom, prenom, droitsAccess, loginTROUVE, motPasseTROUVE,
						idAgEmploye);
			} else {
				rs.close();
				pst.close();
				// Non trouvé
				return null;
			}

			if (rs.next()) {
				// Trouvé plus de 1 ... bizarre ...
				rs.close();
				pst.close();
				throw new RowNotFoundOrTooManyRowsException(Table.Employe, Order.SELECT,
						"Recherche anormale (en trouve au moins 2)", null, 2);
			}
			rs.close();
			pst.close();
			return employeTrouve;
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.SELECT, "Erreur accès", e);
		}
	}

	/**
 	* Récupère tous les employés de la base de données.
 	* @return Une liste contenant tous les employés.
 	* @throws DataAccessException Si une erreur d'accès aux données survient.
 	* @throws DatabaseConnexionException Si une erreur de connexion à la base de données survient.
	* @author Théo Raban
 	*/
	public List<Employe> getAllEmployes() throws DataAccessException, DatabaseConnexionException {
		List<Employe> employes = new ArrayList<>();
	
		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM Employe";
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
	
			while (rs.next()) {
				int idEmploye = rs.getInt("idEmploye");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String droitsAccess = rs.getString("droitsAccess");
				String login = rs.getString("login");
				String motPasse = rs.getString("motPasse");
				int idAg = rs.getInt("idAg");
	
				Employe employe = new Employe(idEmploye, nom, prenom, droitsAccess, login, motPasse, idAg);
				employes.add(employe);
			}
			rs.close();
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.SELECT, "Erreur accès", e);
		}
		System.out.println("Employés récupérés: " + employes.size()); // Ligne de débogage
		return employes;
	}

	/**
     * Ajoute un nouvel employé à la base de données.
     *
     * @param employe L'employé à ajouter.
     * @throws DataAccessException      Si une erreur d'accès aux données survient.
     * @throws DatabaseConnexionException Si une erreur de connexion à la base de données survient.
	 * @author Théo Raban
     */
	public void addEmploye(Employe employe) throws DataAccessException, DatabaseConnexionException {
		try {
			Connection con = LogToDatabase.getConnexion();
			
			// Compter le nombre d'employés existants
			String countQuery = "SELECT COUNT(*) FROM Employe";
			PreparedStatement countStatement = con.prepareStatement(countQuery);
			ResultSet resultSet = countStatement.executeQuery();
			resultSet.next();
			int existingCount = resultSet.getInt(1);
			
			// Ajouter 1 pour obtenir un nouvel ID unique
			int newId = existingCount + 1;
	
			String query = "INSERT INTO Employe (idEmploye, nom, prenom, droitsAccess, login, motPasse, idAg) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, newId);  // Utiliser le nouvel ID
			pst.setString(2, employe.getNom());
			pst.setString(3, employe.getPrenom());
			pst.setString(4, employe.getDroitsAccess());
			pst.setString(5, employe.getLogin());
			pst.setString(6, employe.getMotPasse());
			pst.setInt(7, employe.getIdAg());
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.INSERT, "Erreur accès", e);
		}
	}

	/**
     * Met à jour les informations d'un employé dans la base de données.
     *
     * @param employe L'employé avec les nouvelles informations.
     * @throws DataAccessException      Si une erreur d'accès aux données survient.
     * @throws DatabaseConnexionException Si une erreur de connexion à la base de données survient.
	 *@author Yahya MAGAZ	
	 */

	public void updateEmploye(Employe employe) throws DataAccessException, DatabaseConnexionException {
		try {
			Connection con = LogToDatabase.getConnexion();
	
			String query = "UPDATE Employe SET nom=?, prenom=?, droitsAccess=?, login=?, motPasse=?, idAg=? WHERE idEmploye=?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, employe.getNom());
			pst.setString(2, employe.getPrenom());
			pst.setString(3, employe.getDroitsAccess());
			pst.setString(4, employe.getLogin());
			pst.setString(5, employe.getMotPasse());
			pst.setInt(6, employe.getIdAg());
			pst.setInt(7, employe.getIdEmploye());
	
			int rowsAffected = pst.executeUpdate();
			pst.close();
	
			if (rowsAffected == 0) {
				throw new DataAccessException(Table.Employe, Order.UPDATE, "Aucune ligne mise à jour", null);
			}
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.UPDATE, "Erreur accès", e);
		}
	}
	

	/**
     * Récupère un employé à partir de son login.
     *
     * @param login Le login de l'employé à rechercher.
     * @return L'employé correspondant au login, ou null s'il n'existe pas.
     * @throws DataAccessException      Si une erreur d'accès aux données survient.
     * @throws DatabaseConnexionException Si une erreur de connexion à la base de données survient.
     */
	public Employe getEmployeByLogin(String login) throws DataAccessException, DatabaseConnexionException {
		Employe employeTrouve = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM Employe WHERE login = ?";
			pst = con.prepareStatement(query);
			pst.setString(1, login);
			rs = pst.executeQuery();
	
			if (rs.next()) {
				int idEmploye = rs.getInt("idEmploye");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String droitsAccess = rs.getString("droitsAccess");
				String motPasse = rs.getString("motPasse");
				int idAg = rs.getInt("idAg");
	
				employeTrouve = new Employe(idEmploye, nom, prenom, droitsAccess, login, motPasse, idAg);
			}
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.SELECT, "Erreur accès", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				// Gérer les erreurs de fermeture de la connexion, du statement et du resultset
				e.printStackTrace();
			}
		}
	
		return employeTrouve;
	}

	/**
 	* Supprime un employé de la base de données.
 	* @param employe L'employé à supprimer.
 	* @throws DataAccessException Si une erreur d'accès aux données survient.
 	* @throws DatabaseConnexionException Si une erreur de connexion à la base de données survient.
	* @author Thomas CEOLIN
 	*/
	public void deleteEmploye(Employe employe) throws DataAccessException, DatabaseConnexionException {
		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "DELETE FROM Employe WHERE idEmploye = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, employe.getIdEmploye());
			int rowsAffected = pst.executeUpdate();
			pst.close();
	
			if (rowsAffected == 0) {
				throw new DataAccessException(Table.Employe, Order.DELETE, "Aucune ligne supprimée", null);
			}
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.DELETE, "Erreur accès", e);
		}
	}
	
	
}

