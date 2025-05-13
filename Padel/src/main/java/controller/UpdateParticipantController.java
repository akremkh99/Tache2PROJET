package controller;

import com.example.padel.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.AdminService;

public class UpdateParticipantController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    private final AdminService adminService = new AdminService();
    private Participant selectedParticipant;

    /**
     * Initialise les champs du formulaire avec les données du participant sélectionné.
     */
    public void initData(Participant participant) {
        this.selectedParticipant = participant;

        firstNameField.setText(participant.getFirstName());
        lastNameField.setText(participant.getLastName());
        phoneNumberField.setText(participant.getPhoneNumber());
    }

    /**
     * Enregistre les modifications apportées au participant sélectionné.
     */
    @FXML
    private void saveChanges() {
        String newFirstName = firstNameField.getText();
        String newLastName = lastNameField.getText();
        String newPhoneNumber = phoneNumberField.getText();

        if (newFirstName.isEmpty() || newLastName.isEmpty() || newPhoneNumber.isEmpty()) {
            System.err.println("Veuillez remplir tous les champs.");
            return;
        }

        // Mettre à jour l'objet sélectionné
        selectedParticipant.setFirstName(newFirstName);
        selectedParticipant.setLastName(newLastName);
        selectedParticipant.setPhoneNumber(newPhoneNumber);

        // Appeler le service pour enregistrer les modifications dans la base de données
        boolean success = adminService.updateParticipant(selectedParticipant);
        if (success) {
            System.out.println("Participant modifié avec succès !");
        } else {
            System.err.println("Erreur lors de la modification du participant.");
        }

        // Fermer la fenêtre modale
        closeWindow();
    }

    /**
     * Annule les modifications et ferme la fenêtre modale.
     */
    @FXML
    private void cancel() {
        closeWindow();
    }

    /**
     * Ferme la fenêtre modale.
     */
    private void closeWindow() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }
}