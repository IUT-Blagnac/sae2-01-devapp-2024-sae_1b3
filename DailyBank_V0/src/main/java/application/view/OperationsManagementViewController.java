package application.view;

import java.util.ArrayList;
import java.util.Locale;

import application.DailyBankState;
import application.control.OperationsManagement;
import application.tools.NoSelectionModel;
import application.tools.PairsOfValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;

/**
 * Contrôleur pour la gestion des opérations dans l'application.
 */
public class OperationsManagementViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à OperationsManagementController
	private OperationsManagement omDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private Client clientDuCompte;
	private CompteCourant compteConcerne;
	private ObservableList<Operation> oListOperations;

	/**
     * Initialise le contexte de la fenêtre.
     * @param _containingStage La fenêtre contenant la scène.
     * @param _om Le contrôleur d'opérations.
     * @param _dbstate L'état courant de l'application.
     * @param client Le client associé au compte.
     * @param compte Le compte concerné par les opérations.
     */
	public void initContext(Stage _containingStage, OperationsManagement _om, DailyBankState _dbstate, Client client,
			CompteCourant compte) {
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.omDialogController = _om;
		this.clientDuCompte = client;
		this.compteConcerne = compte;
		this.configure();
	}
	

	private void configure() {
		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListOperations = FXCollections.observableArrayList();
		this.lvOperations.setItems(this.oListOperations);
		this.lvOperations.setSelectionModel(new NoSelectionModel<Operation>());
		this.updateInfoCompteClient();
		this.validateComponentState();
	}
	/**
     * Affiche la fenêtre de gestion des opérations.
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

	// Attributs de la scene + actions

	@FXML
	private Label lblInfosClient;
	@FXML
	private Label lblInfosCompte;
	@FXML
	private ListView<Operation> lvOperations;
	@FXML
	private Button btnDebit;
	@FXML
	private Button btnCredit;

	/**
     * Annule la gestion des opérations et ferme la fenêtre.
     */
	@FXML
	private void doCancel() {
		this.containingStage.close();
	}

	/**
     * Enregistre une opération de débit.
     */
	@FXML
	private void doDebit() {

		Operation op = this.omDialogController.enregistrerDebit();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
     * Enregistre une opération de crédit.
     */
	@FXML
	private void doCredit() {
		Operation op = this.omDialogController.enregistrerCredit();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}	

	/**
     * Enregistre une autre opération (par exemple, un virement).
     */
	@FXML
	private void doAutre() {
    // Appeler une méthode de contrôleur pour enregistrer un virement
    Operation op = this.omDialogController.enregistrerVirement();
    
    // Vérifier si l'opération a été enregistrée avec succès
    if (op != null) {
        // Mettre à jour les informations du compte client
        this.updateInfoCompteClient();
        
        // Valider l'état des composants (par exemple, désactiver certains boutons)
        this.validateComponentState();
    }
}


	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnCredit.setDisable(false);
		this.btnDebit.setDisable(false);
	}

	private void updateInfoCompteClient() {

		PairsOfValue<CompteCourant, ArrayList<Operation>> opesEtCompte;

		opesEtCompte = this.omDialogController.operationsEtSoldeDunCompte();

		ArrayList<Operation> listeOP;
		this.compteConcerne = opesEtCompte.getLeft();
		listeOP = opesEtCompte.getRight();

		String info;
		info = this.clientDuCompte.nom + "  " + this.clientDuCompte.prenom + "  (id : " + this.clientDuCompte.idNumCli
				+ ")";
		this.lblInfosClient.setText(info);

		info = "Cpt. : " + this.compteConcerne.idNumCompte + "  "
				+ String.format(Locale.ENGLISH, "%12.02f", this.compteConcerne.solde) + "  /  "
				+ String.format(Locale.ENGLISH, "%8d", this.compteConcerne.debitAutorise);
		this.lblInfosCompte.setText(info);

		this.oListOperations.clear();
		this.oListOperations.addAll(listeOP);

		this.validateComponentState();
	}
}
