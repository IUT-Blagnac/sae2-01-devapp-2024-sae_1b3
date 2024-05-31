package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.ExceptionDialogViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.orm.exception.ApplicationException;

/**
 * La classe ExceptionDialog représente un dialogue pour afficher les exceptions survenues dans l'application.
 */
public class ExceptionDialog {

    private Stage edStage;
    private ExceptionDialogViewController edViewController;

    /**
     * Construit une nouvelle instance de ExceptionDialog.
     *
     * @param _parentStage Le stage parent pour le dialogue.
     * @param _dbstate     L'état courant de l'application.
     * @param ae           L'exception à afficher.
     */
    public ExceptionDialog(Stage _parentStage, DailyBankState _dbstate, ApplicationException ae) {

        try {
            FXMLLoader loader = new FXMLLoader(ExceptionDialogViewController.class.getResource("exceptiondialog.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, 700 + 20, 400 + 10);
            scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

            this.edStage = new Stage();
            this.edStage.initModality(Modality.WINDOW_MODAL);
            this.edStage.initOwner(_parentStage);
            StageManagement.manageCenteringStage(_parentStage, this.edStage);
            this.edStage.setScene(scene);
            this.edStage.setTitle("Opération impossible");
            this.edStage.setResizable(false);

            this.edViewController = loader.getController();
            this.edViewController.initContext(this.edStage, _dbstate, ae);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche le dialogue d'exception.
     */
    public void doExceptionDialog() {
        this.edViewController.displayDialog();
    }
}
