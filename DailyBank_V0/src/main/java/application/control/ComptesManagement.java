package application.control;

import java.util.ArrayList;

import com.itextpdf.text.List;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ComptesManagementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;
import model.orm.Access_BD_CompteCourant;
import model.orm.Access_BD_Operation;
import model.orm.exception.ApplicationException;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

/**
 * La classe ComptesManagement représente un contrôleur pour gérer la fenêtre de gestion des comptes.
 * Elle permet d'effectuer des opérations telles que la création, la modification et la suppression de comptes.
 */
public class ComptesManagement {

	private Stage cmStage;
	private ComptesManagementViewController cmViewController;
	private DailyBankState dailyBankState;
	private Client clientDesComptes;

	/**
     * Construit une nouvelle instance de ComptesManagement.
     *
     * @param _parentStage Le stage parent pour la fenêtre de gestion des comptes.
     * @param _dbstate     L'instance de DailyBankState pour gérer l'état de l'application.
     * @param client       Le client dont les comptes sont gérés.
     */
	public ComptesManagement(Stage _parentStage, DailyBankState _dbstate, Client client) {

		this.clientDesComptes = client;
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(ComptesManagementViewController.class.getResource("comptesmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.cmStage = new Stage();
			this.cmStage.initModality(Modality.WINDOW_MODAL);
			this.cmStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.cmStage);
			this.cmStage.setScene(scene);
			this.cmStage.setTitle("Gestion des comptes");
			this.cmStage.setResizable(false);

			this.cmViewController = loader.getController();
			this.cmViewController.initContext(this.cmStage, this, _dbstate, client);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * Affiche la fenêtre de gestion des comptes.
     */
	public void doComptesManagementDialog() {
		this.cmViewController.displayDialog();
	}

	 /**
     * Gère les opérations d'un compte.
     *
     * @param cpt Le compte pour lequel les opérations sont gérées.
     */
	public void gererOperationsDUnCompte(CompteCourant cpt) {
		OperationsManagement om = new OperationsManagement(this.cmStage, this.dailyBankState,
				this.clientDesComptes, cpt);
		om.doOperationsManagementDialog();
	}

	/**
     * Crée un nouveau compte.
     *
     * @return Le compte créé.
     */
	public CompteCourant creerNouveauCompte() {
		CompteCourant compte;
		CompteEditorPane cep = new CompteEditorPane(this.cmStage, this.dailyBankState);

		// Récupérer la liste de tous les comptes existants
		ArrayList<CompteCourant> tousLesComptes = new ArrayList<>();
		try {
			Access_BD_CompteCourant acc = new Access_BD_CompteCourant();
			tousLesComptes = acc.getTousLesComptes();
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.cmStage.close();
			return null;
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			return null;
		}

		// Trouver le numéro de compte le plus élevé parmi tous les comptes
		int dernierNumeroCompte = 0;
		for (CompteCourant c : tousLesComptes) {
			if (c.idNumCompte > dernierNumeroCompte) {
				dernierNumeroCompte = c.idNumCompte;
			}
		}

		// Incrémenter pour obtenir le nouveau numéro de compte
		int nouveauNumeroCompte = dernierNumeroCompte + 1;
		System.out.println("Nouveau numéro de compte : " + nouveauNumeroCompte);

		// Créer le nouveau compte avec le numéro de compte correct
		compte = cep.doCompteEditorDialog(this.clientDesComptes, null, EditionMode.CREATION);
		if (compte != null) {
			compte.idNumCompte = nouveauNumeroCompte;
			System.out.println("Compte à insérer : " + compte.idNumCompte);
			try {
				Access_BD_CompteCourant acc = new Access_BD_CompteCourant();
				acc.insertCompte(compte); // Enregistre le compte dans la base de données
				AlertUtilities.showAlert(this.cmStage, "Création réussie", "Compte créé",
						"Le nouveau compte a été créé avec succès", AlertType.INFORMATION);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.cmStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
			}
		}
		return compte;
	}
	
	
	

	/**
     * Récupère la liste des comptes d'un client.
     *
     * @return La liste des comptes d'un client.
	 * @author Thomas CEOLIN
     */
	public ArrayList<CompteCourant> getComptesDunClient() {
		ArrayList<CompteCourant> listeCpt = new ArrayList<>();

		try {
			Access_BD_CompteCourant acc = new Access_BD_CompteCourant();
			listeCpt = acc.getCompteCourants(this.clientDesComptes.idNumCli, false, -1);
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.cmStage.close();
			listeCpt = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listeCpt = new ArrayList<>();
		}
		return listeCpt;
	}

	public CompteCourant modifierCompteCourant(CompteCourant compte) throws RowNotFoundOrTooManyRowsException, ManagementRuleViolation {
		CompteEditorPane cep = new CompteEditorPane(this.cmStage, this.dailyBankState);
		CompteCourant result = cep.doCompteEditorDialog(this.clientDesComptes, compte, EditionMode.MODIFICATION);
	
		if (result != null) {
			try {
				Access_BD_CompteCourant acc = new Access_BD_CompteCourant();
				acc.updateCompteCourant(result);
				AlertUtilities.showAlert(this.cmStage, "Modification réussie", "Compte modifié",
						"Le compte a été modifié avec succès", AlertType.INFORMATION);
			} catch (DatabaseConnexionException | DataAccessException e) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				result = null;
			}
		}
		return result;
	}
	
	
	


	public void supprimerCompteCourant(CompteCourant compte) {
	if (compte != null) {
		try {
			Access_BD_CompteCourant acc = new Access_BD_CompteCourant();
			acc.deleteCompteCourant(compte); // Supprime le compte de la base de données
			AlertUtilities.showAlert(this.cmStage, "Suppression réussie", "Compte supprimé",
					"Le compte a été supprimé avec succès", AlertType.INFORMATION);
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
			ed.doExceptionDialog();
		} catch (DataAccessException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
		}
	}
}



	
}
