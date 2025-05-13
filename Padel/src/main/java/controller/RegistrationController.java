package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import service.RegistrationService;
import com.example.padel.Participant;

public class RegistrationController {

    @FXML
    private TableView<Participant> registeredParticipantsTable;

    @FXML
    private TableColumn<Participant, String> lastNameColumn;

    @FXML
    private TableColumn<Participant, String> firstNameColumn;

    @FXML
    private TableColumn<Participant, String> phoneNumberColumn;

    @FXML
    private TableView<Participant> waitingListTable;

    @FXML
    private TableColumn<Participant, String> waitingLastNameColumn;

    @FXML
    private TableColumn<Participant, String> waitingFirstNameColumn;

    @FXML
    private TableColumn<Participant, String> waitingPhoneNumberColumn;

    private RegistrationService registrationService;
    private int tournamentId;

    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
        loadParticipants();
    }

    /**
     * Charge les participants inscrits et la liste d'attente.
     */
    private void loadParticipants() {
        // Participants inscrits
        ObservableList<Participant> registeredParticipants = FXCollections.observableArrayList(registrationService.getRegisteredParticipants(tournamentId));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        registeredParticipantsTable.setItems(registeredParticipants);

        // Liste d'attente
        ObservableList<Participant> waitingList = FXCollections.observableArrayList(registrationService.getWaitingList(tournamentId));
        waitingLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        waitingFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        waitingPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        waitingListTable.setItems(waitingList);
    }
}