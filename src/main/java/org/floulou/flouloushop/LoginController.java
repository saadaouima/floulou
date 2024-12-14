package org.floulou.flouloushop;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.floulou.flouloushop.model.services.UserService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Method to handle login button click
    public void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if the username and password match
        if (isValidUser(username, password)) {
            // Load the HomePage scene if the login is successful
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
            GridPane homePage = loader.load();  // Load the HomePage.fxml as StackPane

            // Create a new scene and set it on the current stage
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene homeScene = new Scene(homePage);
            stage.setScene(homeScene);
            stage.show();
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
}

