package com.komeetta.controller;

import com.komeetta.view.AddEntityGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import com.komeetta.view.EditProductGUI;

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
    private Button buttonEdit;

    @FXML
    private Button buttonAdd;

    @FXML
    public void initialize() {
        // Ensure only one view is visible at a time
        showView(HomeVBox);

        // Debugging: Check if buttonEdit is null
        if (buttonEdit == null) {
            System.err.println("ERROR: buttonEdit is NULL! Check fx:id in Dashboard.fxml.");
        } else {
            buttonEdit.setOnAction(event -> handleEditButtonAction());
        }

        // Debugging: Check if contentArea is null
        if (contentArea == null) {
            System.err.println("ERROR: contentArea is NULL! Check fx:id in Dashboard.fxml.");
        }
    }

    @FXML
    private void handleEditButtonAction() {
        if (productVBox.isVisible()) { // If the product view is visible, edit the selected product
            // method for the editing of the product
        } else if (customerVBox.isVisible() || SupplierVBox.isVisible()) { // If the customer or supplier view is visible, edit the selected entity
            // method for the editing of the entity
        }
    }

    @FXML
    private void handleAddButtonAction() {
        if (productVBox.isVisible()) { // If the product view is visible, add a new product
            EditProductGUI.display();
        } else if (customerVBox.isVisible() || SupplierVBox.isVisible()) { // If the customer or supplier view is visible, add a new entity
            AddEntityGUI.display();
        }
    }

    @FXML
    private void handleProductButtonAction() {
        showView(productVBox);
    }

    @FXML
    private void handleCustomerButtonAction() {
        EntityController.setIsCustomer(true); // Set the entity type to Customer
        showView(customerVBox);
    }

    @FXML
    private void handleSupplierButtonAction() {
        EntityController.setIsCustomer(false); // Set the entity type to Supplier
        showView(SupplierVBox);
    }

    /* Currently unused
    @FXML
    private void handleHomeButtonAction() {
        showView(HomeVBox);
    }
    */

    // Shows corresponding View to passed variable
    private void showView(VBox view) {
        if (contentArea == null) {
            System.err.println("ERROR: contentArea is NULL! Check fx:id in Dashboard.fxml.");
            return;
        }

        for (Node node : contentArea.getChildren()) {
            node.setVisible(node == view);
            node.setManaged(node == view);
        }
    }
}
