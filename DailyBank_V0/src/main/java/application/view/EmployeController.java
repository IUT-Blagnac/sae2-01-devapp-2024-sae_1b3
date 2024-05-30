package application.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Employe;
import model.orm.Access_BD_Employe;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import java.io.IOException;
import java.util.List;

public class EmployeController {

    @FXML
    private TableView<Employe> employeTable;

    @FXML
    private TableColumn<Employe, Integer> idColumn;

    @FXML
    private TableColumn<Employe, String> nomColumn;

    @FXML
    private TableColumn<Employe, String> prenomColumn;

    @FXML
    private TableColumn<Employe, String> droitsAccessColumn;

    @FXML
    private TableColumn<Employe, String> loginColumn;

    private ObservableList<Employe> employeData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialiser la table avec les colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idEmploye"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        droitsAccessColumn.setCellValueFactory(new PropertyValueFactory<>("droitsAccess"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));

        // Mettre la liste d'employé sur la table pour l'affichage
        employeTable.setItems(employeData);
    }

    public void setEmployeData(ObservableList<Employe> employeData) {
        this.employeData = employeData;
        employeTable.setItems(employeData);
    }

    @FXML
    private void loadEmployes() {
        Access_BD_Employe accessBDEmploye = new Access_BD_Employe();
        try {
            List<Employe> employes = accessBDEmploye.getAllEmployes();
            setEmployeData(FXCollections.observableArrayList(employes));
        } catch (DataAccessException | DatabaseConnexionException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showAddEmployeForm() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/application/view/AddEmploye.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un Employé");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            AddEmployeController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                loadEmployes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierEmploye() {
        // Récupérer l'employé sélectionné dans la table
        Employe selectedEmploye = employeTable.getSelectionModel().getSelectedItem();
    
        // Vérifier si un employé est sélectionné
        if (selectedEmploye != null) {
            try {
                // Charger le formulaire de modification de l'employé
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/application/view/ModifyEmploye.fxml"));
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Modifier un Employé");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(loader.load());
                dialogStage.setScene(scene);
    
                // Passer l'employé sélectionné au contrôleur de modification
                ModifyEmployeController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setEmploye(selectedEmploye);
    
                // Afficher le formulaire de modification et attendre sa fermeture
                dialogStage.showAndWait();
    
                // Recharger la liste des employés si la modification est confirmée
                if (controller.isOkClicked()) {
                    loadEmployes();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Aucun employé sélectionné, afficher un message d'erreur
            showAlert(Alert.AlertType.ERROR, "Aucune sélection", "Veuillez sélectionner un employé dans la table.");
        }
    }

    @FXML
    private Button btnSupprimerEmploye;

    @FXML
    private void supprimerEmploye() {
        // Récupérer l'employé sélectionné dans la table
        Employe selectedEmploye = employeTable.getSelectionModel().getSelectedItem();

        // Vérifier si un employé est sélectionné
        if (selectedEmploye != null) {
            try {
                // Supprimer l'employé de la base de données
                Access_BD_Employe accessBDEmploye = new Access_BD_Employe();
                accessBDEmploye.deleteEmploye(selectedEmploye);

                // Supprimer l'employé de la table affichée
                employeTable.getItems().remove(selectedEmploye);

                // Afficher une confirmation de suppression
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "L'employé a été supprimé avec succès.");
            } catch (DataAccessException | DatabaseConnexionException e) {
                // Gérer les exceptions
                showAlert(Alert.AlertType.ERROR, "Erreur de suppression", "Une erreur est survenue lors de la suppression de l'employé.");
            }
        } else {
            // Aucun employé sélectionné, afficher un message d'erreur
            showAlert(Alert.AlertType.ERROR, "Aucune sélection", "Veuillez sélectionner un employé dans la table.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
