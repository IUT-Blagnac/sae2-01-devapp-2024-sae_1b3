package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.LoginDialogViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Employe;
import model.orm.Access_BD_Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

/**
 * La classe LoginDialog représente le contrôleur du dialogue de la fenêtre de login.
 */
public class LoginDialog {

    // Stage de la fenêtre construite par LoginDialog
    private Stage ldStage;

    // Etat courant de l'application
    private DailyBankState dailyBankState;

    // Controleur de vue associé à LoginDialog
    private LoginDialogViewController ldViewController;

    /**
     * Crée une nouvelle instance de LoginDialog.
     *
     * @param _parentStage La fenêtre parente de LoginDialog (sur laquelle se centrer et être modale)
     * @param _dbstate     L'état courant de l'application
     */
    public LoginDialog(Stage _parentStage, DailyBankState _dbstate) {
        this.dailyBankState = _dbstate;
        try {
            FXMLLoader loader = new FXMLLoader(LoginDialogViewController.class.getResource("logindialog.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
            scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

            this.ldStage = new Stage();
            this.ldStage.initModality(Modality.WINDOW_MODAL);
            this.ldStage.initOwner(_parentStage);
            StageManagement.manageCenteringStage(_parentStage, this.ldStage);
            this.ldStage.setScene(scene);
            this.ldStage.setTitle("Identification");
            this.ldStage.setResizable(false);

            this.ldViewController = loader.getController();
            this.ldViewController.initContext(this.ldStage, this, _dbstate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche le dialogue de login.
     */
    public void doLoginDialog() {
        this.ldViewController.displayDialog();
    }

    /**
     * Recherche un employé par son login/mot de passe.
     *
     * @param login    Le login recherché
     * @param password Le mot de passe recherché
     * @return L'employé trouvé, ou null s'il n'existe pas
     */
    public Employe chercherEmployeParLogin(String login, String password) {
        Employe employe = null;
        Access_BD_Employe aebd;

        try {
            aebd = new Access_BD_Employe();
            employe = aebd.getEmploye(login, password);

            // Si un employé est trouvé, modifier l'état de l'application
            if (employe != null) {
                this.dailyBankState.setEmployeActuel(employe);
            }
        } catch (DatabaseConnexionException e) {
            ExceptionDialog ed = new ExceptionDialog(this.ldStage, this.dailyBankState, e);
            ed.doExceptionDialog();
            this.dailyBankState.setEmployeActuel(null);
            this.ldStage.close();
            employe = null;
        } catch (ApplicationException ae) {
            ExceptionDialog ed = new ExceptionDialog(this.ldStage, this.dailyBankState, ae);
            ed.doExceptionDialog();
            this.dailyBankState.setEmployeActuel(null);
            this.ldStage.close();
            employe = null;
        }
        return employe;
    }

    /**
     * Annule le login en cours (aucun employé connecté).
     */
    public void annulerLogin() {
        this.dailyBankState.setEmployeActuel(null);
    }
}
