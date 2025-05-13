package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.EventService;
import utils.Myconnection;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialisation de la connexion à la base de données
            Myconnection.getConnection();
            System.out.println("Connexion à la base de données établie avec succès.");

            // Chargement du fichier FXML principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/EventListView.fxml"));
            Parent root = loader.load();

            // Récupération du contrôleur et injection des dépendances
            Object controller = loader.getController();
            if (controller instanceof controller.EventListController) {
                ((controller.EventListController) controller).setEventService(new EventService());
            } else {
                throw new RuntimeException("Le contrôleur n'est pas une instance de EventListController.");
            }

            // Configuration de la scène principale
            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestion des Événements");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage de l'application : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Fermeture de la connexion à la base de données lors de l'arrêt de l'application
        Myconnection.closeConnection();
        System.out.println("Connexion à la base de données fermée.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}