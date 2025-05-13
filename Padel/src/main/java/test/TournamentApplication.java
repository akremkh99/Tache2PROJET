package test;

import service.TournamentService;
import controller.TournamentListController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class TournamentApplication extends Application {

    private TournamentService tournamentService;

    @Override
    public void start(Stage stage) throws IOException {
        // Initialiser le service
        tournamentService = new TournamentService();

        // Charger le fichier FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/TournamentListView.fxml"));
        Scene scene = new Scene(loader.load());

        // Obtenir le contrôleur et définir le service
        TournamentListController controller = loader.getController();
        controller.setTournamentService(tournamentService);

        // Configurer la scène
        stage.setTitle("Gestion des Tournois");
        stage.setScene(scene);

        // Obtenir les dimensions de l'écran
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Soustraire 2 cm (≈ 80 pixels) de chaque côté
        double windowWidth = screenWidth - 80; // 80 pixels de chaque côté
        double windowHeight = screenHeight - 80; // 80 pixels en haut et en bas

        // Définir la taille de la fenêtre avec les marges
        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);

        // Centrer la fenêtre sur l'écran
        stage.setX(60); // Décalage de 80 pixels depuis le bord gauche
        stage.setY(60); // Décalage de 80 pixels depuis le bord supérieur

        // Afficher la fenêtre
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}