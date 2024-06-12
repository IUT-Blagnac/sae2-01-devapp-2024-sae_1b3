package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.data.PrelevementAutomatique;
import model.orm.Access_BD_PrelevementAutomatiques;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

/**
 * Classe contrôleur pour l'ajout d'un nouveau prélèvement automatique.
 */
public class AddPrelevementController {

    @FXML
    private Label lblMessage;
    @FXML
    private Label errorSupp;
    @FXML
    private TextField txtIdNumCompte;
    @FXML
    private TextField txtMontant;
    @FXML
    private TextField txtIdprelev;
    @FXML
    private TextField txtDatePrelev;
    @FXML
    private TextField txtBeneficiaire;
    @FXML
    private Label errorMontant;
    @FXML
    private Label errorDatePrelev;
    @FXML
    private Label errorBeneficiaire;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnOk;

    private Stage dialogStage;
    private boolean okClicked = false;

    /**
     * Définit la fenêtre pour le dialogue.
     *
     * @param dialogStage La fenêtre pour le dialogue
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Indique si le bouton "OK" a été cliqué.
     *
     * @return true si le bouton "OK" a été cliqué, sinon false
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Gère l'action d'ajouter un nouveau prélèvement automatique.
     */
    @FXML
    private void handleAddPrelevement() {
        if (isInputValid()) {
            try {
                int idNumCompte = Integer.parseInt(txtIdNumCompte.getText());
                double montant = Double.parseDouble(txtMontant.getText());
                String dateRecurrente = txtDatePrelev.getText();
                String beneficiaire = txtBeneficiaire.getText();

                // Créer un nouvel objet PrelevementAutomatique
                PrelevementAutomatique newPrelevement = new PrelevementAutomatique(0, idNumCompte, montant, dateRecurrente, beneficiaire);

                // Ajouter le prélèvement automatique à la base de données
                Access_BD_PrelevementAutomatiques accessBD = new Access_BD_PrelevementAutomatiques();
                accessBD.addPrelevement(newPrelevement);

                okClicked = true;
                dialogStage.close();
            } catch (DataAccessException | DatabaseConnexionException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Une erreur s'est produite lors de l'ajout du prélèvement automatique.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Gère l'action d'annuler le dialogue.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Valide les champs d'entrée.
     *
     * @return true si l'entrée est valide, sinon false
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (txtIdNumCompte.getText() == null || txtIdNumCompte.getText().isEmpty()) {
            errorMessage += "ID Compte invalide!\n";
        } else {
            try {
                Integer.parseInt(txtIdNumCompte.getText());
            } catch (NumberFormatException e) {
                errorMessage += "ID Compte doit être un entier!\n";
            }
        }
        if (txtMontant.getText() == null || txtMontant.getText().isEmpty()) {
            errorMessage += "Montant invalide!\n";
        } else {
            try {
                Double.parseDouble(txtMontant.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Montant doit être un nombre!\n";
            }
        }
        if (txtDatePrelev.getText() == null || txtDatePrelev.getText().isEmpty()) {
            errorMessage += "Date de prélèvement invalide!\n";
        }
        if (txtBeneficiaire.getText() == null || txtBeneficiaire.getText().isEmpty()) {
            errorMessage += "Bénéficiaire invalide!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Champs invalides", errorMessage);
            return false;
        }
    }

    /**
     * Affiche une alerte.
     *
     * @param alertType Type d'alerte
     * @param title     Titre de l'alerte
     * @param message   Message de l'alerte
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
