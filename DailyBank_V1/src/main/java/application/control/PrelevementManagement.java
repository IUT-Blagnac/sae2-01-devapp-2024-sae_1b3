package application.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.PrelevementManagementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.CompteCourant;
import model.data.PrelevementAutomatique;
import model.orm.Access_BD_PrelevementAutomatiques;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

/**
 * Cette classe gère la fenêtre de gestion des prélèvements automatiques dans l'application.
 * Elle permet d'afficher une interface utilisateur pour interagir avec les prélèvements,
 * notamment pour les lire.
 */
public class PrelevementManagement {

    private Stage pmStage;
    private DailyBankState dailyBankState;
    private PrelevementManagementViewController pmViewController;

    /**
     * Constructeur de la classe PrelevementManagement.
     * 
     * @param _parentStage La fenêtre parente à laquelle la fenêtre de gestion des prélèvements est associée.
     * @param _dbstate L'état actuel de la banque quotidienne.
     */
    public PrelevementManagement(Stage _parentStage, DailyBankState _dbstate) {
        this.dailyBankState = _dbstate;
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
     */
    public void doPrelevementManagementDialog() {
        this.pmViewController.displayDialog();
    }

    /**
     * Récupère une liste de prélèvements automatiques en fonction des paramètres de recherche.
     * 
     * @param idNumCompte L'ID du compte associé aux prélèvements.
     * @return La liste des prélèvements automatiques correspondant aux critères de recherche.
     */
    public List<PrelevementAutomatique> getListePrelevements(int idNumCompte) {
        List<PrelevementAutomatique> listePrelev = new ArrayList<>();
        try {
            Access_BD_PrelevementAutomatiques ap = new Access_BD_PrelevementAutomatiques();
            listePrelev = ap.getPrelevements(idNumCompte);
        } catch (DatabaseConnexionException e) {
            ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, e);
            ed.doExceptionDialog();
            this.pmStage.close();
            listePrelev = new ArrayList<>();
        } catch (ApplicationException ae) {
            ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, ae);
            ed.doExceptionDialog();
            listePrelev = new ArrayList<>();
        }
        return listePrelev;
    }

    /**
 * Méthode pour gérer les prélèvements d'un compte.
 * 
 * @param cpt Le compte courant dont les prélèvements doivent être gérés.
 */
public void gererPrelevementsDUnCompte(CompteCourant cpt) {
    try {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PrelevementManagementViewController.class.getResource("prelevementmanagement.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Gestion des Prélèvements");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(pmStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        PrelevementManagementViewController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCompteCourant(cpt);
        
        dialogStage.showAndWait();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
