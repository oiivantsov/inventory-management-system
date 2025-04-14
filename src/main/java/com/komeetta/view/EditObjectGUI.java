/**
 * EditObjectGUI handles the creation and display of a modal window
 * for editing different types of entities (Customer, Supplier, Product).
 * It dynamically injects the target object into the form controller.
 */
package com.komeetta.view;

import com.komeetta.controller.EditWindowController;
import com.komeetta.util.LanguageUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class EditObjectGUI {

    /**
     * Displays the EditObjectForm in a modal window with the provided entity object.
     * Initializes the controller and populates the form with the entity data.
     *
     * @param objectToEdit the entity object to be edited (Customer, Supplier, or Product)
     */
    public static void display(Object objectToEdit) {
        try {
            // Create a new modal stage
            Stage window = new Stage();
            window.setTitle(LanguageUtil.getString("str_edit_entity"));
            window.initModality(Modality.APPLICATION_MODAL);

            // Load the FXML layout with localized resources
            FXMLLoader loader = new FXMLLoader(
                    EditObjectGUI.class.getResource("/Scenes/EditObjectForm.fxml"),
                    ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale())
            );
            Parent newWindow = loader.load();

            // Inject the target object into the controller
            EditWindowController controller = loader.getController();
            controller.showNeededForm(objectToEdit);

            // Set and show the window
            Scene scene = new Scene(newWindow, 600, 400);
            window.setScene(scene);
            window.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load EditObjectForm.fxml. Check the file path.");
        }
    }
}
