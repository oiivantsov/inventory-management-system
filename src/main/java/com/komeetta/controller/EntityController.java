package com.komeetta.controller;

import com.komeetta.dao.CustomerDAO;
import com.komeetta.dao.SupplierDAO;
import com.komeetta.model.Customer;
import com.komeetta.model.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EntityController {
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private static boolean isCustomer; // true if customer, false if supplier

    protected static void setIsCustomer(boolean isCustomer) {
        EntityController.isCustomer = isCustomer;
    }

    @FXML
    private Label headlineLabel;

    @FXML
    private TextField entityNameField;

    @FXML
    private TextField entityEmailField;

    @FXML
    private TextField entityPhoneField;

    @FXML
    private TextField entityAddressField;

    @FXML
    private void initialize(){
        if (isCustomer) {
            headlineLabel.setText("Add New Customer");
            System.out.println("Adding a new customer.");
        } else {
            headlineLabel.setText("Add New Supplier");
            System.out.println("Adding a new supplier.");
        }
    }

    @FXML
    private void handleAddEntity(ActionEvent event) {
        if (!validateForm()) { // If the form is not valid, show an error message and return
            System.out.println("Form validation failed.");
            return;
        }

        System.out.println("Form validation passed.");
        boolean success = isCustomer ? addCustomer() : addSupplier(); // Add the entity to the database

        if (success) { // If the entity was added successfully, show a success message and close the form
            System.out.println(isCustomer ? "Customer added successfully." : "Supplier added successfully.");
            closeForm(event);
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        System.out.println("Form cancelled.");
        System.out.println("Closing form.");
        closeForm(event);
    }

    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Please fill in the following information:\n"); // Initial message, 42 characters

        if (entityNameField.getText().isEmpty() || entityNameField.getText() == null) {
            errorMessage.append("- Name\n");
        }
        if (entityEmailField.getText().isEmpty() || entityEmailField.getText() == null) {
            errorMessage.append("- Email\n");
        }
        if (entityPhoneField.getText().isEmpty() || entityPhoneField.getText() == null) {
            errorMessage.append("- Phone\n");
        }
        if (entityAddressField.getText().isEmpty() || entityAddressField.getText() == null) {
            errorMessage.append("- Address\n");
        }
        if (errorMessage.length() > 42) { // 42 is the length of the initial message
            showAlert(errorMessage.toString());
            return false;
        }
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Form Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeForm(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Add a new customer to the database
    private boolean addCustomer(){
        try {
            Customer customer = new Customer();
            customer.setName(entityNameField.getText());
            customer.setEmail(entityEmailField.getText());
            customer.setPhoneNumber(entityPhoneField.getText());
            customer.setAddress(entityAddressField.getText());

            customerDAO.addCustomer(customer);
            return true;
        } catch (RuntimeException e) {
            showAlert("Failed to add customer. Please try again.");
            return false;
        }
    }

    // Add a new supplier to the database
    private boolean addSupplier(){
        try{
            Supplier supplier = new Supplier();
            supplier.setName(entityNameField.getText());
            supplier.setEmail(entityEmailField.getText());
            supplier.setPhoneNumber(entityPhoneField.getText());
            supplier.setAddress(entityAddressField.getText());

            supplierDAO.addSupplier(supplier);
            return true;
        } catch (RuntimeException e) {
            showAlert("Failed to add supplier. Please try again.");
            return false;
        }
    }
}
