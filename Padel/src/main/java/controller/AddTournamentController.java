package controller;

import com.example.padel.Tournament;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import service.TournamentService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddTournamentController implements Initializable {

    @FXML
    private TextField titreField;

    @FXML
    private TextField lieuField;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextField nbParticipantsMaxField;

    @FXML
    private ComboBox<String> statusComboBox; // ComboBox pour le statut

    @FXML
    private TextField latitudeField;

    @FXML
    private TextField longitudeField;

    @FXML
    private WebView mapWebView;

    private TournamentService tournamentService;

    public void setTournamentService(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Remplir la ComboBox des statuts avec "Ouvert" et "Fermé"
        statusComboBox.getItems().addAll("Ouvert", "Fermé");
        statusComboBox.setValue("Ouvert"); // Valeur par défaut

        // Charger la carte OpenLayers
        String mapUrl = getClass().getResource("/com/example/padel/openLayers.html").toExternalForm();
        WebEngine webEngine = mapWebView.getEngine();
        webEngine.load(mapUrl);

        // Attendre que la page soit chargée
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                System.out.println("Page chargée, connexion des champs...");
                // Connecter les champs JavaFX à JavaScript
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("latitudeField", latitudeField);
                window.setMember("longitudeField", longitudeField);
            }
        });
    }

    @FXML
    private void showMap(ActionEvent event) {
        mapWebView.setVisible(true);
        mapWebView.setManaged(true);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (validateInput()) {
            try {
                double latitude = Double.parseDouble(latitudeField.getText());
                double longitude = Double.parseDouble(longitudeField.getText());

                Tournament newTournament = new Tournament(
                        -1, // ID temporaire (-1 car généré par la base de données)
                        titreField.getText(),
                        lieuField.getText(),
                        dateDebutPicker.getValue(),
                        dateFinPicker.getValue(),
                        Integer.parseInt(nbParticipantsMaxField.getText()),
                        statusComboBox.getValue(), // Utiliser la valeur sélectionnée ("Ouvert" ou "Fermé")
                        latitude,
                        longitude
                );

                if (tournamentService.addTournament(newTournament)) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Tournoi ajouté avec succès");
                    closeStage();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du tournoi");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les coordonnées doivent être des nombres valides.");
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

        if (nbParticipantsMaxField.getText().isEmpty()) {
            errors.append("- Le nombre de participants max est obligatoire\n");
        } else {
            try {
                int nbParticipantsMax = Integer.parseInt(nbParticipantsMaxField.getText());
                if (nbParticipantsMax <= 0) {
                    errors.append("- Le nombre de participants max doit être supérieur à 0\n");
                }
            } catch (NumberFormatException e) {
                errors.append("- Le nombre de participants max doit être un nombre valide\n");
            }
        }

        if (statusComboBox.getValue() == null) {
            errors.append("- Le statut est obligatoire\n");
        }

        if (latitudeField.getText().isEmpty() || longitudeField.getText().isEmpty()) {
            errors.append("- Veuillez sélectionner un emplacement sur la carte\n");
        } else {
            try {
                double latitude = Double.parseDouble(latitudeField.getText());
                double longitude = Double.parseDouble(longitudeField.getText());

                if (latitude < -90 || latitude > 90) {
                    errors.append("- La latitude doit être comprise entre -90 et 90\n");
                }

                if (longitude < -180 || longitude > 180) {
                    errors.append("- La longitude doit être comprise entre -180 et 180\n");
                }
            } catch (NumberFormatException e) {
                errors.append("- Les coordonnées doivent être des nombres valides\n");
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
}