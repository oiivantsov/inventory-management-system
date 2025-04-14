/**
 * EditWindowController manages the logic for editing Product, Customer, or Supplier objects.
 * It handles data validation, translation (for Product), and updates to the database.
 */
package com.komeetta.controller;

import com.komeetta.dao.CustomerDAO;
import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.SupplierDAO;
import com.komeetta.model.Customer;
import com.komeetta.model.Product;
import com.komeetta.model.Supplier;
import com.komeetta.service.TranslateUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;

public class EditWindowController {

    @FXML private Label headlineLabel;
    @FXML private TextField productNameField, categoryField, brandField, descriptionField, quantityField;
    @FXML private VBox productVBox;
    @FXML private Button productEditButton, productCancelButton;
    @FXML private TextField entityNameField, entityEmailField, entityPhoneField, entityAddressField;
    @FXML private Label entityHeadlineLabel;
    @FXML private VBox entityVBox;
    @FXML private StackPane formArea;

    private Product productToEdit;
    private Customer customerToEdit;
    private Supplier supplierToEdit;

    /**
     * Handles editing and saving a Product object, including field validation,
     * translation to multiple languages, and database update.
     */
    @FXML
    private void handleProductEdit(ActionEvent event) {
        String productName = productNameField.getText().trim();
        String category = categoryField.getText().trim();
        String brand = brandField.getText().trim();
        String description = descriptionField.getText().trim();
        int quantity;

        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Quantity must be a valid number.");
            return;
        }

        if (productName.isEmpty() || category.isEmpty() || brand.isEmpty() || description.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled before editing the product.");
            return;
        } else if (quantity < 0) {
            showAlert("Invalid Input", "Quantity must be a positive number.");
            return;
        }

        productToEdit.setName(productName);
        productToEdit.setCategory(category);
        productToEdit.setDescription(description);
        productToEdit.setBrand(brand);
        productToEdit.setQuantity(quantity);

        CompletableFuture<String> nameFi = TranslateUtil.translate(productName, "fi");
        CompletableFuture<String> nameRu = TranslateUtil.translate(productName, "ru");
        CompletableFuture<String> nameJa = TranslateUtil.translate(productName, "ja");

        CompletableFuture<String> descFi = TranslateUtil.translate(description, "fi");
        CompletableFuture<String> descRu = TranslateUtil.translate(description, "ru");
        CompletableFuture<String> descJa = TranslateUtil.translate(description, "ja");

        CompletableFuture<String> catFi = TranslateUtil.translate(category, "fi");
        CompletableFuture<String> catRu = TranslateUtil.translate(category, "ru");
        CompletableFuture<String> catJa = TranslateUtil.translate(category, "ja");

        CompletableFuture.allOf(nameFi, nameRu, nameJa, descFi, descRu, descJa, catFi, catRu, catJa)
                .thenRun(() -> {
                    try {
                        productToEdit.setNameFi(nameFi.get());
                        productToEdit.setNameRu(nameRu.get());
                        productToEdit.setNameJa(nameJa.get());
                        productToEdit.setDescriptionFi(descFi.get());
                        productToEdit.setDescriptionRu(descRu.get());
                        productToEdit.setDescriptionJa(descJa.get());
                        productToEdit.setCategoryFi(catFi.get());
                        productToEdit.setCategoryRu(catRu.get());
                        productToEdit.setCategoryJa(catJa.get());

                        new ProductDAO().updateProduct(productToEdit);

                        javafx.application.Platform.runLater(() -> {
                            showAlert("Success", "Product updated and translated!");
                            Stage stage = (Stage) productVBox.getScene().getWindow();
                            stage.close();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        javafx.application.Platform.runLater(() ->
                                showAlert("Error", "Failed to update product. Please try again.")
                        );
                    }
                });
    }

    /**
     * Handles editing a Customer or Supplier, performs validation and updates the database.
     */
    public void handleEntityEdit(ActionEvent event) {
        String entityName = entityNameField.getText().trim();
        String entityEmail = entityEmailField.getText().trim();
        String entityPhone = entityPhoneField.getText().trim();
        String entityAddress = entityAddressField.getText().trim();

        if (entityName.isEmpty() || entityEmail.isEmpty() || entityPhone.isEmpty() || entityAddress.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled before continuing.");
        }

        if (!entityPhone.matches("^\\d{10,15}$")) {
            showAlert("Invalid Phone Number", "Phone number must be between 10-15 digits.");
            return;
        }

        try {
            if (customerToEdit != null) {
                customerToEdit.setName(entityName);
                customerToEdit.setEmail(entityEmail);
                customerToEdit.setPhoneNumber(entityPhone);
                customerToEdit.setAddress(entityAddress);
                new CustomerDAO().updateCustomer(customerToEdit);
                showAlert("Success", "Customer details updated successfully!");
            } else {
                supplierToEdit.setName(entityName);
                supplierToEdit.setEmail(entityEmail);
                supplierToEdit.setPhoneNumber(entityPhone);
                supplierToEdit.setAddress(entityAddress);
                new SupplierDAO().updateSupplier(supplierToEdit);
                showAlert("Success", "Supplier details updated successfully!");
            }
            Stage stage = (Stage) entityVBox.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showAlert("Something went wrong", "Please try again later.");
        }
    }

    /**
     * Cancels the edit and closes the current window.
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) productEditButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays the appropriate form based on the type of object (Product, Customer, Supplier).
     * @param object The object to be edited
     */
    public void showNeededForm(Object object) {
        if (object instanceof Product product) {
            this.productToEdit = product;
            productNameField.setText(product.getName());
            categoryField.setText(product.getCategory());
            brandField.setText(product.getBrand());
            descriptionField.setText(product.getDescription());
            quantityField.setText(Integer.toString(product.getQuantity()));
            showView(productVBox);
        } else if (object instanceof Customer customer) {
            this.customerToEdit = customer;
            entityHeadlineLabel.setText("Alter customer details");
            entityNameField.setText(customer.getName());
            entityEmailField.setText(customer.getEmail());
            entityAddressField.setText(customer.getAddress());
            entityPhoneField.setText(customer.getPhoneNumber());
            showView(entityVBox);
        } else if (object instanceof Supplier supplier) {
            this.supplierToEdit = supplier;
            entityHeadlineLabel.setText("Alter supplier details");
            entityNameField.setText(supplier.getName());
            entityEmailField.setText(supplier.getEmail());
            entityPhoneField.setText(supplier.getPhoneNumber());
            entityAddressField.setText(supplier.getAddress());
            showView(entityVBox);
        }
    }

    /**
     * Displays the given VBox form and hides others.
     */
    private void showView(VBox view) {
        if (formArea == null) {
            System.err.println("ERROR: formArea is NULL! Check fx:id in Dashboard.fxml.");
            return;
        }

        for (Node node : formArea.getChildren()) {
            node.setVisible(node == view);
            node.setManaged(node == view);
        }
    }

    /**
     * Utility method to show an informational alert dialog.
     * @param title The title of the alert
     * @param message The message to display
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}