package application.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import model.data.Amortissement;

import java.util.List;

/**
 * Contrôleur de la vue du tableau d'amortissement.
 * Gère l'affichage des amortissements dans une table.
 * 
 * @author Théo
 */
public class TableauAmortissementViewController {

    @FXML
    private TableView<Amortissement> tableAmortissement;
    @FXML
    private TableColumn<Amortissement, Integer> colMois;
    @FXML
    private TableColumn<Amortissement, Double> colCapitalRestant;
    @FXML
    private TableColumn<Amortissement, Double> colInterets;
    @FXML
    private TableColumn<Amortissement, Double> colCapitalRembourse;
    @FXML
    private TableColumn<Amortissement, Double> colMensualite;
    @FXML
    private TableColumn<Amortissement, Double> colAssurance;

    private ObservableList<Amortissement> amortissements = FXCollections.observableArrayList();

    /**
     * Initialisation de la table d'amortissement.
     * Configure les colonnes et lie les données.
     */
    @FXML
    private void initialize() {
        colMois.setCellValueFactory(new PropertyValueFactory<>("mois"));
        colCapitalRestant.setCellValueFactory(new PropertyValueFactory<>("capitalRestant"));
        colInterets.setCellValueFactory(new PropertyValueFactory<>("interet"));
        colCapitalRembourse.setCellValueFactory(new PropertyValueFactory<>("capitalRembourse"));
        colMensualite.setCellValueFactory(new PropertyValueFactory<>("mensualite"));
        colAssurance.setCellValueFactory(new PropertyValueFactory<>("assurance"));

        tableAmortissement.setItems(amortissements);
    }

    /**
     * Met à jour la liste des amortissements affichés dans la table.
     *
     * @param amortissementsList la liste des amortissements à afficher
     */
    public void setAmortissements(List<Amortissement> amortissementsList) {
        amortissements.clear();
        amortissements.addAll(amortissementsList);
    }
}
