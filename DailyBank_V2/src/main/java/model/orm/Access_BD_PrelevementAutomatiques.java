package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.data.PrelevementAutomatique;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.Table;

public class Access_BD_PrelevementAutomatiques {

    public Access_BD_PrelevementAutomatiques() {
    }

    public List<PrelevementAutomatique> getPrelevements(int idNumCompte)
            throws DataAccessException, DatabaseConnexionException {

        List<PrelevementAutomatique> prelevements = new ArrayList<>();

        try {
            Connection con = LogToDatabase.getConnexion();

            String query = "SELECT * FROM PrelevementAutomatique WHERE idNumCompte = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, idNumCompte);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int idPrelev = rs.getInt("idPrelev");
                double montant = rs.getDouble("montant");
                String dateRecurrente = rs.getString("dateRecurrente");
                String beneficiaire = rs.getString("beneficiaire");

                prelevements.add(new PrelevementAutomatique(idPrelev, idNumCompte, montant, dateRecurrente, beneficiaire));
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new DataAccessException(null, null, "Erreur lors de la récupération des prélèvements automatiques.", e);
        }
        System.out.println("nb prelevements: " + prelevements.size());
        return prelevements;
    }

    public List<PrelevementAutomatique> getTousLesPrelevements() throws DataAccessException, DatabaseConnexionException {
        List<PrelevementAutomatique> prelevements = new ArrayList<>();
        try {
            Connection con = LogToDatabase.getConnexion();

            String query = "SELECT * FROM PrelevementAutomatique";
            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int idPrelev = rs.getInt("idPrelev");
                int idNumCompte = rs.getInt("idNumCompte");
                double montant = rs.getDouble("montant");
                String dateRecurrente = rs.getString("dateRecurrente");
                String beneficiaire = rs.getString("beneficiaire");

                prelevements.add(new PrelevementAutomatique(idPrelev, idNumCompte, montant, dateRecurrente, beneficiaire));
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new DataAccessException(null, null, "Erreur lors de la récupération des prélèvements automatiques.", e);
        }
        return prelevements;
    }

    public void deleteprelevementAutomatique(PrelevementAutomatique prelevement) throws DataAccessException, DatabaseConnexionException {
        String query = "DELETE FROM PrelevementAutomatique WHERE idPrelev = ?";
        try (Connection con = LogToDatabase.getConnexion(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, prelevement.getIdPrelev());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataAccessException(Table.PrelevementAutomatique, Order.DELETE, "Aucune ligne affectée par la suppression", null);
            }
        } catch (SQLException e) {
            throw new DataAccessException(Table.PrelevementAutomatique, Order.DELETE, "Erreur lors de la suppression du prélèvement automatique", e);
        }
    }

    public void addPrelevement(PrelevementAutomatique prelevement) throws DataAccessException, DatabaseConnexionException {
        try {
            Connection con = LogToDatabase.getConnexion();
            
            // Compter le nombre de prélèvements automatiques existants
            String countQuery = "SELECT COUNT(*) FROM PrelevementAutomatique";
            PreparedStatement countStatement = con.prepareStatement(countQuery);
            ResultSet resultSet = countStatement.executeQuery();
            resultSet.next();
            int existingCount = resultSet.getInt(1);
            
            // Ajouter 1 pour obtenir un nouvel ID unique
            int newId = existingCount + 1;
    
            String query = "INSERT INTO PrelevementAutomatique (idPrelev, idNumCompte, montant, dateRecurrente, beneficiaire) VALUES (?, ?, ?, ?, ?)";
            
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, newId);  // Utiliser le nouvel ID
            pst.setInt(2, prelevement.getIdNumCompte());
            pst.setDouble(3, prelevement.getMontant());
            pst.setString(4, prelevement.getDateRecurrente());
            pst.setString(5, prelevement.getBeneficiaire());

    
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            throw new DataAccessException(Table.PrelevementAutomatique, Order.INSERT, "Erreur accès", e);
        }
    }


/**
 * Met à jour les informations d'un prélèvement automatique dans la base de données.
 *
 * @param prelevement Le prélèvement automatique avec les nouvelles informations.
 * @throws DataAccessException      Si une erreur d'accès aux données survient.
 * @throws DatabaseConnexionException Si une erreur de connexion à la base de données survient.
 */
public void updatePrelevement(PrelevementAutomatique prelevement) throws DataAccessException, DatabaseConnexionException {
    try {
        Connection con = LogToDatabase.getConnexion();

        String query = "UPDATE PrelevementAutomatique SET montant=?, dateRecurrente=?, beneficiaire=? WHERE idPrelev=?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setDouble(1, prelevement.getMontant());
        pst.setString(2, prelevement.getDateRecurrente());
        pst.setString(3, prelevement.getBeneficiaire());
        pst.setInt(4, prelevement.getIdPrelev());

        int rowsAffected = pst.executeUpdate();
        pst.close();

        if (rowsAffected == 0) {
            throw new DataAccessException(Table.PrelevementAutomatique, Order.UPDATE, "Aucune ligne mise à jour", null);
        }
    } catch (SQLException e) {
        throw new DataAccessException(Table.PrelevementAutomatique, Order.UPDATE, "Erreur accès", e);
    }
}


    
    
    
}
