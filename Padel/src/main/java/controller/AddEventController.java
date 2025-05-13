package controller;

import com.example.padel.Event;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.EventService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddEventController implements Initializable {

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
    private ObservableList<Event> eventList;

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setEventList(ObservableList<Event> eventList) {
        this.eventList = eventList;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (validateInput()) {
            try {
                Event newEvent = new Event();
                newEvent.setTitre(titreField.getText());
                newEvent.setLieu(lieuField.getText());
                newEvent.setDateDebut(dateDebutPicker.getValue());
                newEvent.setDateFin(dateFinPicker.getValue());
                newEvent.setNbPlaces(Integer.parseInt(nbPlacesField.getText()));

                if (eventService.addEvent(newEvent)) {
                    eventList.add(newEvent);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement ajouté avec succès");
                    closeStage();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'événement");
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
        dateDebutPicker.setValue(LocalDate.now());
        dateFinPicker.setValue(LocalDate.now().plusDays(1));
    }
}