package com.komeetta.controller;

import com.komeetta.dao.ProductDAO;
import com.komeetta.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProductController {
    private final ProductDAO dao = new ProductDAO();

    @FXML
    private TextField productNameField;

    @FXML
    private ComboBox supplierComboBox;

    @FXML
    private Button newSupplierButton;

    @FXML
    private ComboBox categoryCombobox;

    @FXML
    private TextField brandField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void initialize(){
        // nyt alkuun jotain preset, halutaanko jostain tietokannasta tms nää?
        categoryCombobox.getItems().addAll("Electronics", "Clothing", "Shoes", "Bags", "Accessories", "Books");

    }

    @FXML
    private void handleAddProduct(ActionEvent event){
        if (!validateForm()) {
            System.out.println("Form validation failed.");
            return;
        }

        System.out.println("Form validation passed.");
        boolean success = addProduct();

        if (success) {
            System.out.println("Product added successfully.");
            closeForm(event);
        } else {
            System.out.println("Failed to add product.");
        }
    }

    @FXML
    private void handleCancelProduct(ActionEvent event){
        System.out.println("Form cancelled.");
        System.out.println("Closing form.");
        closeForm(event);
    }

    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Please fill in the following information:\n"); // Initial message, 42 characters, !!!UPDATE IF CLAUSE BELOW IF CHANGED!!!

        if (productNameField.getText() == null || productNameField.getText().isEmpty()) {
            errorMessage.append("- Product Name\n");
        }
        if (categoryCombobox.getValue() == null) {
            errorMessage.append("- Category\n");
        }
        if (brandField.getText() == null || brandField.getText().isEmpty()) {
            errorMessage.append("- Brand\n");
        }
        if (quantityField.getText() == null || quantityField.getText().isEmpty()) {
            errorMessage.append("- Quantity\n");
        } else {
            try {
                Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException e) {
                errorMessage.append("Quantity must be a number.\n");
            }
        }

        // if the error message is longer than the initial message, show the alert
        if (errorMessage.length() > 42) { // 42 is the length of the initial message
            showAlert(errorMessage.toString());
            return false;
        }

        // if no errors, return true
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
        ((Button) event.getSource()).getScene().getWindow().hide();
    }

    private boolean addProduct() {
        try {
            Product product = new Product();
            product.setName(productNameField.getText());
            product.setCategory(categoryCombobox.getValue().toString());
            product.setBrand(brandField.getText());
            product.setStockQuantity(Integer.parseInt(quantityField.getText()));
            product.setDescription(descriptionArea.getText());

            dao.addProduct(product);
            return true;
        } catch (RuntimeException e) {
            showAlert("Failed to add product. Please try again.");
            return false;
        }
    }
}