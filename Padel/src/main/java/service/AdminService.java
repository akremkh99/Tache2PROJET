package service;

import com.example.padel.Event;
import com.example.padel.Participant;
import com.example.padel.Tournament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class AdminService {

    private final TournamentService tournamentService = new TournamentService();
    private final EventService eventService = new EventService();
    private final RegistrationService registrationService = new RegistrationService();

    /**
     * Récupère tous les tournois depuis la base de données.
     */
    public ObservableList<Tournament> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return FXCollections.observableArrayList(tournaments);
    }

    /**
     * Récupère tous les événements depuis la base de données.
     */
    public ObservableList<Event> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return FXCollections.observableArrayList(events);
    }

    /**
     * Récupère tous les participants inscrits pour un tournoi spécifique.
     */
    public ObservableList<Participant> getRegisteredParticipants(int tournamentId) {
        List<Participant> participants = registrationService.getRegisteredParticipants(tournamentId);
        return FXCollections.observableArrayList(participants);
    }

    /**
     * Supprime un tournoi par son ID.
     */
    public boolean deleteTournament(int tournamentId) {
        return tournamentService.deleteTournament(tournamentId);
    }

    /**
     * Modifie un tournoi existant.
     */
    public boolean updateTournament(Tournament updatedTournament) {
        return tournamentService.updateTournament(updatedTournament);
    }

    /**
     * Supprime un événement par son ID.
     */
    public boolean deleteEvent(int eventId) {
        return eventService.deleteEvent(eventId);
    }

    /**
     * Modifie un événement existant.
     */
    public boolean updateEvent(Event updatedEvent) {
        return eventService.updateEvent(updatedEvent);
    }

    /**
     * Supprime un participant par son numéro de téléphone.
     */
    public boolean deleteParticipant(String phoneNumber) {
        return registrationService.deleteParticipant(phoneNumber);
    }

    /**
     * Modifie un participant existant.
     */
    public boolean updateParticipant(Participant updatedParticipant) {
        return registrationService.updateParticipant(updatedParticipant);
    }
}