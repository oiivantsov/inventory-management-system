package com.komeetta.view;

import com.komeetta.controller.StockController;
import com.komeetta.model.LanguageUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class SalesPurchaseGUI {

    public static void display(boolean isSale) {
        try {
            Stage window = new Stage();
            window.setTitle(isSale ? "Make a sale" : "Make a purchase");

            // Set modality to block interactions with other windows
            window.initModality(Modality.APPLICATION_MODAL);

            // Ensure a NEW instance of AddObjectForm.fxml is loaded
            FXMLLoader loader = new FXMLLoader(SalesPurchaseGUI.class.getResource("/Scenes/StockForm.fxml"), ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale()));
            Parent newWindow = loader.load();

            StockController stockController = loader.getController();
            stockController.setIsSale(isSale);

            Scene scene = new Scene(newWindow, 600, 400);
            window.setScene(scene);

            // Show window and wait (blocking interaction with other windows)
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load AddObjectForm.fxml. Check the file path.");
        }

    }
}
