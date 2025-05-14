package controller;

import com.example.padel.Event;
import com.example.padel.Participant;
import com.example.padel.Tournament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.AdminService;

import java.io.IOException;

public class AdminController3 {

    // ListView unifiée pour les tournois, événements, et participants
    @FXML
    private ListView<Object> adminListView;

    // Barre de sélection pour les inscriptions
    @FXML
    private HBox registrationSelectionBar;

    @FXML
    private ComboBox<String> tournamentComboBox;

    // Boutons Modifier et Supprimer
    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private final AdminService adminService = new AdminService();
    private ObservableList<Tournament> currentTournaments = FXCollections.observableArrayList();
    private ObservableList<Event> currentEvents = FXCollections.observableArrayList();
    private ObservableList<Participant> currentParticipants = FXCollections.observableArrayList();

    private String currentView = "tournaments"; // Pour suivre la vue actuelle (tournaments, events, participants)

    /**
     * Méthode d'initialisation appelée automatiquement lors du chargement du FXML.
     */
    @FXML
    public void initialize() {
        // Configurer la CellFactory pour personnaliser l'affichage
        setupListViewCellFactory();

        // Ajouter un écouteur pour le ComboBox
        tournamentComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && currentView.equals("participants")) {
                loadParticipantsForTournamentFromCombo();
            }
        });

        // Charger les données par défaut (par exemple, les tournois)
        loadTournois();
    }

    /**
     * Configure la CellFactory pour personnaliser l'affichage des éléments dans la ListView.
     */
    private void setupListViewCellFactory() {
        adminListView.setCellFactory(listView -> new ListCell<Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                if (item instanceof Tournament) {
                    Tournament tournament = (Tournament) item;
                    VBox container = new VBox(2);

                    HBox idRow = createStyledRow("ID: ", String.valueOf(tournament.getId()));
                    HBox titleRow = createStyledRow("Titre: ", tournament.getTitre());
                    HBox locationRow = createStyledRow("Lieu: ", tournament.getLieu());
                    HBox startDateRow = createStyledRow("Début: ", tournament.getDateDebut() != null ? String.valueOf(tournament.getDateDebut()) : "");
                    HBox statusRow = createStyledRow("Statut: ", tournament.getStatus() != null ? tournament.getStatus() : "");

                    container.getChildren().addAll(idRow, titleRow, locationRow, startDateRow, statusRow);

                    // Définir la couleur de fond en fonction du statut (inchangé)
                    String backgroundColor;
                    if (tournament.getStatus() != null) {
                        switch (tournament.getStatus().toLowerCase()) {
                            case "ouvert":
                                backgroundColor = "-fx-background-color: #91f97e;";
                                break;
                            case "fermé":
                                backgroundColor = "-fx-background-color: #ffcdd2;";
                                break;
                            default:
                                backgroundColor = "-fx-background-color: #FFFFFF;";
                                break;
                        }
                    } else {
                        backgroundColor = "-fx-background-color: #FFFFFF;";
                    }
                    container.setStyle(backgroundColor);

                    setGraphic(container);
                } else if (item instanceof Event) {
                    Event event = (Event) item;
                    VBox container = new VBox(2);

                    HBox idRow = createStyledRow("ID: ", String.valueOf(event.getId()));
                    HBox titleRow = createStyledRow("Titre: ", event.getTitre());
                    HBox locationRow = createStyledRow("Lieu: ", event.getLieu());
                    HBox startDateRow = createStyledRow("Début: ", event.getDateDebut() != null ? String.valueOf(event.getDateDebut()) : "");

                    container.getChildren().addAll(idRow, titleRow, locationRow, startDateRow);

                    // Alternance de couleurs pour les événements (bleu #0078D4 et bleu ciel #4DABF7)
                    int index = getIndex();
                    String backgroundColor = (index % 2 == 0) ? "-fx-background-color: #67f008;" : "-fx-background-color: #4DABF7;";
                    container.setStyle(backgroundColor + " -fx-text-fill: white;"); // Texte blanc pour lisibilité

                    setGraphic(container);
                } else if (item instanceof Participant) {
                    Participant participant = (Participant) item;
                    VBox container = new VBox(2);

                    HBox firstNameRow = createStyledRow("Prénom: ", participant.getFirstName());
                    HBox lastNameRow = createStyledRow("Nom: ", participant.getLastName());
                    HBox phoneRow = createStyledRow("Téléphone: ", participant.getPhoneNumber());

                    container.getChildren().addAll(firstNameRow, lastNameRow, phoneRow);

                    // Alternance de couleurs pour les participants (bleu #0078D4 et bleu ciel #4DABF7)
                    int index = getIndex();
                    String backgroundColor = (index % 2 == 0) ? "-fx-background-color: #67f008;" : "-fx-background-color: #4DABF7;";
                    container.setStyle(backgroundColor + " -fx-text-fill: white;"); // Texte blanc pour lisibilité

                    setGraphic(container);
                } else {
                    setText(item.toString());
                    setGraphic(null);
                }
            }
        });
    }

    /**
     * Crée une ligne avec une étiquette en gras et une valeur normale.
     */
    private HBox createStyledRow(String labelText, String valueText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label value = new Label(valueText);
        value.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333;");

        HBox row = new HBox(5);
        row.getChildren().addAll(label, value);
        return row;
    }

    /**
     * Charge les tournois dans la ListView.
     */
    private void loadTournois() {
        currentTournaments.setAll(adminService.getAllTournaments());
        adminListView.setItems(FXCollections.observableArrayList(currentTournaments));
        currentView = "tournaments";

        editButton.setVisible(true);
        editButton.setManaged(true);
        deleteButton.setVisible(true);
        deleteButton.setManaged(true);
        registrationSelectionBar.setVisible(false);
        registrationSelectionBar.setManaged(false);
    }

    /**
     * Gère l'action "Gestion des Tournois".
     */
    @FXML
    private void handleTournaments() {
        loadTournois();
    }

    /**
     * Charge les événements dans la ListView.
     */
    private void loadEvents() {
        currentEvents.setAll(adminService.getAllEvents());
        adminListView.setItems(FXCollections.observableArrayList(currentEvents));
        currentView = "events";

        editButton.setVisible(true);
        editButton.setManaged(true);
        deleteButton.setVisible(true);
        deleteButton.setManaged(true);
        registrationSelectionBar.setVisible(false);
        registrationSelectionBar.setManaged(false);
    }

    /**
     * Gère l'action "Gestion des Événements".
     */
    @FXML
    private void handleEvents() {
        loadEvents();
    }

    /**
     * Gère l'action "Gestion des Inscriptions".
     */
    @FXML
    private void handleRegistrations() {
        registrationSelectionBar.setVisible(true);
        registrationSelectionBar.setManaged(true);

        editButton.setVisible(true);
        editButton.setManaged(true);
        deleteButton.setVisible(true);
        deleteButton.setManaged(true);

        adminListView.getItems().clear();
        currentView = "participants";

        // Mettre à jour currentTournaments avec les tournois actuels
        currentTournaments.setAll(adminService.getAllTournaments());
        ObservableList<String> tournamentNames = FXCollections.observableArrayList();
        for (Tournament tournament : currentTournaments) {
            tournamentNames.add(tournament.getTitre());
        }

        tournamentComboBox.setItems(tournamentNames);

        if (tournamentNames.isEmpty()) {
            adminListView.getItems().add("Aucun tournoi disponible.");
        } else {
            // Charger les participants pour le premier tournoi par défaut
            if (!tournamentNames.isEmpty()) {
                tournamentComboBox.getSelectionModel().selectFirst();
                loadParticipantsForTournamentFromCombo();
            }
        }
    }

    /**
     * Charge les participants en fonction du tournoi sélectionné dans la ComboBox.
     */
    @FXML
    private void loadParticipantsForTournamentFromCombo() {
        int tournamentId = getSelectedTournamentId();
        if (tournamentId != -1) {
            loadParticipantsForTournament(tournamentId);
        } else {
            adminListView.getItems().clear();
            adminListView.getItems().add("Veuillez sélectionner un tournoi valide.");
        }
    }

    /**
     * Charge les participants inscrits à un tournoi spécifique.
     */
    private void loadParticipantsForTournament(int tournamentId) {
        currentParticipants.setAll(adminService.getRegisteredParticipants(tournamentId));
        adminListView.setItems(FXCollections.observableArrayList(currentParticipants));

        if (currentParticipants.isEmpty()) {
            adminListView.getItems().add("Aucun participant inscrit pour ce tournoi.");
        } else {
            System.out.println("Participants chargés avec succès !");
        }
    }

    /**
     * Gère l'action du bouton "Modifier" en fonction de la vue actuelle.
     */
    @FXML
    private void editSelectedItem() {
        if (currentView.equals("tournaments")) {
            editSelectedTournament();
        } else if (currentView.equals("events")) {
            editSelectedEvent();
        } else if (currentView.equals("participants")) {
            editSelectedParticipant();
        }
    }

    /**
     * Gère l'action du bouton "Supprimer" en fonction de la vue actuelle.
     */
    @FXML
    private void deleteSelectedItem() {
        if (currentView.equals("tournaments")) {
            deleteSelectedTournament();
        } else if (currentView.equals("events")) {
            deleteSelectedEvent();
        } else if (currentView.equals("participants")) {
            deleteSelectedParticipant();
        }
    }

    /**
     * Supprime un tournoi sélectionné.
     */
    @FXML
    private void deleteSelectedTournament() {
        Tournament tournament = (Tournament) adminListView.getSelectionModel().getSelectedItem();
        if (tournament != null) {
            boolean success = adminService.deleteTournament(tournament.getId());
            if (success) {
                System.out.println("Tournoi supprimé avec succès !");
                loadTournois();
            } else {
                System.err.println("Erreur lors de la suppression du tournoi.");
            }
        } else {
            System.out.println("Aucun tournoi sélectionné.");
        }
    }

    /**
     * Modifie un tournoi sélectionné en ouvrant une fenêtre modale.
     */
    @FXML
    private void editSelectedTournament() {
        Tournament tournament = (Tournament) adminListView.getSelectionModel().getSelectedItem();
        if (tournament != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/UpdateTournament.fxml"));
                Parent root = loader.load();

                UpdateTournamentController updateController = loader.getController();
                updateController.initData(tournament);

                Stage stage = new Stage();
                stage.setTitle("Modifier un Tournoi");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                loadTournois();
            } catch (IOException e) {
                System.err.println("Erreur lors de l'ouverture de la fenêtre de modification : " + e.getMessage());
            }
        } else {
            System.out.println("Aucun tournoi sélectionné.");
        }
    }

    /**
     * Supprime un événement sélectionné.
     */
    @FXML
    private void deleteSelectedEvent() {
        Event event = (Event) adminListView.getSelectionModel().getSelectedItem();
        if (event != null) {
            boolean success = adminService.deleteEvent(event.getId());
            if (success) {
                System.out.println("Événement supprimé avec succès !");
                loadEvents();
            } else {
                System.err.println("Erreur lors de la suppression de l'événement.");
            }
        } else {
            System.out.println("Aucun événement sélectionné.");
        }
    }

    /**
     * Modifie un événement sélectionné en ouvrant une fenêtre modale.
     */
    @FXML
    private void editSelectedEvent() {
        Event event = (Event) adminListView.getSelectionModel().getSelectedItem();
        if (event != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/UpdateEvent.fxml"));
                Parent root = loader.load();

                UpdateEventController updateController = loader.getController();
                updateController.initData(event);

                Stage stage = new Stage();
                stage.setTitle("Modifier un Événement");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                loadEvents();
            } catch (IOException e) {
                System.err.println("Erreur lors de l'ouverture de la fenêtre de modification : " + e.getMessage());
            }
        } else {
            System.out.println("Aucun événement sélectionné.");
        }
    }

    /**
     * Supprime un participant sélectionné.
     */
    @FXML
    private void deleteSelectedParticipant() {
        Participant participant = (Participant) adminListView.getSelectionModel().getSelectedItem();
        if (participant != null) {
            boolean success = adminService.deleteParticipant(participant.getPhoneNumber());
            if (success) {
                System.out.println("Participant supprimé avec succès !");
                loadParticipantsForTournament(getSelectedTournamentId());
            } else {
                System.err.println("Erreur lors de la suppression du participant.");
            }
        } else {
            System.out.println("Aucun participant sélectionné.");
        }
    }

    /**
     * Modifie un participant sélectionné en ouvrant une fenêtre modale.
     */
    @FXML
    private void editSelectedParticipant() {
        Participant participant = (Participant) adminListView.getSelectionModel().getSelectedItem();
        if (participant != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/UpdateParticipant.fxml"));
                Parent root = loader.load();

                UpdateParticipantController updateController = loader.getController();
                updateController.initData(participant);

                Stage stage = new Stage();
                stage.setTitle("Modifier un Participant");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                loadParticipantsForTournament(getSelectedTournamentId());
            } catch (IOException e) {
                System.err.println("Erreur lors de l'ouverture de la fenêtre de modification : " + e.getMessage());
            }
        } else {
            System.out.println("Aucun participant sélectionné.");
        }
    }

    /**
     * Récupère l'ID du tournoi sélectionné dans la ComboBox.
     */
    private int getSelectedTournamentId() {
        String selectedTournamentName = tournamentComboBox.getValue();
        if (selectedTournamentName != null) {
            Tournament selectedTournament = currentTournaments.stream()
                    .filter(t -> t.getTitre().equals(selectedTournamentName))
                    .findFirst()
                    .orElse(null);
            return selectedTournament != null ? selectedTournament.getId() : -1;
        }
        return -1;
    }
}