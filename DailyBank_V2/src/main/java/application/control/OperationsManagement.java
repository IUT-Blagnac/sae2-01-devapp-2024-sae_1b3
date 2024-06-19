package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.CategorieOperation;
import application.tools.PairsOfValue;
import application.tools.StageManagement;
import application.view.OperationsManagementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;
import model.orm.Access_BD_CompteCourant;
import model.orm.Access_BD_Operation;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;


/**
 * La classe OperationsManagement contrôle la gestion des opérations.
 * Elle gère l'affichage des données et les interactions utilisateur pour les opérations de débit, crédit et virement.
 */
public class OperationsManagement {

	private Stage omStage;
	private DailyBankState dailyBankState;
	private OperationsManagementViewController omViewController;
	private Client clientDuCompte;
	private CompteCourant compteConcerne;

	/**
     * Initialise un gestionnaire d'opérations.
     * @param _parentStage La fenêtre parente.
     * @param _dbstate L'état courant de l'application.
     * @param client Le client concerné par les opérations.
     * @param compte Le compte concerné par les opérations.
     */
	public OperationsManagement(Stage _parentStage, DailyBankState _dbstate, Client client, CompteCourant compte) {

		this.clientDuCompte = client;
		this.compteConcerne = compte;
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(
					OperationsManagementViewController.class.getResource("operationsmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, 900 + 20, 350 + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.omStage = new Stage();
			this.omStage.initModality(Modality.WINDOW_MODAL);
			this.omStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.omStage);
			this.omStage.setScene(scene);
			this.omStage.setTitle("Gestion des opérations");
			this.omStage.setResizable(false);

			this.omViewController = loader.getController();
			this.omViewController.initContext(this.omStage, this, _dbstate, client, this.compteConcerne);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * Affiche la fenêtre de gestion des opérations.
     */
	public void doOperationsManagementDialog() {
		this.omViewController.displayDialog();
	}

	/**
     * Enregistre une opération de débit.
     * @return L'opération enregistrée.
	 * @author Thomas CEOLIN
     */
	public Operation enregistrerDebit() {

		OperationEditorPane oep = new OperationEditorPane(this.omStage, this.dailyBankState);
		Operation op = oep.doOperationEditorDialog(this.compteConcerne, CategorieOperation.DEBIT);
		if (op != null) {
			try {
				Access_BD_Operation ao = new Access_BD_Operation();

				ao.insertDebit(this.compteConcerne.idNumCompte, op.montant, op.idTypeOp);

			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.omStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.omStage.close();
				op = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.omStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				op = null;
			}
		}
		return op;
	}

	/**
     * Enregistre une opération de débit.
     * @return L'opération enregistrée.
	 * @author Thomas CEOLIN
     */
	public Operation enregistrerDEBITExceptionnel() {

		OperationEditorPane oep = new OperationEditorPane(this.omStage, this.dailyBankState);
		Operation op = oep.doOperationEditorDialog(this.compteConcerne, CategorieOperation.DEBITExceptionnel);
		if (op != null) {
			try {
				Access_BD_Operation ao = new Access_BD_Operation();

				ao.insertDebitExceptionnel(this.compteConcerne.idNumCompte, op.montant, op.idTypeOp);

			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.omStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.omStage.close();
				op = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.omStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				op = null;
			}
		}
		return op;
	}

	

	/**
     * Enregistre une opération de virement.
     * @return L'opération enregistrée.
	 * @author Thomas CEOLIN
     */
	public Operation enregistrerVirement() {

		OperationEditorPane oep = new OperationEditorPane(this.omStage, this.dailyBankState);
		Operation op = oep.doOperationEditorDialog(this.compteConcerne, CategorieOperation.VIREMENT);
		if (op != null) {
			try {
				Access_BD_Operation ao = new Access_BD_Operation();

				ao.insertVirement(this.compteConcerne.idNumCompte, op.idNumCompte, op.montant); // j'utilise la méthode insertVirement qui est dans mon acces bd 

			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.omStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.omStage.close();
				op = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.omStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				op = null;
			}
		}
		return op;
	}

	 /**
     * Enregistre une opération de crédit.
     * @return L'opération enregistrée.
	 * @author Thomas CEOLIN
     */
	public Operation enregistrerCredit() {
		OperationEditorPane oep = new OperationEditorPane(this.omStage, this.dailyBankState);
		Operation op = oep.doOperationEditorDialog(this.compteConcerne, CategorieOperation.CREDIT);
		if (op != null) {
			try {
				Access_BD_Operation aop = new Access_BD_Operation();
				aop.insertCredit(this.compteConcerne.idNumCompte, op.montant, op.idTypeOp);
			} catch (DatabaseConnexionException e) {
				e.printStackTrace();
				ExceptionDialog ed = new ExceptionDialog(this.omStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.omStage.close();
				op = null;
			} catch (ApplicationException ae) {
				ae.printStackTrace();
				ExceptionDialog ed = new ExceptionDialog(this.omStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				op = null;
			}
		}
		return op;
	}

	 /**
     * Récupère les opérations et le solde d'un compte.
     * @return Une paire contenant le compte et sa liste d'opérations.
	 * @author Thomas CEOLIN
     */
	public PairsOfValue<CompteCourant, ArrayList<Operation>> operationsEtSoldeDunCompte() {
		ArrayList<Operation> listeOP = new ArrayList<>();

		try {
			// Relecture BD du solde du compte
			Access_BD_CompteCourant acc = new Access_BD_CompteCourant();
			this.compteConcerne = acc.getCompteCourant(this.compteConcerne.idNumCompte);

			// lecture BD de la liste des opérations du compte de l'utilisateur
			Access_BD_Operation ao = new Access_BD_Operation();
			listeOP = ao.getOperations(this.compteConcerne.idNumCompte);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.omStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.omStage.close();
			listeOP = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.omStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listeOP = new ArrayList<>();
		}
		System.out.println(this.compteConcerne.solde);
		return new PairsOfValue<>(this.compteConcerne, listeOP);
	}
}
