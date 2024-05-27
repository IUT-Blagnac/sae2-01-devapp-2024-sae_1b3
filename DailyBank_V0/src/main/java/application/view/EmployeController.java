package application.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
}
