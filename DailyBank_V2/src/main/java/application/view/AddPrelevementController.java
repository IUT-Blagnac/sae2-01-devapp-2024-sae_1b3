package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.data.PrelevementAutomatique;
import model.orm.Access_BD_PrelevementAutomatiques;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

public class AddPrelevementController {

    @FXML
    private TextField montantField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField beneficiaireField;

    private Stage dialogStage;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleAddPrelevement() {
        if (isInputValid()) {
            double montant = Double.parseDouble(montantField.getText());
            String date = dateField.getText();
            String beneficiaire = beneficiaireField.getText();

            PrelevementAutomatique prelevement = new PrelevementAutomatique(0, 0, montant, date, beneficiaire);

            try {
                Access_BD_PrelevementAutomatiques access = new Access_BD_PrelevementAutomatiques();
                access.addPrelevement(prelevement);
                okClicked = true;
                dialogStage.close();
            } catch (DataAccessException | DatabaseConnexionException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Une erreur s'est produite lors de l'ajout du prélèvement.");
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

        if (montantField.getText() == null || montantField.getText().isEmpty()) {
            errorMessage += "Montant invalide!\n";
        } else {
            try {
                Double.parseDouble(montantField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Le montant doit être un nombre!\n";
            }
        }

        if (dateField.getText() == null || dateField.getText().isEmpty()) {
            errorMessage += "Date invalide!\n";
        }

        if (beneficiaireField.getText() == null || beneficiaireField.getText().isEmpty()) {
            errorMessage += "Bénéficiaire invalide!\n";
        }

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
