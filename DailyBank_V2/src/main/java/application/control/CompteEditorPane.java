package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.CompteEditorPaneViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;

/**
 * La classe CompteEditorPane représente un contrôleur pour gérer le volet d'édition de compte.
 * Elle fournit des méthodes pour initialiser et afficher la boîte de dialogue d'édition de compte.
 */
public class CompteEditorPane {

    private Stage cepStage;
    private CompteEditorPaneViewController cepViewController;

    /**
     * Construit une nouvelle instance de CompteEditorPane.
     *
     * @param _parentStage Le stage parent pour le volet d'édition.
     * @param _dbstate     L'instance de DailyBankState pour gérer l'état de l'application.
     */
    public CompteEditorPane(Stage _parentStage, DailyBankState _dbstate) {
        try {
            FXMLLoader loader = new FXMLLoader(CompteEditorPaneViewController.class.getResource("compteeditorpane.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
            scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

            this.cepStage = new Stage();
            this.cepStage.initModality(Modality.WINDOW_MODAL);
            this.cepStage.initOwner(_parentStage);
            StageManagement.manageCenteringStage(_parentStage, this.cepStage);
            this.cepStage.setScene(scene);
            this.cepStage.setTitle("Gestion d'un compte");
            this.cepStage.setResizable(false);

            this.cepViewController = loader.getController();
            this.cepViewController.initContext(this.cepStage, _dbstate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche la boîte de dialogue d'édition de compte.
     *
     * @param client Le client dont le compte est en cours d'édition.
     * @param cpte   Le compte en cours d'édition.
     * @param em     Le mode d'édition (AJOUTER, MODIFIER, SUPPRIMER).
     * @return Le compte édité.
     */
    public CompteCourant doCompteEditorDialog(Client client, CompteCourant cpte, EditionMode em) {
        return this.cepViewController.displayDialog(client, cpte, em);
    }
}
