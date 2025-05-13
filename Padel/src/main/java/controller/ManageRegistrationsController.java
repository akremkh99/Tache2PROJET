package controller;

import com.example.padel.Participant;
import com.example.padel.Tournament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.TournamentService;

import java.util.List;

public class ManageRegistrationsController {

    @FXML
    private TextField searchField; // Barre de recherche

    @FXML
    private TableView<Participant> participantsTable; // Tableau des participants

    @FXML
    private TableColumn<Participant, String> lastNameColumn; // Colonne Nom

    @FXML
    private TableColumn<Participant, String> firstNameColumn; // Colonne Prénom

    @FXML
    private TableColumn<Participant, String> phoneNumberColumn; // Colonne Téléphone

    private Tournament selectedTournament; // Tournoi sélectionné
    private TournamentService tournamentService; // Service pour interagir avec la base de données
    private ObservableList<Participant> allParticipants; // Liste complète des participants

    /**
     * Définit le service de gestion des tournois.
     */
    public void setTournamentService(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    /**
     * Initialise les données du tournoi sélectionné.
     */
    public void initData(Tournament tournament) {
        this.selectedTournament = tournament;
        if (tournament != null) {
            loadParticipants();
        }
    }

    /**
     * Charge les participants inscrits au tournoi dans le tableau.
     */
    private void loadParticipants() {
        // Récupérer les participants depuis le service
        List<Participant> participants = tournamentService.getParticipantsForTournament(selectedTournament.getId());
        allParticipants = FXCollections.observableArrayList(participants);

        // Lier les colonnes aux propriétés des participants
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // Appliquer un filtre dynamique basé sur la barre de recherche
        FilteredList<Participant> filteredParticipants = new FilteredList<>(allParticipants, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredParticipants.setPredicate(participant -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Afficher tous les participants si la barre de recherche est vide
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return participant.getFirstName().toLowerCase().startsWith(lowerCaseFilter) ||
                        participant.getLastName().toLowerCase().startsWith(lowerCaseFilter);
            });
        });

        // Mettre à jour le tableau avec les participants filtrés
        participantsTable.setItems(filteredParticipants);
    }

    /**
     * Gère l'action du bouton "Fermer".
     */
    @FXML
    private void handleClose(ActionEvent event) {
        // Récupérer la fenêtre (Stage) à partir du parent du contrôleur
        Stage stage = (Stage) participantsTable.getScene().getWindow();

        // Fermer la fenêtre
        stage.close();
    }
}