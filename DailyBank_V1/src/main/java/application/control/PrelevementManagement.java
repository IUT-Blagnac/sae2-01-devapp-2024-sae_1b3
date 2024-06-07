package application.control;

import java.util.ArrayList;
import java.util.List;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.PrelevementManagementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.PrelevementAutomatique;
import model.orm.Access_BD_PrelevementAutomatiques;
import model.orm.exception.ApplicationException;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

public class PrelevementManagement {

    private Stage pmStage;
    private DailyBankState dailyBankState;
    private PrelevementManagementViewController pmViewController;
    private Access_BD_PrelevementAutomatiques prelevementAutomatiquesDB;

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

    public void doPrelevementManagementDialog(int numeroCompte) {
        this.pmViewController.displayDialog(numeroCompte);
    }


    public void gererPrelevementsDUnCompte(Client c){
        ComptesManagement cm = new ComptesManagement(this.pmStage, this.dailyBankState, null);
        cm.doComptesManagementDialog();
    }

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
