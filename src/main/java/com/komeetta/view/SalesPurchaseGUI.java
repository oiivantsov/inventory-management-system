/**
 * SalesPurchaseGUI provides a modal form for creating either a sales or purchase order.
 * The form is localized and initialized based on the operation type.
 */
package com.komeetta.view;

import com.komeetta.controller.StockController;
import com.komeetta.util.LanguageUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class SalesPurchaseGUI {

    /**
     * Displays the stock management form for either a sale or a purchase.
     * Loads the StockForm.fxml and configures the controller with the operation type.
     *
     * @param isSale true if creating a sales order, false for purchase order
     */
    public static void display(boolean isSale) {
        try {
            // Create a new modal window
            Stage window = new Stage();
            window.setTitle(isSale ? "Make a sale" : "Make a purchase");
            window.initModality(Modality.APPLICATION_MODAL);

            // Load FXML and resource bundle for localization
            FXMLLoader loader = new FXMLLoader(
                    SalesPurchaseGUI.class.getResource("/Scenes/StockForm.fxml"),
                    ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale())
            );
            Parent newWindow = loader.load();

            // Pass operation type to the controller
            StockController stockController = loader.getController();
            stockController.setIsSale(isSale);

            // Display the scene
            Scene scene = new Scene(newWindow, 600, 400);
            window.setScene(scene);
            window.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load AddObjectForm.fxml. Check the file path.");
        }
    }
}