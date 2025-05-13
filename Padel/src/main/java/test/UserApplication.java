package test;

import controller.UserRegistrationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.TournamentService;

import java.io.IOException;

public class UserApplication extends Application {

    private TournamentService tournamentService;

    @Override
    public void start(Stage stage) throws IOException {
        // Initialiser le service
        tournamentService = new TournamentService();

        // Charger le fichier FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/padel/UserRegistrationView.fxml"));
        Scene scene = new Scene(loader.load());

        // Obtenir le contrôleur et définir le service
        UserRegistrationController controller = loader.getController();
        controller.setRegistrationService(tournamentService);

        // Configurer la scène
        stage.setTitle("Inscription aux Tournois");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}