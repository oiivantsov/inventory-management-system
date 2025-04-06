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
    /*
    private boolean isSale; // for testing, then should be moved to dashboard controller

    @Override
    public void start(Stage primaryStage) throws Exception {

        // sales or purchase
        StockController.setIsSale(isSale);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/StockForm.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle(isSale ? "Make a sale" : "Make a purchase");
        primaryStage.show();
    }

     */


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
