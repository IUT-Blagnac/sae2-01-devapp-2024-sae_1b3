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
 * Classe contrôleur pour la modification des informations des employés dans l'application.
 */
public class ModifyEmployeController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private RadioButton userRadioButton;

    @FXML
    private RadioButton adminRadioButton;

    @FXML
    private TextField loginField;

    @FXML
    private TextField motPasseField;

    @FXML
    private TextField idAgField;

    private Stage dialogStage;
    private Employe employe;
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
     * Définit les données de l'employé à modifier.
     *
     * @param employe Les données de l'employé à modifier.
     */
    public void setEmploye(Employe employe) {
        this.employe = employe;
    
        nomField.setText(employe.getNom());
        prenomField.setText(employe.getPrenom());
        loginField.setText(employe.getLogin());
        motPasseField.setText(employe.getMotPasse());
        idAgField.setText(Integer.toString(employe.getIdAg()));
        String droitsAccess = employe.getDroitsAccess();
        if (droitsAccess.equals("guichetier")) {
            userRadioButton.setSelected(true);
        } else if (droitsAccess.equals("chefAgence")) {
            adminRadioButton.setSelected(true);
        }
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
            employe.setNom(nomField.getText());
            employe.setPrenom(prenomField.getText());
            employe.setLogin(loginField.getText());
            employe.setMotPasse(motPasseField.getText());
            employe.setIdAg(Integer.parseInt(idAgField.getText()));
            employe.setDroitsAccess(userRadioButton.isSelected() ? "guichetier" : "chefAgence");

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleUpdateEmploye() {
        if (isInputValid()) {
            String login = loginField.getText();

            try {
                Access_BD_Employe accessBDEmploye = new Access_BD_Employe();
                Employe existingEmploye = accessBDEmploye.getEmployeByLogin(login);

                if (existingEmploye == null || existingEmploye.getIdEmploye() == employe.getIdEmploye()) {
                    employe.setNom(nomField.getText());
                    employe.setPrenom(prenomField.getText());
                    employe.setLogin(loginField.getText());
                    employe.setMotPasse(motPasseField.getText());
                    employe.setIdAg(Integer.parseInt(idAgField.getText()));
                    employe.setDroitsAccess(userRadioButton.isSelected() ? "guichetier" : "chefAgence");

                    accessBDEmploye.updateEmploye(employe);
                    okClicked = true;
                    dialogStage.close();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de modification", "Ce login est déjà utilisé par un autre employé.");
                }
            } catch (DataAccessException | DatabaseConnexionException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Une erreur s'est produite lors de la modification de l'employé.");
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

        if (nomField.getText() == null || nomField.getText().isEmpty()) {
            errorMessage += "Nom invalide!\n";
        }
        if (prenomField.getText() == null || prenomField.getText().isEmpty()) {
            errorMessage += "Prénom invalide!\n";
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
