package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.control.PrelevementManagement;
import model.data.CompteCourant;
import model.data.Employe;
import model.data.PrelevementAutomatique;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.Table;
import model.orm.LogToDatabase;

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

    public void deleteprelevementAutomatique(PrelevementAutomatique prelevement) throws DataAccessException, DatabaseConnexionException {

        String query = "DELETE FROM PrelevementAutomatique WHERE idPrelev = ?";
        try (Connection con = LogToDatabase.getConnexion(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, prelevement.getIdPrelev()); // Utiliser l'identifiant unique du prélèvement
    
            // Ajout de logs pour le débogage
            System.out.println("Tentative de suppression du prélèvement automatique avec idPrelevement : " + prelevement.getIdPrelev());
    
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataAccessException(Table.PrelevementAutomatique, Order.DELETE, "Aucune ligne affectée par la suppression", null);
            } else {
                System.out.println("Prélèvement automatique supprimé avec succès, idPrelevement : " + prelevement.getIdPrelev());
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Affiche la pile d'appels pour le débogage
            throw new DataAccessException(Table.PrelevementAutomatique, Order.DELETE, "Erreur lors de la suppression du prélèvement automatique", e);
        }
    }

    public void addPrelevement(PrelevementAutomatique prelevement) throws DataAccessException, DatabaseConnexionException {
        try (Connection con = LogToDatabase.getConnexion()) {
            
            // Query to insert a new PrelevementAutomatique
            String query = "INSERT INTO PrelevementAutomatique(idPrelev, idNumCompte, montant, dateRecurrente, beneficiaire) VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement pst = con.prepareStatement(query)) {
                // Assuming idPrelev is generated as an auto-increment field by the database
                pst.setInt(1, prelevement.getIdPrelev());
                pst.setInt(2, prelevement.getIdNumCompte());
                pst.setDouble(3, prelevement.getMontant());
                pst.setString(4, prelevement.getDateRecurrente());
                pst.setString(5, prelevement.getBeneficiaire());
                pst.executeUpdate();
			    pst.close();
                
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(Table.PrelevementAutomatique, Order.INSERT, "Erreur lors de l'ajout du prélèvement automatique", e);
        }
    }
    


    }


