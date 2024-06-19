package application.view;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import application.DailyBankState;
import application.control.OperationsManagement;
import application.tools.ConstantesIHM;
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

    // Fenêtre physique où est la scène contenant le fichier FXML contrôlé par this
    private Stage containingStage;

    // Données de la fenêtre
    private Client clientDuCompte;
    private CompteCourant compteConcerne;
    private ObservableList<Operation> oListOperations;

    /**
     * Initialise le contexte de la fenêtre.
     * 
     * @param _containingStage La fenêtre contenant la scène.
     * @param _om              Le contrôleur d'opérations.
     * @param _dbstate         L'état courant de l'application.
     * @param client           Le client associé au compte.
     * @param compte           Le compte concerné par les opérations.
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

    /**
     * Configure la fenêtre et initialise les composants.
     */
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

    /**
     * Gère la fermeture de la fenêtre.
     * 
     * @param e L'événement de fermeture de la fenêtre.
     */
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
     * @author Thomas
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
     * @author Thomas
     */
    @FXML
    private void doDebitExceptionnel() {
        Operation op = this.omDialogController.enregistrerDEBITExceptionnel();
        if (op != null) {
            this.updateInfoCompteClient();
            this.validateComponentState();
        }
    }

    /**
     * Valide l'état des composants de l'interface.
     */
    private void validateComponentState() {
        this.btnCredit.setDisable(false);
        this.btnDebit.setDisable(false);
        this.btnDebitExceptionnel.setVisible(ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel()));
    }

    /**
     * Met à jour les informations du compte client affichées dans l'interface.
     */
    private void updateInfoCompteClient() {
        PairsOfValue<CompteCourant, ArrayList<Operation>> opesEtCompte = this.omDialogController.operationsEtSoldeDunCompte();
        ArrayList<Operation> listeOP = opesEtCompte.getRight();
        this.compteConcerne = opesEtCompte.getLeft();

        String infoClient = String.format("%s %s (id : %d)", this.clientDuCompte.nom, this.clientDuCompte.prenom, this.clientDuCompte.idNumCli);
        this.lblInfosClient.setText(infoClient);

        String infoCompte = String.format(Locale.ENGLISH, "Cpt. : %d  %.2f € / %d", this.compteConcerne.idNumCompte, this.compteConcerne.solde, this.compteConcerne.debitAutorise);
        this.lblInfosCompte.setText(infoCompte);

        this.oListOperations.clear();
        this.oListOperations.addAll(listeOP);

        this.validateComponentState();
    }

    /**
     * Génère un relevé de compte PDF pour le compte client.
     * @author Théo
     */
    @FXML
    public void generatePDF() {
        try {
            // Récupérez les informations nécessaires pour générer le PDF
            String clientName = this.clientDuCompte.nom;
            String clientSurname = this.clientDuCompte.prenom;
            String accountNumber = String.valueOf(this.compteConcerne.idNumCompte);
            double accountBalance = this.compteConcerne.solde;
            List<Operation> operations = this.oListOperations;

            // Créez le document PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("releve_compte_" + accountNumber + ".pdf"));

            document.open();

            // Ajoutez le contenu au document avec des titres plus gros et en gras
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            document.add(new Paragraph("Relevé de compte", titleFont));
            document.add(new Paragraph("Client : " + clientName + " " + clientSurname, subTitleFont));
            document.add(new Paragraph("Numéro de compte : " + accountNumber, subTitleFont));
            document.add(new Paragraph("Solde du compte : " + String.format(Locale.ENGLISH, "%.2f", accountBalance) + " €", subTitleFont));
            document.add(new Paragraph(" ")); // ligne vide pour l'espacement

            // Création du tableau pour les opérations
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Définition des en-têtes de colonne
            PdfPCell cell1 = new PdfPCell(new Phrase("Type d'Opération", subTitleFont));
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase("Montant", subTitleFont));
            cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Phrase("Date", subTitleFont));
            cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell3);

            // Ajout des opérations dans le tableau
            for (Operation op : operations) {
                String dateValeurStr = op.dateValeur != null ? op.dateValeur.toString() : "Date non définie";
                table.addCell(new PdfPCell(new Phrase(op.idTypeOp, normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.format(Locale.ENGLISH, "%.2f", op.montant) + " €", normalFont)));
                table.addCell(new PdfPCell(new Phrase(dateValeurStr, normalFont)));
            }

            document.add(table);

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
