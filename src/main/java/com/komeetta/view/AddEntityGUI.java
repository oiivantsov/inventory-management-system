/**
 * AddEntityGUI handles the creation and display of a modal window
 * used for adding new entities (Customer, Supplier, etc.) via a form.
 */
package com.komeetta.view;

import com.komeetta.util.LanguageUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * AddEntityGUI class provides a static method to display a modal form window
 * for adding a new entity (Customer, Supplier, etc.) in the JavaFX application.
 * It uses FXML to load the form layout and handles localization using ResourceBundle.
 */
public class AddEntityGUI {

    /**
     * Displays a modal form window for adding a new entity.
     * The method loads AddObjectForm.fxml with the correct language resource bundle,
     * creates a new modal stage, and blocks the main window until the form is closed.
     */
    public static void display() {
        try {
            // Create a new modal stage
            Stage window = new Stage();
            window.setTitle(LanguageUtil.getString("str_add_entity"));
            window.initModality(Modality.APPLICATION_MODAL);
            ResourceBundle bundle = ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale());
            // Load the FXML form using the current locale's resource bundle
            FXMLLoader loader = new FXMLLoader(
                    EditObjectGUI.class.getResource("/Scenes/AddObjectForm.fxml"), bundle);
            Parent newWindow = loader.load();

            // Set and show the scene
            Scene scene = new Scene(newWindow, 600, 400);
            window.setScene(scene);
            window.showAndWait(); // Blocks user interaction with other windows
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load AddObjectForm.fxml. Check the file path.");
        }
    }
} 