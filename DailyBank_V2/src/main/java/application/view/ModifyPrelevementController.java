package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.data.PrelevementAutomatique;
import model.orm.Access_BD_PrelevementAutomatiques;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

/**
 * Classe contrôleur pour la modification des informations des prélèvements automatiques dans l'application.
 * @author Thomas CEOLIN
 */
public class ModifyPrelevementController {

    @FXML
    private TextField montantField;

    @FXML
    private TextField dateRecurrenteField;

    @FXML
    private TextField beneficiaireField;

    private Stage dialogStage;
    private PrelevementAutomatique prelevement;
    private boolean okClicked = false;

    /**
     * Définit la fenêtre de dialogue pour ce contrôleur.
     *
     * @param dialogStage La fenêtre de dialogue à définir.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Définit les données du prélèvement automatique à modifier.
     *
     * @param prelevement Les données du prélèvement automatique à modifier.
     */
    public void setPrelevement(PrelevementAutomatique prelevement) {
        this.prelevement = prelevement;
    
        montantField.setText(Double.toString(prelevement.getMontant()));
        dateRecurrenteField.setText(prelevement.getDateRecurrente());
        beneficiaireField.setText(prelevement.getBeneficiaire());
    }

    /**
     * Indique si le bouton OK a été cliqué.
     *
     * @return Vrai si le bouton OK a été cliqué, faux sinon.
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            prelevement.setMontant(Double.parseDouble(montantField.getText()));
            prelevement.setDateRecurrente(dateRecurrenteField.getText());
            prelevement.setBeneficiaire(beneficiaireField.getText());

            try {
                Access_BD_PrelevementAutomatiques accessBDPrelevement = new Access_BD_PrelevementAutomatiques();
                accessBDPrelevement.updatePrelevement(prelevement);
                okClicked = true;
                dialogStage.close();
            } catch (DataAccessException | DatabaseConnexionException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Une erreur s'est produite lors de la modification du prélèvement automatique.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
    String errorMessage = "";

    // Vérification du champ montant
    if (montantField.getText() == null || montantField.getText().isEmpty()) {
        errorMessage += "Montant invalide!\n";
    } else {
        try {
            double montant = Double.parseDouble(montantField.getText());
            if (montant <= 0) {
                errorMessage += "Le montant doit être supérieur à zéro!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Le montant doit être un nombre!\n";
        }
    }

    // Vérification du champ date récurrente
    if (dateRecurrenteField.getText() == null || dateRecurrenteField.getText().isEmpty()) {
        errorMessage += "Date récurrente invalide!\n";
    } else {
        try {
            double dateRecurrente = Double.parseDouble(dateRecurrenteField.getText());
            if (dateRecurrente <= 0) {
                errorMessage += "La date récurrente doit être supérieure à zéro!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "La date récurrente doit être un nombre!\n";
        }
    }

    // Vérification du champ bénéficiaire
    if (beneficiaireField.getText() == null || beneficiaireField.getText().isEmpty()) {
        errorMessage += "Bénéficiaire invalide!\n";
    }

    // Vérification finale
    if (errorMessage.isEmpty()) {
        return true;
    } else {
        showAlert(Alert.AlertType.ERROR, "Champs invalides", errorMessage);
        return false;
    }
}


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
