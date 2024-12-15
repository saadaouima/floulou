package org.floulou.flouloushop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.Objects;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Charger l'image du logo depuis le dossier resources
        String logoPath = getClass().getResource("/images/back.jpg").toExternalForm();
        Image appLogo = new Image(logoPath); // Crée une instance d'Image avec le chemin de l'image

        // Définir l'icône de l'application
        primaryStage.getIcons().add(appLogo);

        primaryStage.setTitle("Floulou Shop");

        primaryStage.setMaximized(true);
        // Charger le fichier FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        // Créer une scène
        Scene scene = new Scene(root, screenWidth-100, screenHeight-100);


        // Add the CSS file
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());


        // Configurer la fenêtre principale
       // primaryStage.setTitle("Connexion");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
