package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.CategorieOperation;
import application.tools.StageManagement;
import application.view.OperationEditorPaneViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.CompteCourant;
import model.data.Operation;

/**
 * La classe OperationEditorPane représente le contrôleur du dialogue pour l'enregistrement d'une opération.
 */
public class OperationEditorPane {

    private Stage oepStage;
    private OperationEditorPaneViewController oepViewController;

    /**
     * Construit une nouvelle instance de OperationEditorPane.
     *
     * @param _parentStage Le stage parent pour le dialogue.
     * @param _dbstate     L'état courant de l'application.
     */
    public OperationEditorPane(Stage _parentStage, DailyBankState _dbstate) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    OperationEditorPaneViewController.class.getResource("operationeditorpane.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, 500 + 20, 250 + 10);
            scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

            this.oepStage = new Stage();
            this.oepStage.initModality(Modality.WINDOW_MODAL);
            this.oepStage.initOwner(_parentStage);
            StageManagement.manageCenteringStage(_parentStage, this.oepStage);
            this.oepStage.setScene(scene);
            this.oepStage.setTitle("Enregistrement d'une opération");
            this.oepStage.setResizable(false);

            this.oepViewController = loader.getController();
            this.oepViewController.initContext(this.oepStage, _dbstate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche le dialogue pour l'enregistrement d'une opération.
     *
     * @param cpte Le compte courant concerné par l'opération.
     * @param cm   La catégorie de l'opération.
     * @return L'opération enregistrée.
     */
    public Operation doOperationEditorDialog(CompteCourant cpte, CategorieOperation cm) {
        return this.oepViewController.displayDialog(cpte, cm);
    }
}
