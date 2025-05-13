package service;

import com.example.padel.Participant;
import com.example.padel.Tournament;
import utils.Myconnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TournamentService {

    private Connection connection;

    public TournamentService() {
        connection = Myconnection.getConnection();
    }

    /**
     * Ajoute un nouveau tournoi avec latitude et longitude.
     */
    public boolean addTournament(Tournament tournament) {
        String query = "INSERT INTO tournaments (titre, lieu, date_debut, date_fin, nb_participants_max, status, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, tournament.getTitre());
            pstmt.setString(2, tournament.getLieu());
            pstmt.setString(3, tournament.getDateDebut().toString());
            pstmt.setString(4, tournament.getDateFin().toString());
            pstmt.setInt(5, tournament.getNbParticipantsMax());
            pstmt.setString(6, tournament.getStatus());
            pstmt.setDouble(7, tournament.getLatitude()); // Nouveau champ : latitude
            pstmt.setDouble(8, tournament.getLongitude()); // Nouveau champ : longitude

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du tournoi : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Met à jour un tournoi existant avec latitude et longitude.
     */
    public boolean updateTournament(Tournament tournament) {
        String query = "UPDATE tournaments SET titre = ?, lieu = ?, date_debut = ?, date_fin = ?, nb_participants_max = ?, status = ?, latitude = ?, longitude = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, tournament.getTitre());
            pstmt.setString(2, tournament.getLieu());
            pstmt.setString(3, tournament.getDateDebut().toString());
            pstmt.setString(4, tournament.getDateFin().toString());
            pstmt.setInt(5, tournament.getNbParticipantsMax());
            pstmt.setString(6, tournament.getStatus());
            pstmt.setDouble(7, tournament.getLatitude()); // Nouveau champ : latitude
            pstmt.setDouble(8, tournament.getLongitude()); // Nouveau champ : longitude
            pstmt.setInt(9, tournament.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du tournoi : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Supprime un tournoi par son ID.
     */
    public boolean deleteTournament(int id) {
        String query = "DELETE FROM tournaments WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du tournoi : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Récupère tous les tournois avec latitude et longitude.
     */
    public List<Tournament> getAllTournaments() {
        List<Tournament> tournaments = new ArrayList<>();
        String query = "SELECT * FROM tournaments";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String lieu = rs.getString("lieu");
                LocalDate dateDebut = LocalDate.parse(rs.getString("date_debut"));
                LocalDate dateFin = LocalDate.parse(rs.getString("date_fin"));
                int nbParticipantsMax = rs.getInt("nb_participants_max");
                String status = rs.getString("status");
                double latitude = rs.getDouble("latitude"); // Nouveau champ : latitude
                double longitude = rs.getDouble("longitude"); // Nouveau champ : longitude

                tournaments.add(new Tournament(id, titre, lieu, dateDebut, dateFin, nbParticipantsMax, status, latitude, longitude));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des tournois : " + e.getMessage());
            e.printStackTrace();
        }
        return tournaments;
    }

    /**
     * Inscrire un participant à un tournoi.
     */
    public boolean registerParticipant(int tournamentId, String firstName, String lastName, String phoneNumber) {
        String query = "INSERT INTO tournament_registrations (tournament_id, participant_first_name, participant_last_name, participant_phone_number) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tournamentId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, phoneNumber);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Participant inscrit : " + firstName + " " + lastName + " (" + phoneNumber + ")");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'inscription du participant : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Désinscrire un participant d'un tournoi.
     */
    public boolean unregisterParticipant(int tournamentId, String firstName, String lastName, String phoneNumber) {
        String query = "DELETE FROM tournament_registrations WHERE tournament_id = ? AND participant_first_name = ? AND participant_last_name = ? AND participant_phone_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tournamentId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, phoneNumber);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Participant désinscrit : " + firstName + " " + lastName + " (" + phoneNumber + ")");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la désinscription du participant : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Récupère les participants inscrits à un tournoi spécifique.
     */
    public List<Participant> getParticipantsForTournament(int tournamentId) {
        List<Participant> participants = new ArrayList<>();
        String query = "SELECT participant_first_name, participant_last_name, participant_phone_number FROM tournament_registrations WHERE tournament_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tournamentId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String firstName = rs.getString("participant_first_name");
                String lastName = rs.getString("participant_last_name");
                String phoneNumber = rs.getString("participant_phone_number");

                // Créer un objet Participant à partir des données récupérées
                participants.add(new Participant(firstName, lastName, phoneNumber));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des participants : " + e.getMessage());
            e.printStackTrace();
        }
        return participants;
    }
}