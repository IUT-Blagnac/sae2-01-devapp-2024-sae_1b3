package application.view;

import java.io.IOException;
import java.util.List;

import application.DailyBankState;
import application.control.PrelevementManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.CompteCourant;
import model.data.Employe;
import model.data.PrelevementAutomatique;
import model.orm.Access_BD_CompteCourant;
import model.orm.Access_BD_Employe;
import model.orm.Access_BD_PrelevementAutomatiques;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

/**
 * Contrôleur pour la vue de gestion des prélèvements automatiques.
 */
public class PrelevementManagementViewController {

    // Etat courant de l'application
    private DailyBankState dailyBankState;

    @FXML
    private ListView lvPrelevement;

    // Contrôleur de Dialogue associé à PrelevementManagementController
    private PrelevementManagement pmDialogController;

    // Fenêtre physique où est la scène contenant le fichier XML contrôlé par this
    private Stage containingStage;

    // Données de la fenêtre
    private ObservableList<PrelevementAutomatique> oListPrelevements;

    /**
     * Initialise le contexte du contrôleur.
     *
     * @param _containingStage La fenêtre contenant la scène.
     * @param _pm              Le contrôleur de dialogue associé.
     * @param _dbstate         L'état actuel de la banque quotidienne.
     */
    public void initContext(Stage _containingStage, PrelevementManagement _pm, DailyBankState _dbstate) {
        this.pmDialogController = _pm;
        this.containingStage = _containingStage;
        this.dailyBankState = _dbstate;
        this.configure();
        this.validateComponentState();
    }

    private void configure() {
        this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));

        this.oListPrelevements = FXCollections.observableArrayList();
        this.lvPrelevements.setItems(this.oListPrelevements);
        this.lvPrelevements.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.lvPrelevements.getFocusModel().focus(-1);
        this.lvPrelevements.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
        this.validateComponentState();
    }

    /**
     * Affiche la boîte de dialogue de gestion des prélèvements automatiques.
     */
    public void displayDialog(int numeroCompte) {
        this.txtIdNumCompte.setText(String.valueOf(numeroCompte));
        this.containingStage.showAndWait();
    }
    

    // Gestion du stage
    private void closeWindow(WindowEvent e) {
        this.doCancel();
        e.consume();
    }

    // Attributs de la scène + actions

    @FXML
    private TextField txtIdNumCompte;
    @FXML
    private ListView<PrelevementAutomatique> lvPrelevements;
    @FXML
    private Button btnRechercher;

    @FXML
    private void doCancel() {
        this.containingStage.close();
    }

    @FXML
private void loadPrelevements() {
    String idNumCompteText = txtIdNumCompte.getText();
    if (!idNumCompteText.isEmpty()) {
        int idNumCompte = Integer.parseInt(idNumCompteText);
        Access_BD_PrelevementAutomatiques accessBDPrelevements = new Access_BD_PrelevementAutomatiques();
        try {
            System.out.println("ppp");
            List<PrelevementAutomatique> prelevementAutomatiques = accessBDPrelevements.getPrelevements(idNumCompte);
            oListPrelevements.setAll(prelevementAutomatiques);
        } catch (DataAccessException | DatabaseConnexionException e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("ID du compte est vide !");
    }
}


    private void validateComponentState() {
        // Gérer l'état des composants si nécessaire
    }



    private ObservableList<PrelevementAutomatique> oListPrelevementAutomatiques;


    @FXML
    private Button btnSupprimerPrelevement;

    @FXML
private void doSupprimerPrelevement() {
    int selectedIndice = this.lvPrelevements.getSelectionModel().getSelectedIndex();
    if (selectedIndice >= 0) {
        PrelevementAutomatique prelevement = this.oListPrelevements.get(selectedIndice);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Suppression de prélèvement automatique");
        confirm.setHeaderText("Voulez-vous réellement supprimer ce prélèvement automatique?");
        confirm.initOwner(this.containingStage);
        confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        if (confirm.showAndWait().orElse(null) == ButtonType.YES) {
            try {
                new Access_BD_PrelevementAutomatiques().deleteprelevementAutomatique(prelevement);
                this.oListPrelevements.remove(selectedIndice);
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la suppression du prélèvement automatique", e.getMessage());
            }
        }
    }
}

    
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (title != null) {
            alert.setTitle(title);
        }
        if (header != null) {
            alert.setHeaderText(header);
        }
        alert.setContentText(content);
        alert.showAndWait();
    }
    

}
