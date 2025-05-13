package controller;

import com.example.padel.Tournament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.TournamentService;

import java.io.IOException;
import java.util.List;

public class TournamentListController {

    @FXML
    private ListView<Tournament> tournamentListView;

    private TournamentService tournamentService;

    public void setTournamentService(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
        initializeListView();
        loadTournaments();
    }

    public void initialize() {
        // Rien à initialiser, puisque WebView est supprimé
    }

    private void initializeListView() {
        // Liste des images pour le côté droit
        String[] tournamentImages = {
                "/com/example/padel/tour1.png",
                "/com/example/padel/tour2.png",
                "/com/example/padel/tour3.png",
                "/com/example/padel/tour4.png",
                "/com/example/padel/tour5.png"
        };

        tournamentListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Tournament tournament, boolean empty) {
                super.updateItem(tournament, empty);

                if (empty || tournament == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    // Créer une mise en page avec HBox pour diviser en deux moitiés
                    HBox hbox = new HBox(10); // Espacement de 10px entre les éléments
                    hbox.setStyle("-fx-padding: 5;"); // Réduire le padding global

                    // VBox pour les informations à gauche
                    VBox infoBox = new VBox(2); // Réduire l'espacement vertical
                    infoBox.setPrefWidth(USE_COMPUTED_SIZE);
                    infoBox.setMaxWidth(Double.MAX_VALUE);

                    // Label pour le titre (Nom)
                    Label titleLabel = new Label("Nom: " + tournament.getTitre());
                    titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #003087; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);");

                    // Label pour le lieu
                    Label lieuLabel = new Label("Lieu: " + tournament.getLieu());
                    lieuLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #28A745; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);");

                    // Label pour les dates
                    Label dateLabel = new Label("Date: " + tournament.getDateDebut() + " à " + tournament.getDateFin());
                    dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #003087; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);");

                    // Label pour les participants max
                    Label participantsLabel = new Label("Participants max: " + tournament.getNbParticipantsMax());
                    participantsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #666666; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);");

                    // Label pour le statut
                    Label statusLabel = new Label("Statut: " + tournament.getStatus());
                    statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                            (tournament.getStatus().toLowerCase().equals("ouvert") ? "-fx-text-fill: #228B22;" : "-fx-text-fill: #C71585;") +
                            " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);");

                    // Label pour la latitude
                    Label latitudeLabel = new Label("Latitude: " + String.format("%.4f", tournament.getLatitude()));
                    latitudeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #003087; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);");

                    // Label pour la longitude
                    Label longitudeLabel = new Label("Longitude: " + String.format("%.4f", tournament.getLongitude()));
                    longitudeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #003087; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);");

                    // Ajouter les labels au VBox
                    infoBox.getChildren().addAll(titleLabel, lieuLabel, dateLabel, participantsLabel, statusLabel, latitudeLabel, longitudeLabel);

                    // Appliquer la couleur de fond selon le statut
                    String backgroundColor;
                    switch (tournament.getStatus().toLowerCase()) {
                        case "ouvert":
                            backgroundColor = "-fx-background-color: #61ff00;";
                            break;
                        case "fermé":
                            backgroundColor = "-fx-background-color: #ff5151;";
                            break;
                        default:
                            backgroundColor = "-fx-background-color: #FFFFFF;";
                            break;
                    }
                    infoBox.setStyle(infoBox.getStyle() + backgroundColor);

                    // Image à droite
                    int index = tournamentListView.getItems().indexOf(tournament);
                    String imagePath = tournamentImages[index % tournamentImages.length];
                    ImageView imageView = new ImageView(new Image(imagePath));
                    imageView.setFitWidth(150); // Réduire la largeur de l'image à 150px
                    imageView.setPreserveRatio(true); // Préserver les proportions
                    imageView.setSmooth(true);
                    imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);");

                    // Ajuster dynamiquement la hauteur de l'image pour correspondre à infoBox
                    infoBox.heightProperty().addListener((obs, oldHeight, newHeight) -> {
                        imageView.setFitHeight(newHeight.doubleValue());
                    });

                    // Calculer la largeur minimale basée sur le texte le plus long + 1cm (20px) de marge totale
                    double maxTextWidth = Math.max(
                            titleLabel.getLayoutBounds().getWidth(),
                            Math.max(lieuLabel.getLayoutBounds().getWidth(),
                                    Math.max(dateLabel.getLayoutBounds().getWidth(),
                                            Math.max(participantsLabel.getLayoutBounds().getWidth(),
                                                    Math.max(statusLabel.getLayoutBounds().getWidth(),
                                                            Math.max(latitudeLabel.getLayoutBounds().getWidth(),
                                                                    longitudeLabel.getLayoutBounds().getWidth()))))))
                            + 20; // Ajouter 20px (1cm de chaque côté)
                    infoBox.setMinWidth(maxTextWidth);
                    infoBox.setPrefWidth(maxTextWidth);

                    // Diviser l'HBox en deux parties et centrer verticalement
                    HBox.setHgrow(infoBox, Priority.ALWAYS);
                    HBox.setHgrow(imageView, Priority.ALWAYS);
                    hbox.setAlignment(Pos.CENTER);

                    // Ajouter les éléments au HBox
                    hbox.getChildren().addAll(infoBox, imageView);

                    setGraphic(hbox);
                }
            }
        });
    }

    private void loadTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        ObservableList<Tournament> observableTournaments = FXCollections.observableArrayList(tournaments);
        tournamentListView.setItems(observableTournaments);
    }

    @FXML
    private void handleAddTournament(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/AddTournament.fxml"));
            Scene scene = new Scene(loader.load());
            AddTournamentController controller = loader.getController();
            controller.setTournamentService(tournamentService);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un tournoi");
            stage.setScene(scene);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.showAndWait();

            loadTournaments();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'écran d'ajout de tournoi.");
        }
    }

    @FXML
    private void handleEditTournament(ActionEvent event) {
        Tournament selectedTournament = tournamentListView.getSelectionModel().getSelectedItem();
        if (selectedTournament == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un tournoi à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/EditTournament.fxml"));
            Scene scene = new Scene(loader.load());
            EditTournamentController controller = loader.getController();
            controller.setTournamentService(tournamentService);
            controller.initData(selectedTournament);

            Stage stage = new Stage();
            stage.setTitle("Modifier un tournoi");
            stage.setScene(scene);
            stage.setWidth(800); // Augmenté de 600 à 800
            stage.setHeight(600); // Augmenté de 400 à 600
            stage.showAndWait();

            loadTournaments();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'écran de modification de tournoi.");
        }
    }

    @FXML
    private void handleDeleteTournament(ActionEvent event) {
        Tournament selectedTournament = tournamentListView.getSelectionModel().getSelectedItem();
        if (selectedTournament == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un tournoi à supprimer.");
            return;
        }

        boolean isDeleted = tournamentService.deleteTournament(selectedTournament.getId());
        if (isDeleted) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le tournoi a été supprimé avec succès.");
            loadTournaments();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le tournoi.");
        }
    }

    @FXML
    private void handleRefreshTournaments(ActionEvent event) {
        loadTournaments();
    }

    @FXML
    private void handleManageRegistrations(ActionEvent event) {
        Tournament selectedTournament = tournamentListView.getSelectionModel().getSelectedItem();
        if (selectedTournament == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un tournoi pour gérer les inscriptions.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/ManageRegistrations.fxml"));
            Scene scene = new Scene(loader.load());
            ManageRegistrationsController controller = loader.getController();
            controller.setTournamentService(tournamentService);
            controller.initData(selectedTournament);

            Stage stage = new Stage();
            stage.setTitle("Gérer les inscriptions - " + selectedTournament.getTitre());
            stage.setScene(scene);
            stage.setWidth(600);
            stage.setHeight(400);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'écran de gestion des inscriptions.");
        }
    }

    @FXML
    private void showMap() {
        // Méthode laissée vide pour éviter une erreur, mais elle sera ignorée
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}