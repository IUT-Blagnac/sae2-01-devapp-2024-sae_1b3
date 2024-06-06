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
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;
import model.orm.exception.Order;

public class Access_BD_PrelevementAutomatiques {

    public Access_BD_PrelevementAutomatiques() {
    }

    /**
     * Recherche de tous les prélèvements automatiques pour un compte spécifique.
     *
     * @param idNumCompte 
     * @return La liste des prélèvements automatiques trouvés, liste vide si aucun.
     * @throws DataAccessException        
     * @throws DatabaseConnexionException 
     */
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
            throw new DataAccessException(Table.PrelevementAutomatique, Order.SELECT, "Erreur accès", e);
        }

        return prelevements;
    }

    /**
     * Recherche d'un prélèvement automatique par son ID.
     *
     * @param idPrelev 
     * @return Le prélèvement automatique trouvé ou null si non trouvé.
     * @throws RowNotFoundOrTooManyRowsException 
     * @throws DataAccessException               
     * @throws DatabaseConnexionException        
     */
    public PrelevementAutomatique getPrelevement(int idPrelev)
            throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {

        PrelevementAutomatique prelevement = null;

        try {
            Connection con = LogToDatabase.getConnexion();

            String query = "SELECT * FROM PrelevementAutomatique WHERE idPrelev = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, idPrelev);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int idNumCompte = rs.getInt("idNumCompte");
                double montant = rs.getDouble("montant");
                String dateRecurrente = rs.getString("dateRecurrente");
                String beneficiaire = rs.getString("beneficiaire");

                prelevement = new PrelevementAutomatique(idPrelev, idNumCompte, montant, dateRecurrente, beneficiaire);
            } else {
                rs.close();
                pst.close();
                throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.SELECT, "Aucun prélèvement trouvé", null, 0);
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new DataAccessException(Table.PrelevementAutomatique, Order.SELECT, "Erreur accès", e);
        }

        return prelevement;
    }

    /**
     * Insertion d'un nouveau prélèvement automatique.
     *
     * @param prelevement 
     * @throws RowNotFoundOrTooManyRowsException 
     * @throws DataAccessException               
     * @throws DatabaseConnexionException        
     */
    public void insertPrelevement(PrelevementAutomatique prelevement)
            throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
        try {
            Connection con = LogToDatabase.getConnexion();

            String query = "INSERT INTO PrelevementAutomatique (idPrelev, montant, dateRecurrente, beneficiaire, idNumCompte) VALUES (seq_id_prelev.NEXTVAL, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setDouble(1, prelevement.montant);
            pst.setString(2, prelevement.dateRecurrente);
            pst.setString(3, prelevement.beneficiaire);
            pst.setInt(4, prelevement.idNumCompte);

            int result = pst.executeUpdate();
            pst.close();

            if (result != 1) {
                con.rollback();
                throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.INSERT, "Insert anormal (insert de moins ou plus d'une ligne)", null, result);
            }

            query = "SELECT seq_id_prelev.CURRVAL FROM DUAL";
            PreparedStatement pst2 = con.prepareStatement(query);
            ResultSet rs = pst2.executeQuery();
            if (rs.next()) {
                prelevement.idPrelev = rs.getInt(1);
            }

            con.commit();
            rs.close();
            pst2.close();
        } catch (SQLException e) {
            throw new DataAccessException(Table.PrelevementAutomatique, Order.INSERT, "Erreur accès", e);
        }
    }

    /**
     * Mise à jour d'un prélèvement automatique existant.
     *
     * @param prelevement 
     * @throws RowNotFoundOrTooManyRowsException 
     * @throws DataAccessException               
     * @throws DatabaseConnexionException        
     */
    public void updatePrelevement(PrelevementAutomatique prelevement)
            throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
        try {
            Connection con = LogToDatabase.getConnexion();

            String query = "UPDATE PrelevementAutomatique SET montant = ?, dateRecurrente = ?, beneficiaire = ?, idNumCompte = ? WHERE idPrelev = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setDouble(1, prelevement.montant);
            pst.setString(2, prelevement.dateRecurrente);
            pst.setString(3, prelevement.beneficiaire);
            pst.setInt(4, prelevement.idNumCompte);
            pst.setInt(5, prelevement.idPrelev);

            int result = pst.executeUpdate();
            pst.close();
            if (result != 1) {
                con.rollback();
                throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.UPDATE, "Update anormal (update de moins ou plus d'une ligne)", null, result);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException(Table.PrelevementAutomatique, Order.UPDATE, "Erreur accès", e);
        }
    }

    /**
     * Suppression d'un prélèvement automatique par son ID.
     *
     * @param idPrelev 
     * @throws RowNotFoundOrTooManyRowsException 
     * @throws DataAccessException               
     * @throws DatabaseConnexionException        
     */
    public void deletePrelevement(int idPrelev)
            throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
        try {
            Connection con = LogToDatabase.getConnexion();

            String query = "DELETE FROM PrelevementAutomatique WHERE idPrelev = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, idPrelev);

            int result = pst.executeUpdate();
            pst.close();
            if (result != 1) {
                con.rollback();
                throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.DELETE, "Delete anormal (delete de moins ou plus d'une ligne)", null, result);
            }
            con.commit();
        } catch (SQLException e) {
            throw new DataAccessException(Table.PrelevementAutomatique, Order.DELETE, "Erreur accès", e);
        }
    }
}
