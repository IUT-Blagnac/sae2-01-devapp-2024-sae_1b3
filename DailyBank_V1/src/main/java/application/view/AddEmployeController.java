package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.data.Employe;
import model.orm.Access_BD_Employe;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

/**
 * Classe contrôleur pour l'ajout d'un nouvel employé.
 * @author Théo Raban
 */
public class AddEmployeController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private RadioButton adminRadioButton;

    @FXML
    private RadioButton userRadioButton;

    @FXML
    private TextField loginField;

    @FXML
    private TextField motPasseField;

    @FXML
    private TextField idAgField;

    private Stage dialogStage;
    private boolean okClicked = false;

    /**
     * Définit la fenêtre pour le dialogue.
     *
     * @param dialogStage La fenêtre pour le dialogue
     *  @author Théo Raban
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Indique si le bouton "OK" a été cliqué.
     *
     * @return true si le bouton "OK" a été cliqué, sinon false
     * @author Théo Raban
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Gère l'action d'ajouter un nouvel employé.
     * @author Théo Raban
     */
    @FXML
    private void handleAddEmploye() {
        if (isInputValid()) {
            String login = loginField.getText(); // Utilisation de la variable login uniquement ici
    
            // Vérifier si le login est déjà utilisé
            try {
                Access_BD_Employe accessBDEmploye = new Access_BD_Employe();
                Employe existingEmploye = accessBDEmploye.getEmployeByLogin(login);
                
                if (existingEmploye != null) {
                    showAlert(Alert.AlertType.ERROR, "Erreur d'ajout", "Ce login est déjà utilisé par un autre employé.");
                    return; // Arrêter l'ajout de l'employé
                }
            } catch (DataAccessException | DatabaseConnexionException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Une erreur s'est produite lors de la vérification du login.");
                e.printStackTrace();
                return; // Arrêter l'ajout de l'employé en cas d'erreur de base de données
            }
    
            // Continuer l'ajout de l'employé si le login n'est pas déjà utilisé
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String droitsAccess;
            if (userRadioButton.isSelected()) {
                droitsAccess = "guichetier";
            } else if (adminRadioButton.isSelected()) {
                droitsAccess = "chefAgence";
            } else {
                droitsAccess = "guichetier";
            }
    
            String motPasse = motPasseField.getText();
            int idAg = Integer.parseInt(idAgField.getText());
    
            Employe newEmploye = new Employe(0, nom, prenom, droitsAccess, login, motPasse, idAg);
    
            try {
                Access_BD_Employe accessBDEmploye = new Access_BD_Employe();
                accessBDEmploye.addEmploye(newEmploye);
                okClicked = true;
                dialogStage.close();
            } catch (DataAccessException | DatabaseConnexionException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Une erreur s'est produite lors de l'ajout de l'employé.");
                e.printStackTrace();
            }
        }
    }

  


    

    /**
     * Gère l'action d'annuler le dialogue.
     *  @author Théo Raban
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Valide les champs d'entrée.
     *
     * @return true si l'entrée est valide, sinon false
     * @author Théo Raban
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (nomField.getText() == null || nomField.getText().isEmpty()) {
            errorMessage += "Nom invalide!\n";
        }
        if (prenomField.getText() == null || prenomField.getText().isEmpty()) {
            errorMessage += "Prenom invalide!\n";
        }
        if (loginField.getText() == null || loginField.getText().isEmpty()) {
            errorMessage += "Login invalide!\n";
        }
        if (motPasseField.getText() == null || motPasseField.getText().isEmpty()) {
            errorMessage += "Mot de passe invalide!\n";
        }
        if (idAgField.getText() == null || idAgField.getText().isEmpty()) {
            errorMessage += "Numéro d'agence invalide!\n";
        } else {
            try {
                Integer.parseInt(idAgField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Numéro d'agence doit être un entier!\n";
            }
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
     * @author Théo Raban
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
