package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.data.Amortissement;

/**
 * Contrôleur de la vue de simulation de crédit.
 * Gère les interactions de l'utilisateur avec les champs de texte et les boutons,
 * calcule les amortissements et affiche les résultats.
 * 
 * @author Théo
 */
public class SimulationCreditViewController {

    @FXML
    private TextField txtCapital;

    @FXML
    private TextField txtDuree;

    @FXML
    private TextField txtTaux;

    @FXML
    private TextField txtAssurance;

    @FXML
    private CheckBox chkAssurance;

    /**
     * Initialisation des composants, si nécessaire.
     */
    @FXML
    private void initialize() {
        // Initialisation des composants, si nécessaire
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur le bouton "Valider".
     * Elle lit les valeurs des champs de texte, calcule les amortissements et affiche le résultat.
     */
    @FXML
    private void doCalculer() {
        try {
            double capital = Double.parseDouble(txtCapital.getText());
            int duree = Integer.parseInt(txtDuree.getText()) * 12; // Convertir en mois
            double taux = Double.parseDouble(txtTaux.getText()) / 100 / 12; // Convertir en taux mensuel
            double tauxAssurance = 0;

            if (chkAssurance.isSelected()) {
                tauxAssurance = Double.parseDouble(txtAssurance.getText()) / 100 / 12; // Convertir en taux mensuel
            }

            List<Amortissement> amortissements = calculerAmortissement(capital, taux, tauxAssurance, duree, chkAssurance.isSelected());
            afficherTableauAmortissement(amortissements);
        } catch (NumberFormatException e) {
            showError("Veuillez entrer des valeurs numériques valides.");
        }
    }

    /**
     * Calcule les amortissements d'un crédit en utilisant la méthode des mensualités constantes,
     * en incluant le coût de l'assurance si nécessaire.
     *
     * @param capital le capital emprunté
     * @param taux le taux d'intérêt mensuel
     * @param tauxAssurance le taux d'assurance mensuel
     * @param duree la durée du crédit en mois
     * @param inclureAssurance un booléen indiquant si l'assurance doit être incluse
     * @return une liste d'amortissements
     */
    private List<Amortissement> calculerAmortissement(double capital, double taux, double tauxAssurance, int duree, boolean inclureAssurance) {
        List<Amortissement> amortissements = new ArrayList<>();
        double mensualite = capital * taux / (1 - Math.pow(1 + taux, -duree));
        double assuranceMensuelle = inclureAssurance ? capital * tauxAssurance : 0;

        double capitalRestant = capital;
        for (int mois = 1; mois <= duree; mois++) {
            double interet = capitalRestant * taux;
            double capitalRembourse;

            if (mois == duree) {
                // Pour le dernier mois, ajuster le remboursement du capital pour éviter un montant négatif
                capitalRembourse = capitalRestant;
            } else {
                capitalRembourse = mensualite - interet;
            }

            capitalRestant -= capitalRembourse;

            // S'assurer que le capital restant ne soit pas négatif (pour le cas où on aurait des arrondis)
            if (capitalRestant < 0) {
                capitalRestant = 0;
            }

            amortissements.add(new Amortissement(mois, capitalRestant, interet, capitalRembourse, mensualite, assuranceMensuelle));
        }

        return amortissements;
    }


    /**
     * Affiche le tableau d'amortissement dans une nouvelle fenêtre.
     *
     * @param amortissements la liste des amortissements à afficher
     */
    private void afficherTableauAmortissement(List<Amortissement> amortissements) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableauAmortissement.fxml"));
            Parent root = loader.load();

            TableauAmortissementViewController controller = loader.getController();
            controller.setAmortissements(amortissements);

            Stage stage = new Stage();
            stage.setTitle("Tableau d'Amortissement");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche un message d'erreur à l'utilisateur.
     *
     * @param message le message d'erreur à afficher
     */
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Méthode appelée lorsque l'utilisateur souhaite retourner à la fenêtre précédente.
     * Elle récupère la référence de la fenêtre actuelle à partir du bouton "Valider",
     * puis ferme cette fenêtre en utilisant sa méthode close().
     */
    @FXML
    private void retour() {
        Stage stage = (Stage) txtCapital.getScene().getWindow();
        stage.close();
    }
}
