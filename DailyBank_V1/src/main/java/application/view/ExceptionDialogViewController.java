package application.view;

import java.io.PrintWriter;
import java.io.StringWriter;

import application.DailyBankState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.orm.exception.ApplicationException;

/**
 * Contrôleur pour la boîte de dialogue des exceptions.
 */
public class ExceptionDialogViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private ApplicationException aException;

	 /**
     * Initialise le contexte du contrôleur.
     *
     * @param _containingStage La fenêtre contenant la scène.
     * @param _dbstate         L'état courant de l'application.
     * @param _ae              L'exception à afficher.
     */
	public void initContext(Stage _containingStage, DailyBankState _dbstate, ApplicationException _ae) {
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.aException = _ae;
		this.configure();
	}

	/**
     * Configure la boîte de dialogue en affichant les détails de l'exception.
     */
	private void configure() {
		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));
		this.lblTitre.setText(this.aException.getMessage());
		this.txtTable.setText(this.aException.getTableName().toString());
		this.txtOpe.setText(this.aException.getOrder().toString());
		this.txtException.setText(this.aException.getClass().getName());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		this.aException.printStackTrace(pw);
		this.txtDetails.setText(sw.toString());
	}

	/**
     * Affiche la boîte de dialogue des exceptions.
     */
	public void displayDialog() {
		this.containingStage.showAndWait();
	}


	/**
     * Gère la fermeture de la fenêtre.
     *
     * @param e L'événement de fermeture de la fenêtre.
     * @return null.
     */	private Object closeWindow(WindowEvent e) {
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblTitre;
	@FXML
	private TextField txtTable;
	@FXML
	private TextField txtOpe;
	@FXML
	private TextField txtException;
	@FXML
	private TextArea txtDetails;

	/**
     * Gère le clic sur le bouton "OK" pour fermer la fenêtre.
     */
	@FXML
	private void doOK() {
		this.containingStage.close();
	}
}
