package application.control;

import java.util.List;
import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.StageManagement;
import application.view.PrelevementManagementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.PrelevementAutomatique;
import model.orm.Access_BD_PrelevementAutomatiques;
import model.orm.exception.ApplicationException;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

/**
 * La classe PrelevementManagement représente un contrôleur pour gérer la fenêtre de gestion des prélèvements automatiques.
 */
public class PrelevementManagement {

    private Stage pmStage;
    private DailyBankState dailyBankState;
    private PrelevementManagementViewController pmViewController;
    private Access_BD_PrelevementAutomatiques prelevementAutomatiquesDB;

    /**
     * Construit une nouvelle instance de PrelevementManagement.
     *
     * @param _parentStage Le stage parent pour la fenêtre de gestion des prélèvements automatiques.
     * @param _dbstate     L'instance de DailyBankState pour gérer l'état de l'application.
     * @author Magaz Yahya
     */
    public PrelevementManagement(Stage _parentStage, DailyBankState _dbstate) {
        this.dailyBankState = _dbstate;
        this.prelevementAutomatiquesDB = new Access_BD_PrelevementAutomatiques();
        try {
            FXMLLoader loader = new FXMLLoader(PrelevementManagementViewController.class.getResource("prelevementmanagement.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
            scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

            this.pmStage = new Stage();
            this.pmStage.initModality(Modality.WINDOW_MODAL);
            this.pmStage.initOwner(_parentStage);
            StageManagement.manageCenteringStage(_parentStage, this.pmStage);
            this.pmStage.setScene(scene);
            this.pmStage.setTitle("Gestion des prélèvements automatiques");
            this.pmStage.setResizable(false);

            this.pmViewController = loader.getController();
            this.pmViewController.initContext(this.pmStage, this, _dbstate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche la fenêtre de gestion des prélèvements automatiques.
     *
     * @param numeroCompte Le numéro de compte pour lequel les prélèvements sont gérés.
    * @author Yahya Magaz

     */
    public void doPrelevementManagementDialog(int numeroCompte) {
        this.pmViewController.displayDialog(numeroCompte);
    }

    /**
     * Supprime un prélèvement automatique existant.
     *
     * @param prelevement Le prélèvement automatique à supprimer.
     * @author Yahya Magaz

     */
    public void supprimerPrelevementAutomatique(PrelevementAutomatique prelevement) {
        if (prelevement != null) {
            try {
                this.prelevementAutomatiquesDB.deleteprelevementAutomatique(prelevement);
                AlertUtilities.showAlert(this.pmStage, "Suppression réussie", "Prélèvement supprimé",
                        "Le prélèvement automatique a été supprimé avec succès", AlertType.INFORMATION);
            } catch (DatabaseConnexionException e) {
                ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, e);
                ed.doExceptionDialog();
            } catch (DataAccessException ae) {
                ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, ae);
                ed.doExceptionDialog();
            }
        }
    }
    
    /**
     * Récupère une liste de prélèvements automatiques pour un compte donné.
     *
     * @param idNumCompte L'ID du compte pour lequel récupérer les prélèvements automatiques.
     * @return Une liste d'objets PrelevementAutomatique représentant les prélèvements automatiques.
     * @throws DatabaseConnexionException S'il y a un problème de connexion à la base de données.
     * @throws ApplicationException Si une exception au niveau de l'application se produit lors de la récupération.
     */
    public List<PrelevementAutomatique> getPrelevementsAutomatiques(int idNumCompte) {
        List<PrelevementAutomatique> listeCli = new ArrayList<>();
        try {
            Access_BD_PrelevementAutomatiques ac = new Access_BD_PrelevementAutomatiques();
            listeCli = ac.getPrelevements(idNumCompte);
        } catch (DatabaseConnexionException e) {
            ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, e);
            ed.doExceptionDialog();
            this.pmStage.close();
        } catch (ApplicationException ae) {
            ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, ae);
            ed.doExceptionDialog();
        }
        return listeCli;
    }
}
