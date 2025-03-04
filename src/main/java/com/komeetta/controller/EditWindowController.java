package com.komeetta.controller;

import com.komeetta.dao.CustomerDAO;
import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.SupplierDAO;
import com.komeetta.model.Customer;
import com.komeetta.model.Product;
import com.komeetta.model.Supplier;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditWindowController {

    @FXML
    private Label headlineLabel;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField quantityField;

    @FXML
    private VBox productVBox;

    @FXML
    private Button productEditButton;

    @FXML
    private Button productCancelButton;

    @FXML
    private TextField entityNameField;

    @FXML
    private TextField entityEmailField;

    @FXML
    private TextField entityPhoneField;

    @FXML
    private TextField entityAddressField;

    @FXML
    private Label entityHeadlineLabel;

    @FXML
    private VBox entityVBox;

    @FXML
    private StackPane formArea;

    private Product productToEdit = null;
    private Customer customerToEdit = null;
    private Supplier supplierToEdit = null;

    @FXML
    private void handleProductEdit(ActionEvent event) {
        String productName = productNameField.getText().trim();
        String category = categoryField.getText().trim();
        String brand = brandField.getText().trim();
        String description = descriptionField.getText().trim();
        int quantity = Integer.parseInt(quantityField.getText().trim());

        if (productName.isEmpty() || category.isEmpty() || brand.isEmpty() || description.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled before editing the product.");
            return;
        } else if (quantity < 0) {
            showAlert("Invalid Input", "Quantity must be a positive number.");
            return;
        }
        productToEdit.setBrand(brand);
        productToEdit.setCategory(category);
        productToEdit.setDescription(description);
        productToEdit.setName(productName);
        productToEdit.setQuantity(quantity);

        try {
            ProductDAO productDAO = new ProductDAO();
            productDAO.updateProduct(productToEdit);

            showAlert("Success", "Product details updated successfully!");
            Stage stage = (Stage) productVBox.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showAlert("Something went wrong", "Please try again later.");
        }

    }

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

        if (customerToEdit != null) {
            customerToEdit.setName(entityName);
            customerToEdit.setEmail(entityEmail);
            customerToEdit.setPhoneNumber(entityPhone);
            customerToEdit.setAddress(entityAddress);

            try{
                CustomerDAO customerDAO = new CustomerDAO();
                customerDAO.updateCustomer(customerToEdit);

                showAlert("Success", "Customer details updated successfully!");
                Stage stage = (Stage) entityVBox.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                showAlert("Something went wrong", "Please try again later.");
            }
        }else {
            supplierToEdit.setName(entityName);
            supplierToEdit.setEmail(entityEmail);
            supplierToEdit.setPhoneNumber(entityPhone);
            supplierToEdit.setAddress(entityAddress);

            try{
                SupplierDAO supplierDAO = new SupplierDAO();
                supplierDAO.updateSupplier(supplierToEdit);

                showAlert("Success", "Supplier details updated successfully!");
                Stage stage = (Stage) entityVBox.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                showAlert("Something went wrong", "Please try again later.");
            }
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) productEditButton.getScene().getWindow();
        stage.close();
    }

    public void showNeededForm(Object object) {
        if (object instanceof Product) {
            this.productToEdit = (Product) object;

            productNameField.setText(productToEdit.getName());
            categoryField.setText(productToEdit.getCategory());
            brandField.setText(productToEdit.getBrand());
            descriptionField.setText(productToEdit.getDescription());
            quantityField.setText(Integer.toString(productToEdit.getQuantity()));

            showView(productVBox);
        } else if (object instanceof Customer) {
            this.customerToEdit = (Customer) object;

            entityHeadlineLabel.setText("Alter customer details");

            entityNameField.setText(customerToEdit.getName());
            entityEmailField.setText(customerToEdit.getEmail());
            entityAddressField.setText(customerToEdit.getAddress());
            entityPhoneField.setText(customerToEdit.getPhoneNumber());

            showView(entityVBox);
        }else if (object instanceof Supplier) {
            this.supplierToEdit = (Supplier) object;

            entityHeadlineLabel.setText("Alter supplier details");

            entityNameField.setText(supplierToEdit.getName());
            entityEmailField.setText(supplierToEdit.getEmail());
            entityPhoneField.setText(supplierToEdit.getPhoneNumber());
            entityAddressField.setText(supplierToEdit.getAddress());

            showView(entityVBox);
        }
    }

    // Shows corresponding View to passed variable
    private void showView(VBox view) {
        if (formArea == null) {
            System.err.println("ERROR: formArea is NULL! Check fx:id in Dashboard.fxml.");
            return;
        }

        for (Node node : formArea.getChildren()) {
            node.setVisible(node == view);
            node.setManaged(node == view);
            if (view == productVBox) {

            } else if (view == entityVBox) {

            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
