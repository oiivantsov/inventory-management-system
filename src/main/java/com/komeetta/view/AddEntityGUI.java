package com.komeetta.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AddEntityGUI {
    public static void display() {
        try {
            Stage window = new Stage();
            window.setTitle("Add Entity");

            // Set modality to block interactions with other windows
            window.initModality(Modality.APPLICATION_MODAL);

            // Ensure a NEW instance of ProductForm.fxml is loaded
            FXMLLoader loader = new FXMLLoader(EditProductGUI.class.getResource("/Scenes/EntityForm.fxml"));
            Parent newWindow = loader.load();

            Scene scene = new Scene(newWindow, 600, 400);
            window.setScene(scene);

            // Show window and wait (blocking interaction with other windows)
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load EntityForm.fxml. Check the file path.");
        }
    }
}
