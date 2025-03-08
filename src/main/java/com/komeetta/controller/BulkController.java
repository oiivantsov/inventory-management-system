package com.komeetta.controller;

import com.komeetta.dao.ProductDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Controller for the Product Bulk Upload form
 */
public class BulkController {
    /**
     * Data Access Object for Product
     */
    ProductDAO dao;

    @FXML
    private TextField filePathField;

    @FXML
    private Button browseFilesBtn;

    @FXML
    private Button uploadFilesBtn;

    /**
     * Initializes the form
     * Sets up the event handlers for the buttons
     * Browse Files button opens a file chooser dialog
     * Upload Files button uploads the selected file
     * dao is initialized with a new ProductDAO object
     */
    // Initialize the form
    @FXML
    private void initialize() {
        browseFilesBtn.setOnAction(event -> browseFile());
        uploadFilesBtn.setOnAction(event -> uploadFile());
        dao = new ProductDAO();
    }

    /**
     * Opens a file chooser dialog
     * Sets the selected file path to the filePathField
     */
    private void browseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
        }
    }

    /**
     * Uploads the selected file
     * Calls ProductDAO.bulkUploadFromCsv() to bulk upload products from the selected file
     * Closes the form after upload
     * Displays an alert if no file is selected
     * @see ProductDAO#bulkUploadFromCsv(String)
     */
    private void uploadFile() {
        String filePath = filePathField.getText();

        if (filePath.isEmpty()) {
            alert("Please select a file to upload.");
            return;
        }

        System.out.println("Bulk uploading products from File: " + filePath);
        ProductDAO.bulkUploadFromCsv(filePath);
        filePathField.clear(); // Clear the file path field
        closeForm();
    }

    /**
     * Closes the form
     * Called after the file is uploaded
     */
    // Close the form after upload
    private void closeForm() {
        Stage stage = (Stage) uploadFilesBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an alert message
     * @param message The message to display
     */
    // Display an alert message
    private void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
