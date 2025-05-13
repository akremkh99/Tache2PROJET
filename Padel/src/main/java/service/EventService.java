package service;

import com.example.padel.Event;
import utils.Myconnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventService {

    private Connection connection;

    public EventService() {
        connection = Myconnection.getConnection();
    }

    // Méthode pour ajouter un événement
    public boolean addEvent(Event event) {
        String query = "INSERT INTO evenements (titre, lieu, date_debut, date_fin, nb_places, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, event.getTitre());
            pstmt.setString(2, event.getLieu());
            pstmt.setDate(3, Date.valueOf(event.getDateDebut()));
            pstmt.setDate(4, Date.valueOf(event.getDateFin()));
            pstmt.setInt(5, event.getNbPlaces());
            pstmt.setString(6, event.getDescription());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        event.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
        return false;
    }

    // Méthode pour mettre à jour un événement
    public boolean updateEvent(Event event) {
        String query = "UPDATE evenements SET titre = ?, lieu = ?, date_debut = ?, date_fin = ?, nb_places = ?, description = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, event.getTitre());
            pstmt.setString(2, event.getLieu());
            pstmt.setDate(3, Date.valueOf(event.getDateDebut()));
            pstmt.setDate(4, Date.valueOf(event.getDateFin()));
            pstmt.setInt(5, event.getNbPlaces());
            pstmt.setString(6, event.getDescription());
            pstmt.setInt(7, event.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'événement : " + e.getMessage());
        }
        return false;
    }

    // Méthode pour récupérer tous les événements
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM evenements";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setTitre(rs.getString("titre"));
                event.setLieu(rs.getString("lieu"));
                event.setDateDebut(rs.getDate("date_debut").toLocalDate());
                event.setDateFin(rs.getDate("date_fin").toLocalDate());
                event.setNbPlaces(rs.getInt("nb_places"));
                event.setDescription(rs.getString("description"));

                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
        return events;
    }

    // Nouvelle méthode : Supprimer un événement
    public boolean deleteEvent(int eventId) {
        String query = "DELETE FROM evenements WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, eventId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Retourne true si au moins une ligne a été supprimée
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'événement : " + e.getMessage());
        }
        return false;
    }
}