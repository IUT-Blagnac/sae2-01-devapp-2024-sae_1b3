package application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.DailyBankState;
import application.control.PrelevementManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.CompteCourant;
import model.data.PrelevementAutomatique;

/**
 * Contrôleur pour la vue de gestion des prélèvements automatiques.
 */
public class PrelevementManagementViewController {

    // Etat courant de l'application
    private DailyBankState dailyBankState;

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
    public void displayDialog() {
        this.containingStage.showAndWait();
    }

    // Gestion du stage
    private Object closeWindow(WindowEvent e) {
        this.doCancel();
        e.consume();
        return null;
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
    private void doRechercher() {
        int idNumCompte;
        try {
            String nc = this.txtIdNumCompte.getText();
            if (nc.equals("")) {
                idNumCompte = -1;
            } else {
                idNumCompte = Integer.parseInt(nc);
                if (idNumCompte < 0) {
                    this.txtIdNumCompte.setText("");
                    idNumCompte = -1;
                }
            }
        } catch (NumberFormatException nfe) {
            this.txtIdNumCompte.setText("");
            idNumCompte = -1;
        }

        List<PrelevementAutomatique> listePrelev;
        listePrelev = this.pmDialogController.getListePrelevements(idNumCompte);

        this.oListPrelevements.clear();
        this.oListPrelevements.addAll(listePrelev);
        this.validateComponentState();
    }

    private void validateComponentState() {
        // Gérer l'état des composants si nécessaire
    }

    
}
