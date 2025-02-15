package application.view;

import java.util.Locale;

import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Contrôleur pour la vue de l'édition des comptes.
 */
public class CompteEditorPaneViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private EditionMode editionMode;
	private Client clientDuCompte;
	private CompteCourant compteEdite;
	private CompteCourant compteResultat;

/**
     * Initialise le contexte du contrôleur.
     *
     * @param _containingStage La fenêtre contenant la scène.
     * @param _dbstate         L'état actuel de la banque quotidienne.
     */
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.txtDecAutorise.focusedProperty().addListener((t, o, n) -> this.focusDecouvert(t, o, n));
		this.txtSolde.focusedProperty().addListener((t, o, n) -> this.focusSolde(t, o, n));
	}

	/**
     * Affiche la boîte de dialogue d'édition de compte.
     *
     * @param client Le client associé au compte.
     * @param cpte   Le compte à éditer.
     * @param mode   Le mode d'édition.
     * @return Le compte édité ou null.
     */
	public CompteCourant displayDialog(Client client, CompteCourant cpte, EditionMode mode) {
		this.clientDuCompte = client;
		this.editionMode = mode;
		if (cpte == null) {
			this.compteEdite = new CompteCourant(0, 200, 0, "N", this.clientDuCompte.idNumCli);
		} else {
			this.compteEdite = new CompteCourant(cpte);
		}
		this.compteResultat = null;
		this.txtIdclient.setDisable(true);
		this.txtIdAgence.setDisable(true);
		this.txtIdNumCompte.setDisable(true);
		switch (mode) {
		case CREATION:
			this.txtDecAutorise.setDisable(false);
			this.txtSolde.setDisable(false);
			this.lblMessage.setText("Informations sur le nouveau compte");
			this.lblSolde.setText("Solde (premier dépôt)");
			this.btnOk.setText("Ajouter");
			this.btnCancel.setText("Annuler");
			break;
			case MODIFICATION:
			this.txtDecAutorise.setDisable(false);
			this.txtSolde.setDisable(false);
			this.lblMessage.setText("Modification des informations du compte");
			this.lblSolde.setText("Nouveau solde");
			this.btnOk.setText("Modifier");
			this.btnCancel.setText("Annuler");
			break;
		case SUPPRESSION:
			this.txtDecAutorise.setDisable(true);
			this.txtSolde.setDisable(true);
			this.lblMessage.setText("Suppression du compte");
			this.lblSolde.setText("Solde");
			this.btnOk.setText("Supprimer");
			this.btnCancel.setText("Annuler");
			break;
		}

		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}

		// initialisation du contenu des champs
		this.txtIdclient.setText("" + this.compteEdite.idNumCli);
		this.txtIdNumCompte.setText("" + this.compteEdite.idNumCompte);
		this.txtIdAgence.setText("" + this.dailyBankState.getEmployeActuel().idAg);
		this.txtDecAutorise.setText("" + this.compteEdite.debitAutorise);
		this.txtSolde.setText(String.format(Locale.ENGLISH, "%10.02f", this.compteEdite.solde));

		this.compteResultat = null;

		this.containingStage.showAndWait();
		return this.compteResultat;
	}

	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	private Object focusDecouvert(ObservableValue<? extends Boolean> txtField, boolean oldPropertyValue,
        boolean newPropertyValue) {
    if (!newPropertyValue) {
        try {
            int val;
            val = Integer.parseInt(this.txtDecAutorise.getText().trim());
            // Vérifier que le découvert autorisé ne soit pas supérieur au solde
            if (val <= this.compteEdite.solde || -val <= this.compteEdite.solde) {
                this.compteEdite.debitAutorise = val; // Mise à jour directe du débit autorisé
            } else {
                // Afficher une alerte d'information
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information de découvert");
                alert.setHeaderText(null);
                alert.setContentText("Le découvert autorisé ne peut pas être supérieur au solde.");
                alert.showAndWait();

                // Réinitialiser le champ de texte à la valeur actuelle du découvert autorisé
                this.txtDecAutorise.setText(String.valueOf(this.compteEdite.debitAutorise));
            }
        } catch (NumberFormatException nfe) {
            // Ne rien faire en cas d'exception
        }
    }
    return null;
}

private Object focusSolde(ObservableValue<? extends Boolean> txtField, boolean oldPropertyValue,
        boolean newPropertyValue) {
    if (!newPropertyValue) {
        try {
            double val;
            val = Double.parseDouble(this.txtSolde.getText().trim());
            // Vérifier que le solde ne soit pas inférieur au découvert autorisé
            if (val >= this.compteEdite.debitAutorise) {
                this.compteEdite.solde = val; // Mise à jour directe du solde
            } else {
                // Afficher une alerte d'information
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information de solde");
                alert.setHeaderText(null);
                alert.setContentText("Le solde ne peut pas être inférieur au découvert autorisé.");
                alert.showAndWait();

                // Réinitialiser le champ de texte à la valeur actuelle du solde
                this.txtSolde.setText(String.valueOf(this.compteEdite.solde));
            }
        } catch (NumberFormatException nfe) {
            // Ne rien faire en cas d'exception
        }
    }
    return null;
}

	// Attributs de la scene + actions
	@FXML
	private Label lblMessage;
	@FXML
	private Label lblSolde;
	@FXML
	private TextField txtIdclient;
	@FXML
	private TextField txtIdAgence;
	@FXML
	private TextField txtIdNumCompte;
	@FXML
	private TextField txtDecAutorise;
	@FXML
	private TextField txtSolde;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnCancel;

	@FXML
	private void doCancel() {
		this.compteResultat = null;
		this.containingStage.close();
	}

	@FXML
	private void doAjouter() {
		switch (this.editionMode) {
		case CREATION:
			if (this.isSaisieValide()) {
				this.compteResultat = this.compteEdite;
				this.containingStage.close();
			}
			break;
		case MODIFICATION:
			if (this.isSaisieValide()) {
				this.compteResultat = this.compteEdite;
				this.containingStage.close();
			}
			break;
		case SUPPRESSION:
			this.compteResultat = this.compteEdite;
			this.containingStage.close();
			break;
		}

	}

	private boolean isSaisieValide() {

		return true;
	}
}
