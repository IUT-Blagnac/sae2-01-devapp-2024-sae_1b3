package application.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.data.Employe;
import model.orm.Access_BD_Employe;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
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
        System.out.println("Initializing EmployeController");
        
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
            System.out.println("Loaded employees: " + employes.size()); // Ligne de débogage
            employeData.setAll(employes);
        } catch (DataAccessException | DatabaseConnexionException e) {
            e.printStackTrace();
        }
    }
}
