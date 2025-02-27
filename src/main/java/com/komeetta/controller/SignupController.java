package com.komeetta.controller;

import java.io.IOException;

import com.komeetta.dao.UserDAO;
import com.komeetta.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController {

    // Variables for view objects
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;
    
    // Variable for storing and sending user object to the next view
    private User user;

    // Called when the Signup button is clicked
    @FXML
    private void handleSignupAction(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate inputs if valid create user in database
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "Please fill in all fields.");
        } else if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "Passwords do not match.");
        } else {
        	UserDAO uDAO = new UserDAO();
        	user = new User(username, password);
        	if(!uDAO.isUsernameAvailable(username)){
        		showAlert(Alert.AlertType.INFORMATION, "Signup Failed", "Username taken.");
        	}else {
        		uDAO.addUser(user);
        		showAlert(Alert.AlertType.INFORMATION, "Signup Successful", "Welcome, " + firstName + " " + lastName + "!");
        		// Send to Dashboard here
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
        	}
            
        	
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
