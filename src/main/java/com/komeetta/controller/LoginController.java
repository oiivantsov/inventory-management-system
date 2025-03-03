package com.komeetta.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import com.komeetta.dao.UserDAO;
import com.komeetta.model.User;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;
    
    private User user;

    //Called when user clicks login
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        // Get input from the text fields
        String username = usernameField.getText();
        String password = passwordField.getText();

        
        UserDAO uDAO = new UserDAO();
        if(uDAO.authenticate(username, password)) {
        	System.out.println("login sucsessful");
        	user = uDAO.getUser(username);
        	//Send to Dashboard here
            try {
                // Load the new FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Dashboard.fxml"));
                Parent root = loader.load();

                // Send User variable to the next view
                DashboardController controller = loader.getController();
                controller.setInitialView(user);

                // Get the current stage (window)
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Set the new scene
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Dashboard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
        	System.out.println("Wrong credentials");
        	showAlert(Alert.AlertType.INFORMATION, "Sign in failed", "Wrong credentials.");
        }
    }
    
    // Called when user clicks create new user
    @FXML
    private void handleSignupButtonAction(ActionEvent event) {
        System.out.println("Redirecting to the registration page...");
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Signup.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Register");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Used to show user alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}