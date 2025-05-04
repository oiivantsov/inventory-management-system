/**
 * MassImportGUI provides a modal interface for importing product data in bulk.
 * It loads a localized FXML layout and blocks the main window until the user closes it.
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
 * MassImportGUI class is responsible for displaying a modal window
 * for mass importing products in the application. It uses JavaFX to create the GUI
 * and loads the FXML layout with localization support.
 */
public class MassImportGUI {

    /**
     * Displays a modal window for mass importing products.
     * Loads MassImportForm.fxml with localization support based on the current locale.
     */
    public static void display() {
        try {
            // Create a new modal window
            Stage window = new Stage();
            window.setTitle(LanguageUtil.getString("str_mass_import"));
            window.initModality(Modality.APPLICATION_MODAL);

            // Load the FXML with localized bundle
            FXMLLoader loader = new FXMLLoader(
                    EditObjectGUI.class.getResource("/Scenes/MassImportForm.fxml"),
                    ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale())
            );
            Parent newWindow = loader.load();

            // Set and show the scene
            Scene scene = new Scene(newWindow, 400, 300);
            window.setScene(scene);
            window.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load MassImportForm.fxml. Check the file path.");
        }
    }
}