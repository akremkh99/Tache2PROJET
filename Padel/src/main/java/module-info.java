module com.example.padel {
    // Dépendances nécessaires
    requires javafx.controls;       // Pour JavaFX UI Controls
    requires javafx.fxml;           // Pour le support FXML
    requires java.sql;              // Pour l'accès aux bases de données via JDBC
    requires mysql.connector.j;
    requires java.desktop;     // Pour MySQL Connector/J
    requires javafx.web;
    requires jdk.jsobject;
    requires twilio;

    // Exporte les packages pour rendre leurs classes accessibles à d'autres modules
    exports com.example.padel;      // Exporte le package principal
    exports controller;             // Exporte le pacskage des contrôleurs

    // Ouvre les packages pour autoriser l'accès par réflexion (nécessaire pour FXML)
    opens com.example.padel to javafx.fxml;
    opens controller to javafx.fxml;

    // Si vous avez un package "test" contenant MainApplication, ajoutez ceci :
    exports test;
    opens test to javafx.fxml;
}