package application.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.data.Client;
import model.orm.Access_BD_Client;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import java.util.List;

public class ClientController {

    @FXML
    private TableView<Client> ClientTable;

    @FXML
    private TableColumn<Client, Integer> idNumCli;

    @FXML
    private TableColumn<Client, String> nom;

    @FXML
    private TableColumn<Client, String> prenom;

    @FXML
    private TableColumn<Client, String> adressePostale;

    @FXML
    private TableColumn<Client, String> email;

    @FXML
    private TableColumn<Client, String> telephone;

    @FXML
    private TableColumn<Client, String> estInactif;

    @FXML
    private TableColumn<Client, Integer> idAgCli;

    private ObservableList<Client> clientData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        System.out.println("Initialisation du contrôleur ClientController...");
        
        // Initialiser la table avec les colonnes
        idNumCli.setCellValueFactory(new PropertyValueFactory<>("idNumCli"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        adressePostale.setCellValueFactory(new PropertyValueFactory<>("adressePostale"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        estInactif.setCellValueFactory(new PropertyValueFactory<>("estInactif"));
        idAgCli.setCellValueFactory(new PropertyValueFactory<>("idAgCli"));
    
        // Vérifiez que la table est initialisée correctement
        ClientTable.setItems(clientData);
        System.out.println("TableView initialisée avec succès.");
    }


    public void setClientData(ObservableList<Client> clientData) {
        this.clientData = clientData;
        ClientTable.setItems(clientData);
        System.out.println("Les données de la TableView ont été mises à jour.");
    }
    
    @FXML
    private void loadClient() {
        System.out.println("Chargement des clients depuis la base de données...");
        Access_BD_Client accessBDClient = new Access_BD_Client();
        try {
            List<Client> clients = accessBDClient.getAllClient();
            System.out.println("Nombre de clients récupérés depuis la base de données : " + clients.size());
            for (Client client : clients) {
                System.out.println(client);
            }
            setClientData(FXCollections.observableArrayList(clients));
        } catch (DataAccessException | DatabaseConnexionException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des clients.");
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
