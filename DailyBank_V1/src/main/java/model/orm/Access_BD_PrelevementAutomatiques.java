package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.data.Employe;
import model.data.PrelevementAutomatique;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
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
}
