package application.view;

import java.io.IOException;
import java.util.ArrayList;

import application.DailyBankState;
import application.control.ClientsManagement;
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
import model.data.Client;

/**
 * Contrôleur pour la vue de gestion des clients.
 */
public class ClientsManagementViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ClientsManagementController
	private ClientsManagement cmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private ObservableList<Client> oListClients;

	/**
     * Initialise le contexte du contrôleur.
     *
     * @param _containingStage La fenêtre contenant la scène.
     * @param _cm              Le contrôleur de dialogue associé.
     * @param _dbstate         L'état actuel de la banque quotidienne.
     */
	public void initContext(Stage _containingStage, ClientsManagement _cm, DailyBankState _dbstate) {
		this.cmDialogController = _cm;
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListClients = FXCollections.observableArrayList();
		this.lvClients.setItems(this.oListClients);
		this.lvClients.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvClients.getFocusModel().focus(-1);
		this.lvClients.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
		this.validateComponentState();
	}

	 /**
     * Affiche la boîte de dialogue de gestion des clients.
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
	private TextField txtNum;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private ListView<Client> lvClients;
	@FXML
	private Button btnDesactClient;
	@FXML
	private Button btnModifClient;
	@FXML
	private Button btnComptesClient;

	@FXML
	private void doCancel() {
		this.containingStage.close();
	}

	@FXML
	private void doRechercher() {
		int numCompte;
		try {
			String nc = this.txtNum.getText();
			if (nc.equals("")) {
				numCompte = -1;
			} else {
				numCompte = Integer.parseInt(nc);
				if (numCompte < 0) {
					this.txtNum.setText("");
					numCompte = -1;
				}
			}
		} catch (NumberFormatException nfe) {
			this.txtNum.setText("");
			numCompte = -1;
		}

		String debutNom = this.txtNom.getText();
		String debutPrenom = this.txtPrenom.getText();

		if (numCompte != -1) {
			this.txtNom.setText("");
			this.txtPrenom.setText("");
		} else {
			if (debutNom.equals("") && !debutPrenom.equals("")) {
				this.txtPrenom.setText("");
			}
		}

		// Recherche des clients en BD. cf. AccessClient > getClients(.)
		// numCompte != -1 => recherche sur numCompte
		// numCompte != -1 et debutNom non vide => recherche nom/prenom
		// numCompte != -1 et debutNom vide => recherche tous les clients
		ArrayList<Client> listeCli;
		listeCli = this.cmDialogController.getlisteComptes(numCompte, debutNom, debutPrenom);

		this.oListClients.clear();
		this.oListClients.addAll(listeCli);
		this.validateComponentState();
	}

	@FXML
	private void doComptesClient() {
		int selectedIndice = this.lvClients.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Client client = this.oListClients.get(selectedIndice);
			this.cmDialogController.gererComptesClient(client);
		}
	}

	@FXML
	private void doModifierClient() {

		int selectedIndice = this.lvClients.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Client cliMod = this.oListClients.get(selectedIndice);
			Client result = this.cmDialogController.modifierClient(cliMod);
			if (result != null) {
				this.oListClients.set(selectedIndice, result);
			}
		}
	}

	@FXML
	private void doDesactiverClient() {
	}

	@FXML
	private void doNouveauClient() {
		Client client;
		client = this.cmDialogController.nouveauClient();
		if (client != null) {
			this.oListClients.add(client);
		}
	}

	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnDesactClient.setDisable(true);
		int selectedIndice = this.lvClients.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnModifClient.setDisable(false);
			this.btnComptesClient.setDisable(false);
		} else {
			this.btnModifClient.setDisable(true);
			this.btnComptesClient.setDisable(true);
		}
	}

		/**
	 	* Ouvre la fenêtre de simulation de crédit lorsqu'on clique sur le bouton "Simuler Emprunt".
 		* Cette méthode charge le fichier FXML correspondant, crée une nouvelle scène et un nouveau stage,
 		* puis affiche cette nouvelle fenêtre.
 		*/
	    @FXML
    private void doSimulerEmprunt() {
        try {
            // Charge le fichier FXML pour la vue de simulation de crédit
            FXMLLoader loader = new FXMLLoader(getClass().getResource("simulationCredit.fxml"));
            Parent root = loader.load();

            // Crée une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Crée une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Simulation de Crédit");
            stage.setScene(scene);

            // Affiche la nouvelle fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
