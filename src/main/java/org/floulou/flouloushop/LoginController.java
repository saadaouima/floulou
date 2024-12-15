package org.floulou.flouloushop;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.floulou.flouloushop.model.services.UserService;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleCreateAccount(ActionEvent event) throws IOException {
        // Load the RegisterPage.fxml scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterPage.fxml"));
        VBox registerPage = loader.load();  // Load the RegisterPage.fxml (use the appropriate layout type)

        // Get the current stage and set the new scene (Register page)
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Scene registerScene = new Scene(registerPage);
        stage.setScene(registerScene);
        stage.show();
    }

    @FXML
    private ImageView logoImage;  // Reference to the ImageView from FXML

    public void initialize() {
        // Load the image from the resources folder
        String imagePath = getClass().getResource("/images/back2.png").toExternalForm();  // Image path in the resources folder
        Image logo = new Image(imagePath);  // Create an Image object
        logoImage.setImage(logo);  // Set the image to the ImageView
    }

    // Method to handle login button click
    public void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if the username and password match
        if (isValidUser(username, password)) {
            if (username.equals("admin") && password.equals("admin")) {
                // If the user is an administrator
                loadAdminDashboard(event);
            } else {
                // Load the HomePage scene if the login is successful
                //loadHomePage(event);
                // Load the HomePage scene if the login is successful
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
                GridPane homePage = loader.load();  // Load the HomePage.fxml as StackPane

                double screenWidth = Screen.getPrimary().getBounds().getWidth();
                double screenHeight = Screen.getPrimary().getBounds().getHeight();
                // Create a new scene and set it on the current stage
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene homeScene = new Scene(homePage,screenWidth,screenHeight);
                stage.setScene(homeScene);
                stage.setMaximized(true);
                stage.show();
            }

        } else {
            // Show error message if login fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }

    // Dummy method to validate user (replace with actual validation logic)
    private boolean isValidUser(String username, String password) {
        UserService userService = new UserService();
        return userService.findUser(username, password) != null;
    }

    // Load Admin Dashboard (Product Management)
    private void loadAdminDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
        GridPane adminDashboard = loader.load();

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setMaximized(true);
        Scene adminScene = new Scene(adminDashboard,screenWidth,screenHeight);
        stage.setScene(adminScene);
        adminScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        stage.show();
    }
}

