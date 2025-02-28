package com.komeetta.view;

import com.komeetta.controller.StockController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SalesPurchaseGUI extends Application {

    private boolean isSaleTest = false; // for testing, then should be moved to dashboard controller

    @Override
    public void start(Stage primaryStage) throws Exception {

        // sales or purchase
        StockController.setIsSale(isSaleTest);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/StockForm.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle(isSaleTest ? "Make a sale" : "Make a purchase");
        primaryStage.show();
    }
}
