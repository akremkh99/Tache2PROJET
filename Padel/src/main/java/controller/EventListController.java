package controller;

import com.example.padel.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.EventService;

import java.io.IOException;
import java.util.Optional;

public class EventListController {

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, Integer> idColumn;

    @FXML
    private TableColumn<Event, String> titreColumn;

    @FXML
    private TableColumn<Event, String> lieuColumn;

    @FXML
    private TableColumn<Event, String> dateDebutColumn;

    @FXML
    private TableColumn<Event, String> dateFinColumn;

    @FXML
    private TableColumn<Event, Integer> nbPlacesColumn;

    private ObservableList<Event> eventList = FXCollections.observableArrayList();
    private EventService eventService;

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
        loadEvents();
    }

    @FXML
    private void handleAddEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/AddEvent.fxml"));
            Parent root = loader.load();

            AddEventController controller = loader.getController();
            controller.setEventService(eventService);
            controller.setEventList(eventList);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajouter un événement");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Recharger la liste après ajout
            loadEvents();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'ajout.");
        }
    }

    @FXML
    private void handleEditEvent(ActionEvent event) {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/EditEvent.fxml"));
                Parent root = loader.load();

                EditEventController controller = loader.getController();
                controller.setEventService(eventService);
                controller.setEvent(selectedEvent);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Modifier un événement");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                // Recharger la liste après modification
                loadEvents();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
        }
    }

    @FXML
    private void handleDeleteEvent(ActionEvent event) {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Suppression d'un événement");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean deleted = eventService.deleteEvent(selectedEvent.getId());
                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement supprimé avec succès.");
                    loadEvents(); // Recharger la liste après suppression
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'événement.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné", "Veuillez sélectionner un événement à supprimer.");
        }
    }

    @FXML
    private void handleRefreshEvents(ActionEvent event) {
        loadEvents();
    }

    private void loadEvents() {
        eventList.clear();
        eventList.addAll(eventService.getAllEvents());

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titreColumn.setCellValueFactory(cellData -> cellData.getValue().titreProperty());
        lieuColumn.setCellValueFactory(cellData -> cellData.getValue().lieuProperty());
        dateDebutColumn.setCellValueFactory(cellData -> cellData.getValue().dateDebutProperty().asString());
        dateFinColumn.setCellValueFactory(cellData -> cellData.getValue().dateFinProperty().asString());
        nbPlacesColumn.setCellValueFactory(cellData -> cellData.getValue().nbPlacesProperty().asObject());

        eventTable.setItems(eventList);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}