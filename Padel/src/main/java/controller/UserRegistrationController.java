package controller;

import com.example.padel.Tournament;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.TwilioService; // Importez la classe TwilioService
import service.TournamentService;

import java.util.List;

public class UserRegistrationController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private ComboBox<Tournament> tournamentComboBox;

    private TournamentService tournamentService;

    /**
     * Définit le service de gestion des tournois.
     */
    public void setRegistrationService(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
        loadTournaments();
    }

    /**
     * Charge les tournois disponibles dans la ComboBox.
     */
    private void loadTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        if (tournaments != null && !tournaments.isEmpty()) {
            tournamentComboBox.getItems().clear();
            tournamentComboBox.getItems().addAll(tournaments);
            tournamentComboBox.setConverter(new javafx.util.StringConverter<Tournament>() {
                @Override
                public String toString(Tournament tournament) {
                    return tournament.getTitre();
                }

                @Override
                public Tournament fromString(String string) {
                    return null; // Non utilisé ici
                }
            });
        } else {
            System.out.println("Aucun tournoi disponible.");
        }
    }

    /**
     * Gère l'action d'inscription à un tournoi.
     */
    @FXML
    private void handleRegister(ActionEvent event) {
        Tournament selectedTournament = tournamentComboBox.getValue();

        // Validation de la sélection du tournoi
        if (selectedTournament == null) {
            showAlert(Alert.AlertType.WARNING, "Erreur de sélection", "Veuillez sélectionner un tournoi.");
            return;
        }

        // Récupérer les valeurs des champs
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();

        // Validation des champs obligatoires
        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Validation du format du numéro de téléphone
        if (!isValidPhoneNumber(phoneNumber)) {
            showAlert(Alert.AlertType.WARNING, "Numéro invalide", "Le numéro de téléphone doit commencer par '+216' et contenir 8 chiffres après.");
            return;
        }

        // Appeler le service pour inscrire le participant
        boolean isRegistered = tournamentService.registerParticipant(selectedTournament.getId(), firstName, lastName, phoneNumber);
        if (isRegistered) {
            // Envoyer un SMS de confirmation
            String messageBody = "Hbibi vous êtes inscrit avec succès au " + selectedTournament.getTitre();
            TwilioService.sendSMS("+21655596014", messageBody); // Numéro fixe pour recevoir le SMS

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Vous êtes inscrit avec succès !");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'inscription.");
        }
    }

    /**
     * Gère l'action de désinscription d'un tournoi.
     */
    @FXML
    private void handleUnregister(ActionEvent event) {
        Tournament selectedTournament = tournamentComboBox.getValue();

        // Validation de la sélection du tournoi
        if (selectedTournament == null) {
            showAlert(Alert.AlertType.WARNING, "Erreur de sélection", "Veuillez sélectionner un tournoi.");
            return;
        }

        // Récupérer les valeurs des champs
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();

        // Validation des champs obligatoires
        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Validation du format du numéro de téléphone
        if (!isValidPhoneNumber(phoneNumber)) {
            showAlert(Alert.AlertType.WARNING, "Numéro invalide", "Le numéro de téléphone doit commencer par '+216' et contenir 8 chiffres après.");
            return;
        }

        // Appeler le service pour désinscrire le participant
        boolean isUnregistered = tournamentService.unregisterParticipant(selectedTournament.getId(), firstName, lastName, phoneNumber);
        if (isUnregistered) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Vous avez été désinscrit avec succès !");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la désinscription.");
        }
    }

    /**
     * Efface les champs après inscription ou désinscription.
     */
    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        tournamentComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Affiche une alerte.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Valide le format du numéro de téléphone.
     * Le numéro doit commencer par '+216' et contenir 8 chiffres après.
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\+216\\d{8}");
    }
}