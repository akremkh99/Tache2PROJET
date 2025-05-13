package controller;

import com.example.padel.Tournament;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.TournamentService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditTournamentController implements Initializable {

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
    private ComboBox<String> statusComboBox;

    private TournamentService tournamentService;
    private Tournament tournamentToEdit;

    /**
     * Définit le service de gestion des tournois.
     */
    public void setTournamentService(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    /**
     * Initialise les données du tournoi à modifier.
     */
    public void initData(Tournament tournament) {
        this.tournamentToEdit = tournament;
        if (tournament != null) {
            titreField.setText(tournament.getTitre());
            lieuField.setText(tournament.getLieu());
            dateDebutPicker.setValue(tournament.getDateDebut());
            dateFinPicker.setValue(tournament.getDateFin());
            nbParticipantsMaxField.setText(String.valueOf(tournament.getNbParticipantsMax()));
            statusComboBox.setValue(tournament.getStatus());
        }
    }

    /**
     * Gère l'action de mise à jour du tournoi.
     */
    @FXML
    private void handleUpdate(ActionEvent event) {
        if (validateInput()) {
            try {
                // Mettre à jour les données du tournoi
                tournamentToEdit.setTitre(titreField.getText());
                tournamentToEdit.setLieu(lieuField.getText());
                tournamentToEdit.setDateDebut(dateDebutPicker.getValue());
                tournamentToEdit.setDateFin(dateFinPicker.getValue());
                tournamentToEdit.setNbParticipantsMax(Integer.parseInt(nbParticipantsMaxField.getText()));
                tournamentToEdit.setStatus(statusComboBox.getValue());

                // Appeler le service pour mettre à jour le tournoi dans la base de données
                if (tournamentService.updateTournament(tournamentToEdit)) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Tournoi modifié avec succès");
                    closeStage();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification du tournoi");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur inattendue est survenue : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Gère l'action d'annulation.
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        closeStage();
    }

    /**
     * Valide les champs de saisie.
     */
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

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", errors.toString());
            return false;
        }

        return true;
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
     * Ferme la fenêtre.
     */
    private void closeStage() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    /**
     * Initialise les composants de l'interface.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Remplir la liste déroulante des statuts
        statusComboBox.getItems().addAll("Ouvert", "Fermé");
    }
}