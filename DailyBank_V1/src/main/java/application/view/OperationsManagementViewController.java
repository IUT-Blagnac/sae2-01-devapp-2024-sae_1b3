package application.view;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import application.DailyBankState;
import application.control.OperationsManagement;
import application.tools.NoSelectionModel;
import application.tools.PairsOfValue;
import application.tools.ConstantesIHM;
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

    // Fenêtre physique où est la scène contenant le fichier FXML contrôlé par this
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
        this.containingStage.setOnCloseRequest(this::closeWindow);
        this.oListOperations = FXCollections.observableArrayList();
        this.lvOperations.setItems(this.oListOperations);
        this.lvOperations.setSelectionModel(new NoSelectionModel<>());
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
    private void closeWindow(WindowEvent e) {
        this.doCancel();
        e.consume();
    }

    // Attributs de la scène + actions

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
    @FXML
    private Button btnDebitExceptionnel;

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
        Operation op = this.omDialogController.enregistrerVirement();
        if (op != null) {
            this.updateInfoCompteClient();
            this.validateComponentState();
        }
    }

    /**
     * Enregistre une opération de débit exceptionnel.
     */
    @FXML
    private void doDebitExceptionnel() {
        Operation op = this.omDialogController.enregistrerDEBITExceptionnel();
        if (op != null) {
            this.updateInfoCompteClient();
            this.validateComponentState();
        }
    }

    private void validateComponentState() {
        // Non implémenté => désactivé
        this.btnCredit.setDisable(false);
        this.btnDebit.setDisable(false);

        // Définir la visibilité du bouton btnDebitExceptionnel en fonction des droits d'accès
        this.btnDebitExceptionnel.setVisible(ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel()));
    }

    private void updateInfoCompteClient() {
        PairsOfValue<CompteCourant, ArrayList<Operation>> opesEtCompte;
        opesEtCompte = this.omDialogController.operationsEtSoldeDunCompte();

        ArrayList<Operation> listeOP;
        this.compteConcerne = opesEtCompte.getLeft();
        listeOP = opesEtCompte.getRight();

        String info;
        info = this.clientDuCompte.nom + "  " + this.clientDuCompte.prenom + "  (id : " + this.clientDuCompte.idNumCli + ")";
        this.lblInfosClient.setText(info);

        info = "Cpt. : " + this.compteConcerne.idNumCompte + "  "
                + String.format(Locale.ENGLISH, "%12.02f", this.compteConcerne.solde) + "  /  "
                + String.format(Locale.ENGLISH, "%8d", this.compteConcerne.debitAutorise);
        this.lblInfosCompte.setText(info);

        this.oListOperations.clear();
        this.oListOperations.addAll(listeOP);

        this.validateComponentState();
    }

    @FXML
    public void generatePDF() {
        try {
            // Récupérez les informations nécessaires pour générer le PDF
            String clientName = this.clientDuCompte.nom;
            String clientSurname = this.clientDuCompte.prenom;
            String accountNumber = String.valueOf(this.compteConcerne.idNumCompte);
            List<Operation> operations = this.oListOperations;

            // Créez le document PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("releve_compte_" + accountNumber + ".pdf"));

            document.open();

            // Ajoutez le contenu au document
            document.add(new Paragraph("Relevé de compte"));
            document.add(new Paragraph("Client : " + clientName + " " + clientSurname));
            document.add(new Paragraph("Numéro de compte : " + accountNumber));
            document.add(new Paragraph(" ")); // ligne vide pour l'espacement

            for (Operation op : operations) {
                String dateValeurStr = op.dateValeur != null ? op.dateValeur.toString() : "Date non définie";
                document.add(new Paragraph("Opération : " + op.idTypeOp + " | Montant : " + op.montant + " | Date : " + dateValeurStr));
            }

            document.close();

            // Affichez un message de succès ou effectuez toute autre action nécessaire
            System.out.println("Le relevé de compte a été généré avec succès.");

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            // Affichez un message d'erreur ou effectuez toute autre action nécessaire
            System.out.println("Une erreur est survenue lors de la génération du relevé de compte.");
        }
    }
}
