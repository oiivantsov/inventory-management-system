package com.komeetta.view;

import com.komeetta.controller.EditWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EditObjectGUI {
    public static void display(Object objectToEdit) {
        try {
            Stage window = new Stage();
            window.setTitle("Edit Entity");

            // Set modality to block interactions with other windows
            window.initModality(Modality.APPLICATION_MODAL);

            // Ensure a NEW instance of EditObjectForm.fxml is loaded
            FXMLLoader loader = new FXMLLoader(EditObjectGUI.class.getResource("/Scenes/EditObjectForm.fxml"));
            Parent newWindow = loader.load();

            EditWindowController controller = loader.getController();
            controller.showNeededForm(objectToEdit);

            Scene scene = new Scene(newWindow, 600, 400);
            window.setScene(scene);

            // Show window and wait (blocking interaction with other windows)
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load EditObjectForm.fxml. Check the file path.");
        }
    }
}
