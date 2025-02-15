package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.view.DailyBankMainFrameViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.orm.LogToDatabase;
import model.orm.exception.DatabaseConnexionException;

/**
 * La classe DailyBankMainFrame est le contrôleur de la fenêtre principale de l'application.
 * Elle gère le démarrage de l'application, la connexion/déconnexion des employés et la gestion des clients.
 */
public class DailyBankMainFrame extends Application {

    // État courant de l'application
    private DailyBankState dailyBankState;

    // Stage de la fenêtre principale construite par DailyBankMainFrame
    private Stage dbmfStage;

    /**
     * Méthode de démarrage (JavaFX).
     */
    @Override
    public void start(Stage primaryStage) {

        this.dbmfStage = primaryStage;

        try {

            // Création de l'état courant de l'application
            this.dailyBankState = new DailyBankState();
            this.dailyBankState.setEmployeActuel(null);

            // Chargement du fichier FXML
            FXMLLoader loader = new FXMLLoader(
                    DailyBankMainFrameViewController.class.getResource("dailybankmainframe.fxml"));
            BorderPane root = loader.load();

            // Paramétrage du Stage : feuille de style, titre
            Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
            scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

            this.dbmfStage.setScene(scene);
            this.dbmfStage.setTitle("Fenêtre Principale");

            // Récupération du contrôleur et initialisation (stage, contrôleur de dialogue, état courant)
            DailyBankMainFrameViewController dbmfViewController = loader.getController();
            dbmfViewController.initContext(this.dbmfStage, this, this.dailyBankState);

            dbmfViewController.displayDialog();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Méthode principale de lancement de l'application.
     */
    public static void runApp() {
        Application.launch();
    }

    /**
     * Réaliser la déconnexion de l'application.
     */
    public void deconnexionEmploye() {
        this.dailyBankState.setEmployeActuel(null);
        try {
            LogToDatabase.closeConnexion();
        } catch (DatabaseConnexionException e) {
            ExceptionDialog ed = new ExceptionDialog(this.dbmfStage, this.dailyBankState, e);
            ed.doExceptionDialog();
        }
    }

    /**
     * Lancer la connexion de l'utilisateur (login/mdp employé).
     */
    public void loginDunEmploye() {
        LoginDialog ld = new LoginDialog(this.dbmfStage, this.dailyBankState);
        ld.doLoginDialog();
    }

    /**
     * Lancer la gestion des clients (liste des clients).
     */
    public void gestionClients() {
        ClientsManagement cm = new ClientsManagement(this.dbmfStage, this.dailyBankState);
        cm.doClientManagementDialog();
    }
}
