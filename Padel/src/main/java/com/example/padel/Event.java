package com.example.padel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Event {

    private final IntegerProperty id;
    private final StringProperty titre;
    private final StringProperty lieu;
    private final ObjectProperty<LocalDate> dateDebut;
    private final ObjectProperty<LocalDate> dateFin;
    private final IntegerProperty nbPlaces;

    // Nouveaux champs : description et participants
    private final StringProperty description;
    private final ObservableList<String> participants;

    // Constructeur par défaut
    public Event() {
        this.id = new SimpleIntegerProperty();
        this.titre = new SimpleStringProperty();
        this.lieu = new SimpleStringProperty();
        this.dateDebut = new SimpleObjectProperty<>();
        this.dateFin = new SimpleObjectProperty<>();
        this.nbPlaces = new SimpleIntegerProperty();

        // Initialisation des nouveaux champs
        this.description = new SimpleStringProperty();
        this.participants = FXCollections.observableArrayList();
    }

    // Getters et setters pour chaque propriété

    // ID
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Titre
    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    public StringProperty titreProperty() {
        return titre;
    }

    // Lieu
    public String getLieu() {
        return lieu.get();
    }

    public void setLieu(String lieu) {
        this.lieu.set(lieu);
    }

    public StringProperty lieuProperty() {
        return lieu;
    }

    // Date de début
    public LocalDate getDateDebut() {
        return dateDebut.get();
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut.set(dateDebut);
    }

    public ObjectProperty<LocalDate> dateDebutProperty() {
        return dateDebut;
    }

    // Date de fin
    public LocalDate getDateFin() {
        return dateFin.get();
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin.set(dateFin);
    }

    public ObjectProperty<LocalDate> dateFinProperty() {
        return dateFin;
    }

    // Nombre de places
    public int getNbPlaces() {
        return nbPlaces.get();
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces.set(nbPlaces);
    }

    public IntegerProperty nbPlacesProperty() {
        return nbPlaces;
    }

    // Description
    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    // Participants
    public ObservableList<String> getParticipants() {
        return participants;
    }

    public void addParticipant(String participant) {
        if (!participants.contains(participant)) {
            participants.add(participant);
        }
    }

    public void removeParticipant(String participant) {
        participants.remove(participant);
    }
}