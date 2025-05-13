package service;

import com.example.padel.Participant;
import utils.Myconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationService {

    private Connection connection;

    // Limite maximale de participants par tournoi
    private static final int MAX_PARTICIPANTS = 32;

    public RegistrationService() {
        connection = Myconnection.getConnection();
    }

    /**
     * Ajoute un participant à un tournoi.
     */
    public boolean addParticipant(int tournamentId, String firstName, String lastName, String phoneNumber) {
        // Vérifier si le tournoi est plein
        if (isTournamentFull(tournamentId)) {
            // Ajouter le participant à la liste d'attente
            String query = "INSERT INTO tournament_registrations (tournament_id, first_name, last_name, phone_number, status) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, tournamentId);
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, phoneNumber);
                pstmt.setString(5, "EN_ATTENTE"); // Statut "EN_ATTENTE"

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Participant ajouté à la liste d'attente.");
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de l'ajout du participant à la liste d'attente : " + e.getMessage());
            }
            return false;
        }

        // Ajouter le participant comme "INSCRIT"
        String query = "INSERT INTO tournament_registrations (tournament_id, first_name, last_name, phone_number, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tournamentId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, "INSCRIT");

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du participant : " + e.getMessage());
        }
        return false;
    }

    /**
     * Supprime un participant d'un tournoi.
     */
    public boolean removeParticipant(int tournamentId, String firstName, String lastName) {
        String query = "DELETE FROM tournament_registrations WHERE tournament_id = ? AND first_name = ? AND last_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tournamentId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du participant : " + e.getMessage());
        }
        return false;
    }

    /**
     * Récupère la liste des participants inscrits pour un tournoi.
     */
    public List<Participant> getRegisteredParticipants(int tournamentId) {
        List<Participant> participants = new ArrayList<>();
        String query = "SELECT participant_first_name, participant_last_name, participant_phone_number FROM tournament_registrations WHERE tournament_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tournamentId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String firstName = rs.getString("participant_first_name");
                String lastName = rs.getString("participant_last_name");
                String phoneNumber = rs.getString("participant_phone_number");

                Participant participant = new Participant(firstName, lastName, phoneNumber);
                participants.add(participant);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des participants : " + e.getMessage());
        }
        return participants;
    }

    /**
     * Récupère la liste d'attente pour un tournoi.
     */
    public List<Participant> getWaitingList(int tournamentId) {
        List<Participant> waitingList = new ArrayList<>();
        String query = "SELECT first_name, last_name, phone_number FROM tournament_registrations WHERE tournament_id = ? AND status = 'EN_ATTENTE'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tournamentId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String phoneNumber = rs.getString("phone_number");
                waitingList.add(new Participant(firstName, lastName, phoneNumber));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la liste d'attente : " + e.getMessage());
        }
        return waitingList;
    }

    /**
     * Vérifie si un participant existe dans un tournoi.
     */
    public boolean participantExists(int tournamentId, String firstName, String lastName) {
        String query = "SELECT COUNT(*) AS count FROM tournament_registrations WHERE tournament_id = ? AND first_name = ? AND last_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tournamentId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du participant : " + e.getMessage());
        }
        return false;
    }

    /**
     * Vérifie si un tournoi est plein.
     */
    public boolean isTournamentFull(int tournamentId) {
        String query = "SELECT COUNT(*) AS count FROM tournament_registrations WHERE tournament_id = ? AND status = 'INSCRIT'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tournamentId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count >= MAX_PARTICIPANTS; // Vérifier si le nombre de participants atteint ou dépasse 32
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la capacité du tournoi : " + e.getMessage());
        }
        return false;
    }

    /**
     * Modifie un participant existant.
     */
    public boolean updateParticipant(Participant updatedParticipant) {
        String query = "UPDATE tournament_registrations SET participant_first_name = ?, participant_last_name = ?, participant_phone_number = ? WHERE participant_phone_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, updatedParticipant.getFirstName());
            pstmt.setString(2, updatedParticipant.getLastName());
            pstmt.setString(3, updatedParticipant.getPhoneNumber()); // Nouveau numéro de téléphone
            pstmt.setString(4, updatedParticipant.getPhoneNumber()); // Ancien numéro de téléphone

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du participant : " + e.getMessage());
            return false;
        }
    }
    /**
     * Supprime un participant par son numéro de téléphone.
     */
    public boolean deleteParticipant(String phoneNumber) {
        String query = "DELETE FROM tournament_registrations WHERE participant_phone_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, phoneNumber);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du participant : " + e.getMessage());
            return false;
        }
    }
}