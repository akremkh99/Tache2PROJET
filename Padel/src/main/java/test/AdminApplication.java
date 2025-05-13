package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminApplication extends Application {

    /**
     * Méthode principale pour démarrer l'application JavaFX.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML de l'interface admin
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/Admin.fxml"));
        Parent root = loader.load();

        // Créer la scène avec la racine chargée depuis le fichier FXML
        Scene scene = new Scene(root);

        // Configurer la fenêtre principale (Stage)
        primaryStage.setTitle("Interface Admin - Padel Event");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1000);  // Largeur de la fenêtre
        primaryStage.setHeight(600); // Hauteur de la fenêtre
        primaryStage.show();
    }

    /**
     * Point d'entrée principal pour exécuter l'application.
     */
    public static void main(String[] args) {
        launch(args); // Démarrer l'application JavaFX
    }
}