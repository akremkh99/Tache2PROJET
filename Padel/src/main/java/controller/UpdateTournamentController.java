package controller;

import com.example.padel.Tournament;
import javafx.collections.FXCollections; // Assurez-vous d'importer cette classe
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.AdminService;

import java.time.LocalDate;

public class UpdateTournamentController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField locationField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    private final AdminService adminService = new AdminService();
    private Tournament selectedTournament;

    /**
     * Initialise les champs du formulaire avec les données du tournoi sélectionné.
     */
    public void initData(Tournament tournament) {
        this.selectedTournament = tournament;

        titleField.setText(tournament.getTitre());
        locationField.setText(tournament.getLieu());
        startDatePicker.setValue(tournament.getDateDebut());

        // Initialiser le ChoiceBox avec les valeurs possibles
        statusChoiceBox.setItems(FXCollections.observableArrayList("ACTIF", "INACTIF"));
        statusChoiceBox.setValue(tournament.getStatus());
    }

    /**
     * Enregistre les modifications apportées au tournoi sélectionné.
     */
    @FXML
    private void saveChanges() {
        String newTitle = titleField.getText();
        String newLocation = locationField.getText();
        LocalDate newStartDate = startDatePicker.getValue();
        String newStatus = statusChoiceBox.getValue();

        if (newTitle.isEmpty() || newLocation.isEmpty() || newStartDate == null || newStatus == null) {
            System.err.println("Veuillez remplir tous les champs.");
            return;
        }

        // Mettre à jour l'objet sélectionné
        selectedTournament.setTitre(newTitle);
        selectedTournament.setLieu(newLocation);
        selectedTournament.setDateDebut(newStartDate);
        selectedTournament.setStatus(newStatus);

        // Appeler le service pour enregistrer les modifications dans la base de données
        boolean success = adminService.updateTournament(selectedTournament);
        if (success) {
            System.out.println("Tournoi modifié avec succès !");
        } else {
            System.err.println("Erreur lors de la modification du tournoi.");
        }

        // Fermer la fenêtre modale
        closeWindow();
    }

    /**
     * Annule les modifications et ferme la fenêtre modale.
     */
    @FXML
    private void cancel() {
        closeWindow();
    }

    /**
     * Ferme la fenêtre modale.
     */
    private void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}