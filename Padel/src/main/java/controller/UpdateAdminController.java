package controller;

import com.example.padel.Tournament;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.AdminService;

import java.time.LocalDate;

public class UpdateAdminController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField locationField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    private final AdminService adminService = new AdminService();
    private Tournament selectedTournament; // Objet à modifier

    /**
     * Initialise les champs du formulaire avec les données de l'élément sélectionné.
     */
    public void initData(Tournament tournament) {
        this.selectedTournament = tournament;

        titleField.setText(tournament.getTitre());
        locationField.setText(tournament.getLieu());
        startDatePicker.setValue(tournament.getDateDebut());
        statusChoiceBox.setValue(tournament.getStatus());
    }

    /**
     * Enregistre les modifications apportées à l'élément sélectionné.
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