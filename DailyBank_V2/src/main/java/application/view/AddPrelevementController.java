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
    private TextField txtIdNumCompte;

    @FXML
    private TextField txtIdprelev;

    @FXML
    private TextField montantField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField beneficiaireField;

    @FXML
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
        try {
            // Récupérer idNumCompte depuis le champ de texte
            int idNumCompte = Integer.parseInt(txtIdNumCompte.getText());
            double montant = Double.parseDouble(montantField.getText());
            String date = dateField.getText();
            String beneficiaire = beneficiaireField.getText();

            // Créer une instance de PrelevementAutomatique
            PrelevementAutomatique prelevement = new PrelevementAutomatique(0, idNumCompte, montant, date, beneficiaire);

            // Accéder à la base de données et ajouter le prélèvement
            Access_BD_PrelevementAutomatiques access = new Access_BD_PrelevementAutomatiques();
            access.addPrelevement(prelevement);
            okClicked = true;
            dialogStage.close();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez vérifier que les champs sont correctement saisis.");
            e.printStackTrace();
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

    // Vérification de l'ID du compte
    if (txtIdNumCompte.getText() == null || txtIdNumCompte.getText().isEmpty()) {
        errorMessage += "ID du compte invalide!\n";
    } else {
        try {
            int idNumCompte = Integer.parseInt(txtIdNumCompte.getText());
            if (idNumCompte <= 0) {
                errorMessage += "L'ID du compte doit être supérieur à zéro!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "L'ID du compte doit être un nombre entier!\n";
        }
    }

    // Vérification du montant
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

    // Vérification de la date
    if (dateField.getText() == null || dateField.getText().isEmpty()) {
        errorMessage += "Date invalide!\n";
    } else {
        try {
            double date = Double.parseDouble(dateField.getText());
            if (date <= 0) {
                errorMessage += "La date doit être supérieure à zéro!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "La date doit être un nombre!\n";
        }
    }

    // Vérification du bénéficiaire
    if (beneficiaireField.getText() == null || beneficiaireField.getText().isEmpty()) {
        errorMessage += "Bénéficiaire invalide!\n";
    }

    // Retourne vrai si aucun message d'erreur n'a été accumulé, sinon affiche une alerte
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

      private int compteId;

    public void setCompteId(int compteId) {
        this.compteId = compteId;
        txtIdNumCompte.setText(String.valueOf(compteId));
    }
}
