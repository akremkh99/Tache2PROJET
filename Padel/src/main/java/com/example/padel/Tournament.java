package com.example.padel;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Tournament {
    private final IntegerProperty id;
    private final StringProperty titre;
    private final StringProperty lieu;
    private final ObjectProperty<LocalDate> dateDebut; // Utilisation de LocalDate
    private final ObjectProperty<LocalDate> dateFin;   // Utilisation de LocalDate
    private final IntegerProperty nbParticipantsMax;
    private final StringProperty status;
    private final DoubleProperty latitude;  // Nouvelle propriété pour la latitude
    private final DoubleProperty longitude; // Nouvelle propriété pour la longitude

    public Tournament(int id, String titre, String lieu, LocalDate dateDebut, LocalDate dateFin, int nbParticipantsMax, String status, double latitude, double longitude) {
        this.id = new SimpleIntegerProperty(id);
        this.titre = new SimpleStringProperty(titre);
        this.lieu = new SimpleStringProperty(lieu);
        this.dateDebut = new SimpleObjectProperty<>(dateDebut);
        this.dateFin = new SimpleObjectProperty<>(dateFin);
        this.nbParticipantsMax = new SimpleIntegerProperty(nbParticipantsMax);
        this.status = new SimpleStringProperty(status);
        this.latitude = new SimpleDoubleProperty(latitude);  // Initialisation de la latitude
        this.longitude = new SimpleDoubleProperty(longitude); // Initialisation de la longitude
    }

    // Getters and Setters pour ID
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Getters and Setters pour Titre
    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    public StringProperty titreProperty() {
        return titre;
    }

    // Getters and Setters pour Lieu
    public String getLieu() {
        return lieu.get();
    }

    public void setLieu(String lieu) {
        this.lieu.set(lieu);
    }

    public StringProperty lieuProperty() {
        return lieu;
    }

    // Getters and Setters pour Date de Début
    public LocalDate getDateDebut() {
        return dateDebut.get();
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut.set(dateDebut);
    }

    public ObjectProperty<LocalDate> dateDebutProperty() {
        return dateDebut;
    }

    // Getters and Setters pour Date de Fin
    public LocalDate getDateFin() {
        return dateFin.get();
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin.set(dateFin);
    }

    public ObjectProperty<LocalDate> dateFinProperty() {
        return dateFin;
    }

    // Getters and Setters pour Nombre Maximum de Participants
    public int getNbParticipantsMax() {
        return nbParticipantsMax.get();
    }

    public void setNbParticipantsMax(int nbParticipantsMax) {
        this.nbParticipantsMax.set(nbParticipantsMax);
    }

    public IntegerProperty nbParticipantsMaxProperty() {
        return nbParticipantsMax;
    }

    // Getters and Setters pour Statut
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    // Getters and Setters pour Latitude
    public double getLatitude() {
        return latitude.get();
    }

    public void setLatitude(double latitude) {
        this.latitude.set(latitude);
    }

    public DoubleProperty latitudeProperty() {
        return latitude;
    }

    // Getters and Setters pour Longitude
    public double getLongitude() {
        return longitude.get();
    }

    public void setLongitude(double longitude) {
        this.longitude.set(longitude);
    }

    public DoubleProperty longitudeProperty() {
        return longitude;
    }
}