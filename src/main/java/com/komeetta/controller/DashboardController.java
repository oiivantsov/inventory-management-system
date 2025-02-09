package com.komeetta.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private VBox productVBox;

    @FXML
    private VBox customerVBox;

    @FXML
    private VBox SupplierVBox;

    @FXML
    private VBox HomeVBox;

    @FXML
    public void initialize() {
        // Ensure only one view is visible at a time
        showView(HomeVBox);
    }

    @FXML
    private void handleProductButtonAction() {
        showView(productVBox);
    }

    @FXML
    private void handleCustomerButtonAction() {
        showView(customerVBox);
    }

    @FXML
    private void handleSupplierButtonAction() {
        showView(SupplierVBox);
    }

    /* Currently unused
    @FXML
    private void handleHomeButtonAction() {
        showView(HomeVBox);
    }
    */

    //Shows corresponding View to passed variable
    private void showView(VBox view) {
        for (Node node : contentArea.getChildren()) {
            node.setVisible(node == view);
            node.setManaged(node == view);
        }
    }
}

