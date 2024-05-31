package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ClientEditorPaneViewController;
import application.view.ClientsManagementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;

/**
 * Cette classe représente une fenêtre de modification de client dans l'application.
 * Elle permet d'afficher une interface utilisateur pour modifier les détails d'un client existant
 * ou en ajouter un nouveau.
 */
public class ClientEditorPane {

    // La scène de la fenêtre de modification de client
    private Stage cepStage;

    // Le contrôleur de la vue associée à la fenêtre
    private ClientEditorPaneViewController cepViewController;

    // L'état actuel de la banque quotidienne
    private DailyBankState dailyBankState;

    /**
     * Constructeur de la classe ClientEditorPane.
     * 
     * @param _parentStage La fenêtre parente à laquelle la fenêtre de modification de client est associée.
     * @param _dbstate L'état actuel de la banque quotidienne.
     */
    public ClientEditorPane(Stage _parentStage, DailyBankState _dbstate) {
        this.dailyBankState = _dbstate;
        try {
            // Chargement de l'interface utilisateur à partir du fichier FXML
            FXMLLoader loader = new FXMLLoader(ClientsManagementViewController.class.getResource("clienteditorpane.fxml"));
            BorderPane root = loader.load();

            // Création de la scène et ajout de styles
            Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
            scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

            // Initialisation de la fenêtre de modification de client
            this.cepStage = new Stage();
            this.cepStage.initModality(Modality.WINDOW_MODAL);
            this.cepStage.initOwner(_parentStage);
            StageManagement.manageCenteringStage(_parentStage, this.cepStage);
            this.cepStage.setScene(scene);
            this.cepStage.setTitle("Gestion d'un client");
            this.cepStage.setResizable(false);

            // Initialisation du contrôleur de la vue associée
            this.cepViewController = loader.getController();
            this.cepViewController.initContext(this.cepStage, this.dailyBankState);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche la fenêtre de modification de client en tant que boîte de dialogue modale.
     * 
     * @param client Le client à modifier ou à ajouter.
     * @param em Le mode d'édition, soit EditionMode.EDIT pour modifier, soit EditionMode.NEW pour ajouter.
     * @return Le client modifié ou ajouté.
     */
    public Client doClientEditorDialog(Client client, EditionMode em) {
        return this.cepViewController.displayDialog(client, em);
    }
}
