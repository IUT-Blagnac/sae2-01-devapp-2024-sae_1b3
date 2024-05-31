package application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import application.DailyBankState;
import application.control.ComptesManagement;
import application.tools.AlertUtilities;
import application.tools.CategorieOperation;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;
import model.orm.Access_BD_Client;
import model.orm.Access_BD_CompteCourant;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

public class OperationEditorPaneViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private CategorieOperation categorieOperation;
	private CompteCourant compteEdite;
	private Operation operationResultat;

	/**
     * Initialise le contexte de la fenêtre.
     * @param _containingStage La fenêtre contenant la scène.
     * @param _dbstate L'état courant de l'application.
     */
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	/**
     * Affiche la boîte de dialogue d'édition des opérations.
     * @param cpte Le compte concerné par l'opération.
     * @param mode Le mode d'opération (débit, crédit, virement).
     * @return L'opération effectuée.
     */
	public Operation displayDialog(CompteCourant cpte, CategorieOperation mode) {
		this.categorieOperation = mode;
		this.compteEdite = cpte;

		switch (mode) {
		case DEBIT:

			String info = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info);

			this.btnOk.setText("Effectuer Débit");
			this.btnCancel.setText("Annuler débit");

			ObservableList<String> listTypesOpesPossibles = FXCollections.observableArrayList();
			listTypesOpesPossibles.addAll(ConstantesIHM.OPERATIONS_DEBIT_GUICHET);

			this.cbTypeOpe.setItems(listTypesOpesPossibles);
			this.cbTypeOpe.getSelectionModel().select(0);
			break;
			case CREDIT:
			String infoCredit = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(infoCredit);

			this.btnOk.setText("Effectuer Crédit");
			this.btnCancel.setText("Annuler crédit");

			ObservableList<String> listTypesOpesPossiblesCredit = FXCollections.observableArrayList();
			listTypesOpesPossiblesCredit.addAll(ConstantesIHM.OPERATIONS_CREDIT_GUICHET);

			this.cbTypeOpe.setItems(listTypesOpesPossiblesCredit);
			this.cbTypeOpe.getSelectionModel().select(0);
			break;
			case VIREMENT:
			this.containingStage.setTitle("Enregistrer un virement");
			info = "Compte n°" + this.compteEdite.idNumCompte + "  Solde : " + this.compteEdite.solde
					+ "  Découvert Autorisé : " + Integer.toString(this.compteEdite.debitAutorise);
			this.lblMessage.setText(info);
			// this.lblNomOp.setText("Comptes destinataires\npossible (ouvert)");
			this.btnOk.setText("Effectuer virement");
			this.btnCancel.setText("Annuler");

			Access_BD_CompteCourant ac = new Access_BD_CompteCourant();
			ObservableList<String> listTypesComptesPossibles = FXCollections.observableArrayList();
			ArrayList<CompteCourant> listCompte = new ArrayList<CompteCourant>();

			try {
				listCompte = ac.getCompteCourants(this.compteEdite.idNumCli, true, this.compteEdite.idNumCompte);
			} catch (DataAccessException e) {
			} catch (DatabaseConnexionException e) {
			}
			for (int i = 0; i < listCompte.size(); i++) {
				if (listCompte.get(i) != null) {
					if (listCompte.get(i).estCloture.equals("N")) {
						listTypesComptesPossibles.add("Numéro du Compte = " + listCompte.get(i).idNumCompte
								+ "   Solde : " + listCompte.get(i).solde);
					}
				}
			}
			this.cbTypeOpe.setItems(listTypesComptesPossibles);
			this.cbTypeOpe.getSelectionModel().select(0);
			break;
			case VIREMENT2:

            ObservableList<String> listTypesOpesPossiblesVIREMENT = FXCollections.observableArrayList();
            listTypesOpesPossiblesVIREMENT.addAll(ConstantesIHM.OPERATIONS_VIREMENT_GUICHET);

            this.cbTypeOpe.setItems(listTypesOpesPossiblesVIREMENT);
            this.cbTypeOpe.getSelectionModel().select(0);
            break;
	}

		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}

		this.operationResultat = null;
		this.cbTypeOpe.requestFocus();

		this.containingStage.showAndWait();
		return this.operationResultat;
	}

	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblMessage;
	@FXML
	private Label lblMontant;
	@FXML
	private ComboBox<String> cbTypeOpe;
	@FXML
	private TextField txtMontant;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnCancel;

	@FXML
	private void doCancel() {
		this.operationResultat = null;
		this.containingStage.close();
	};

	@FXML
private void doAjouter() {
    switch (this.categorieOperation) {
        case DEBIT:
            double montant;

            this.txtMontant.getStyleClass().remove("borderred");
            this.lblMontant.getStyleClass().remove("borderred");
            this.lblMessage.getStyleClass().remove("borderred");
            String info = "Cpt. : " + this.compteEdite.idNumCompte + "  "
                    + String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
                    + String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
            this.lblMessage.setText(info);

            try {
                montant = Double.parseDouble(this.txtMontant.getText().trim());
                if (montant <= 0) throw new NumberFormatException();
            } catch (NumberFormatException nfe) {
                this.txtMontant.getStyleClass().add("borderred");
                this.lblMontant.getStyleClass().add("borderred");
                this.txtMontant.requestFocus();
                return;
            }
            if (this.compteEdite.solde - montant < this.compteEdite.debitAutorise) {
                info = "Dépassement du découvert ! - Cpt. : " + this.compteEdite.idNumCompte + "  "
                        + String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
                        + String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
                this.lblMessage.setText(info);
                this.txtMontant.getStyleClass().add("borderred");
                this.lblMontant.getStyleClass().add("borderred");
                this.lblMessage.getStyleClass().add("borderred");
                this.txtMontant.requestFocus();
                return;
            }
            String typeOp = this.cbTypeOpe.getValue();
            this.operationResultat = new Operation(-1, montant, null, null, this.compteEdite.idNumCli, typeOp);
            this.containingStage.close();
            break;

        case CREDIT:
            double montantCredit;

            this.txtMontant.getStyleClass().remove("borderred");
            this.lblMontant.getStyleClass().remove("borderred");

            try {
                montantCredit = Double.parseDouble(this.txtMontant.getText().trim());
                if (montantCredit <= 0) throw new NumberFormatException();
            } catch (NumberFormatException nfe) {
                this.txtMontant.getStyleClass().add("borderred");
                this.lblMontant.getStyleClass().add("borderred");
                this.txtMontant.requestFocus();
                return;
            }

            String typeOpCredit = this.cbTypeOpe.getValue();
            this.operationResultat = new Operation(-1, montantCredit, null, null, this.compteEdite.idNumCli, typeOpCredit);
            this.containingStage.close();
            break;

        case VIREMENT:
            double montant2;

            this.txtMontant.getStyleClass().remove("borderred");
            this.lblMontant.getStyleClass().remove("borderred");
            this.lblMessage.getStyleClass().remove("borderred");
            info = "Cpt. : " + this.compteEdite.idNumCompte + "  "
                    + String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
                    + String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
            this.lblMessage.setText(info);

            try {
                montant2 = Double.parseDouble(this.txtMontant.getText().trim());
                if (montant2 <= 0) throw new NumberFormatException();
            } catch (NumberFormatException nfe) {
                this.txtMontant.getStyleClass().add("borderred");
                this.lblMontant.getStyleClass().add("borderred");
                this.txtMontant.requestFocus();
                return;
            }
            if (montant2 > 999999) {
                AlertUtilities.showAlert(this.containingStage, "Opération impossible",
                        "Le montant doit être inférieur à 1 000 000 !", "", AlertType.INFORMATION);
                return;
            }
            String cptChoisi = this.cbTypeOpe.getValue();
            int idNumCpt = -1;
            for (int i = 0; i < cptChoisi.length(); i++) {
                char c = cptChoisi.charAt(i);
                if (Character.isDigit(c)) {
                    idNumCpt = Character.getNumericValue(c);
                    if (i + 1 < cptChoisi.length() && Character.isDigit(cptChoisi.charAt(i + 1))) {
                        idNumCpt = idNumCpt * 10 + Character.getNumericValue(cptChoisi.charAt(i + 1));
                    }
                    break;
                }
            }
            if (idNumCpt != -1) {
                this.operationResultat = new Operation(-1, montant2, null, null, idNumCpt, "Virement Compte à Compte");
                this.containingStage.close();
            }
            break;

        case VIREMENT2:
            double montantVirement;

            this.txtMontant.getStyleClass().remove("borderred");
            this.lblMontant.getStyleClass().remove("borderred");

            try {
                montantVirement = Double.parseDouble(this.txtMontant.getText().trim());
                if (montantVirement <= 0) throw new NumberFormatException();
            } catch (NumberFormatException nfe) {
                this.txtMontant.getStyleClass().add("borderred");
                this.lblMontant.getStyleClass().add("borderred");
                this.txtMontant.requestFocus();
                return;
            }

            String typeOpVirement = this.cbTypeOpe.getValue();
            this.operationResultat = new Operation(-1, montantVirement, null, null, this.compteEdite.idNumCli, typeOpVirement);
            this.containingStage.close();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/ClientvirementManagement.fxml"));
                VBox ClientListPane = loader.load();
                Scene scene = new Scene(ClientListPane);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Liste des Client");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                AlertUtilities.showAlert(this.containingStage, "Erreur", null, "Impossible de charger la vue des clients.", AlertType.ERROR);
            }
            break;
    }
}
}