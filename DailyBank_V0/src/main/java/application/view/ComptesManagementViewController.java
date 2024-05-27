package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.ComptesManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.orm.Access_BD_CompteCourant;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.RowNotFoundOrTooManyRowsException;

public class ComptesManagementViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ComptesManagementController
	private ComptesManagement cmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private Client clientDesComptes;
	private ObservableList<CompteCourant> oListCompteCourant;

	// Manipulation de la fenêtre
	public void initContext(Stage _containingStage, ComptesManagement _cm, DailyBankState _dbstate, Client client) {
		this.cmDialogController = _cm;
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.clientDesComptes = client;
		this.configure();
	}

	private void configure() {
		String info;

		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListCompteCourant = FXCollections.observableArrayList();
		this.lvComptes.setItems(this.oListCompteCourant);
		this.lvComptes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvComptes.getFocusModel().focus(-1);
		this.lvComptes.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());

		info = this.clientDesComptes.nom + "  " + this.clientDesComptes.prenom + "  (id : "
				+ this.clientDesComptes.idNumCli + ")";
		this.lblInfosClient.setText(info);

		this.loadList();
		this.validateComponentState();
	}

	public void displayDialog() {
		this.containingStage.showAndWait();
	}

	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblInfosClient;
	@FXML
	private ListView<CompteCourant> lvComptes;
	@FXML
	private Button btnVoirOpes;
	@FXML
	private Button btnModifierCompte;
	@FXML
	private Button btnSupprCompte;

	@FXML
	private void doCancel() {
		this.containingStage.close();
	}

	@FXML
	private void doVoirOperations() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			CompteCourant cpt = this.oListCompteCourant.get(selectedIndice);
			this.cmDialogController.gererOperationsDUnCompte(cpt);
		}
		this.loadList();
		this.validateComponentState();
	}

	@FXML
private void doModifierCompte() throws RowNotFoundOrTooManyRowsException, ManagementRuleViolation {
    int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
    if (selectedIndice >= 0) {
        CompteCourant cptMod = this.oListCompteCourant.get(selectedIndice);
        CompteCourant result = this.cmDialogController.modifierCompteCourant(cptMod);
        if (result != null) {
            this.oListCompteCourant.set(selectedIndice, result);
            // Rafraîchir l'affichage de la liste
            this.lvComptes.refresh();
        }
    }
}



	@FXML
private void doSupprimerCompte() {
    int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
    if (selectedIndice >= 0) {
        CompteCourant compte = this.oListCompteCourant.get(selectedIndice);
        
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Suppression de compte");
        confirm.setHeaderText("Voulez-vous réellement supprimer le compte bancaire?");
        confirm.initOwner(this.containingStage);
        confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        if (confirm.showAndWait().orElse(null) == ButtonType.YES) {
            try {
                new Access_BD_CompteCourant().deleteCompteCourant(compte);
                this.oListCompteCourant.remove(selectedIndice);
            } catch (Exception e) {
                showErrorAlert("Erreur lors de la suppression du compte", e.getMessage());
            }
        }
    }
}

private void showErrorAlert(String message, String details) {
    Alert error = new Alert(AlertType.ERROR);
    error.setTitle("Erreur");
    error.setHeaderText(message);
    error.setContentText(details);
    error.showAndWait();
}

	@FXML
	private void doNouveauCompte() {
		CompteCourant compte;
		compte = this.cmDialogController.creerNouveauCompte();
		if (compte != null) {
			this.oListCompteCourant.add(compte);
		}
	}

	private void loadList() {
		ArrayList<CompteCourant> listeCpt;
		listeCpt = this.cmDialogController.getComptesDunClient();
		this.oListCompteCourant.clear();
		this.oListCompteCourant.addAll(listeCpt);
	}

	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnModifierCompte.setDisable(true);
		this.btnSupprCompte.setDisable(true);
	
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnVoirOpes.setDisable(false);
			this.btnModifierCompte.setDisable(false);
			this.btnSupprCompte.setDisable(false);

		} else {
			this.btnVoirOpes.setDisable(true);
			this.btnModifierCompte.setDisable(true);
			this.btnSupprCompte.setDisable(true);

		}
	}
}
