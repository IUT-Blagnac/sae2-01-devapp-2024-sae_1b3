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
import application.control.OperationsManagement;

import java.util.List;

/**
 * La classe ClientController contrôle le comportement de la vue client.
 * Elle gère l'affichage des données clients dans un tableau, le chargement des clients depuis la base de données,
 * et la gestion des interactions utilisateur telles que les opérations de virement.
 */
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

    private ObservableList<Client> clientData = FXCollections.observableArrayList();

    private OperationsManagement omDialogController;

    /**
     * Initialise le ClientController.
     * Configure les colonnes du tableau et initialise le TableView avec les données clients.
     */
    @FXML
    private void initialize() {
        // Initialise les colonnes du tableau
        idNumCli.setCellValueFactory(new PropertyValueFactory<>("idNumCli"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        adressePostale.setCellValueFactory(new PropertyValueFactory<>("adressePostale"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        estInactif.setCellValueFactory(new PropertyValueFactory<>("estInactif"));

        // Définit les données clients dans le tableau
        ClientTable.setItems(clientData);
        System.out.println("TableView initialisée avec succès.");
    }
    /**
     * Définit les données clients à afficher dans le tableau.
     * @param clientData La liste des clients à afficher.
     */
    public void setClientData(ObservableList<Client> clientData) {
        this.clientData = clientData;
        ClientTable.setItems(clientData);
        System.out.println("Les données de la TableView ont été mises à jour.");
    }
    /**
     * Charge les clients depuis la base de données et met à jour le tableau avec les données récupérées.
     */
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
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des clients depuis la base de données.");
        }
    }

    /**
     * Effectue une opération de virement.
     */
    @FXML
    private void doVirement() {
        // À implémenter
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param alertType Le type d'alerte.
     * @param title Le titre de l'alerte.
     * @param message Le contenu du message de l'alerte.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
