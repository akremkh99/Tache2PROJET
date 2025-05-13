package controller;

import com.example.padel.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.EventService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditEventController implements Initializable {

    @FXML
    private TextField titreField;

    @FXML
    private TextField lieuField;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextField nbPlacesField;

    private EventService eventService;
    private Event eventToEdit;

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setEvent(Event event) {
        this.eventToEdit = event;

        // Pré-remplir les champs avec les valeurs actuelles de l'événement
        titreField.setText(event.getTitre());
        lieuField.setText(event.getLieu());
        dateDebutPicker.setValue(event.getDateDebut());
        dateFinPicker.setValue(event.getDateFin());
        nbPlacesField.setText(String.valueOf(event.getNbPlaces()));
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (validateInput()) {
            try {
                // Mettre à jour les propriétés de l'événement
                eventToEdit.setTitre(titreField.getText());
                eventToEdit.setLieu(lieuField.getText());
                eventToEdit.setDateDebut(dateDebutPicker.getValue());
                eventToEdit.setDateFin(dateFinPicker.getValue());
                eventToEdit.setNbPlaces(Integer.parseInt(nbPlacesField.getText()));

                // Sauvegarder les modifications dans la base de données
                if (eventService.updateEvent(eventToEdit)) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement modifié avec succès");
                    closeStage();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification de l'événement");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur inattendue est survenue : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeStage();
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (titreField.getText().isEmpty()) {
            errors.append("- Le titre est obligatoire\n");
        }

        if (lieuField.getText().isEmpty()) {
            errors.append("- Le lieu est obligatoire\n");
        }

        if (dateDebutPicker.getValue() == null) {
            errors.append("- La date de début est obligatoire\n");
        }

        if (dateFinPicker.getValue() == null) {
            errors.append("- La date de fin est obligatoire\n");
        } else if (dateDebutPicker.getValue() != null &&
                dateFinPicker.getValue().isBefore(dateDebutPicker.getValue())) {
            errors.append("- La date de fin doit être après la date de début\n");
        }

        if (nbPlacesField.getText().isEmpty()) {
            errors.append("- Le nombre de places est obligatoire\n");
        } else {
            try {
                int nbPlaces = Integer.parseInt(nbPlacesField.getText());
                if (nbPlaces < 100 || nbPlaces > 500) {
                    errors.append("- Le nombre de places doit être compris entre 100 et 500\n");
                }
            } catch (NumberFormatException e) {
                errors.append("- Le nombre de places doit être un nombre valide\n");
            }
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", errors.toString());
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeStage() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation par défaut
    }
}