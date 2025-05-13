package controller;

import com.example.padel.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.AdminService;

import java.time.LocalDate;

public class UpdateEventController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField locationField;

    @FXML
    private DatePicker startDatePicker;

    private final AdminService adminService = new AdminService();
    private Event selectedEvent;

    /**
     * Initialise les champs du formulaire avec les données de l'événement sélectionné.
     */
    public void initData(Event event) {
        this.selectedEvent = event;

        titleField.setText(event.getTitre());
        locationField.setText(event.getLieu());
        startDatePicker.setValue(event.getDateDebut());
    }

    /**
     * Enregistre les modifications apportées à l'événement sélectionné.
     */
    @FXML
    private void saveChanges() {
        String newTitle = titleField.getText();
        String newLocation = locationField.getText();
        LocalDate newStartDate = startDatePicker.getValue();

        if (newTitle.isEmpty() || newLocation.isEmpty() || newStartDate == null) {
            System.err.println("Veuillez remplir tous les champs.");
            return;
        }

        // Mettre à jour l'objet sélectionné
        selectedEvent.setTitre(newTitle);
        selectedEvent.setLieu(newLocation);
        selectedEvent.setDateDebut(newStartDate);

        // Appeler le service pour enregistrer les modifications dans la base de données
        boolean success = adminService.updateEvent(selectedEvent);
        if (success) {
            System.out.println("Événement modifié avec succès !");
        } else {
            System.err.println("Erreur lors de la modification de l'événement.");
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