package org.floulou.flouloushop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.floulou.flouloushop.model.services.UserService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    // Method to handle registration button click
    public void handleRegister(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (password.equals(confirmPassword)) {
            // Register the user (add your registration logic here)
            UserService userService = new UserService();
            boolean isRegistred = userService.registerUser(username, password);
            // Show a success message
            if (isRegistred) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.setContentText("Your account has been created successfully.");
                alert.showAndWait();
                handleLoginRedirect(event);
            } else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Failed");
                alert.setHeaderText(null);
                alert.setContentText("Your account already exists!");
                alert.showAndWait();
            }
        } else {
            // Show an error if passwords don't match
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Failed");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLoginRedirect(ActionEvent event) throws IOException {
        // Load the Login.fxml scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        GridPane loginPage = loader.load();  // Load the Login.fxml (which uses GridPane)

        // Get the current stage and set the new scene (Login page)
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Scene loginScene = new Scene(loginPage, 300, 200);  // Use GridPane as the root layout

        // Add the CSS file
        loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        stage.setScene(loginScene);
        stage.show();
    }


}
