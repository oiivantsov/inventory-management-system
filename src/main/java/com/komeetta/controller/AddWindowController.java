package com.komeetta.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;

public class AddWindowController {

    @FXML
    private Label headlineLabel;

    @FXML
    private TextField entityNameField;

    @FXML
    private TextField entityEmailField;

    @FXML
    private TextField entityPhoneField;

    @FXML
    private TextField entityAddressField;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void handleAdd(ActionEvent event) {
        String name = entityNameField.getText().trim();
        String email = entityEmailField.getText().trim();
        String phone = entityPhoneField.getText().trim();
        String address = entityAddressField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled before adding an entity.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (!phone.matches("^\\d{10,15}$")) {
            showAlert("Invalid Phone Number", "Phone number must be between 10-15 digits.");
            return;
        }

        showAlert("Success", "Entity added successfully!");
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        entityNameField.clear();
        entityEmailField.clear();
        entityPhoneField.clear();
        entityAddressField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

