/**
 * StockController manages inventory-related operations in the UI.
 * It allows users to register purchases or sales of products,
 * checks stock availability, and delegates order processing to services.
 */
package com.komeetta.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.komeetta.dao.*;
import com.komeetta.model.*;
import com.komeetta.service.*;
import java.util.*;

public class StockController {

    // UI components for user interaction
    @FXML private ComboBox<String> entityComboBox;
    @FXML private ComboBox<String> productComboBox;
    @FXML private TextField quantityTextField;
    @FXML private TextField unitPriceTextField;
    @FXML private TextField saleTextField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // DAO and service dependencies
    private final ProductDAO productDAO = new ProductDAO();
    private final PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
    private final SalesOrderDAO salesOrderDAO = new SalesOrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final PurchaseService purchaseService = new PurchaseService();
    private final SalesService salesService = new SalesService();

    // Flag to determine whether it's a sale or purchase
    public boolean isSale;

    // Maps for tracking entity and product names to IDs
    private final Map<String, Integer> entityMap = new HashMap<>();
    private final Map<String, Integer> productMap = new HashMap<>();

    /**
     * Sets the controller mode (sale or purchase) and loads related data.
     * @param value true if sale, false if purchase
     */
    public void setIsSale(boolean value) {
        isSale = value;
        loadEntities();
        loadProducts();
    }

    /**
     * Loads either customers or suppliers into the entity combo box depending on mode.
     */
    private void loadEntities() {
        entityComboBox.getItems().clear();
        entityMap.clear();
        if (isSale) {
            entityComboBox.setPromptText("Select Customer");
            for (Customer customer : customerDAO.getCustomers()) {
                entityComboBox.getItems().add(customer.getName());
                entityMap.put(customer.getName(), customer.getCustomerId());
            }
        } else {
            entityComboBox.setPromptText("Select Supplier");
            for (Supplier supplier : supplierDAO.getSuppliers()) {
                entityComboBox.getItems().add(supplier.getName());
                entityMap.put(supplier.getName(), supplier.getSupplierId());
            }
        }
    }

    /**
     * Loads all products into the product combo box.
     */
    private void loadProducts() {
        productComboBox.getItems().clear();
        productMap.clear();
        for (Product product : productDAO.getProducts()) {
            productComboBox.getItems().add(product.getName());
            productMap.put(product.getName(), product.getProductId());
        }
    }

    /**
     * Handles save button click, validates inputs and initiates sale/purchase processing.
     */
    @FXML
    private void handleSave() {
        String selectedEntity = entityComboBox.getValue();
        String selectedProduct = productComboBox.getValue();

        if (selectedEntity == null || selectedProduct == null ||
                quantityTextField.getText().isEmpty() || unitPriceTextField.getText().isEmpty()) {
            showAlert("Error", "All fields must be filled.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityTextField.getText());
            double unitPrice = Double.parseDouble(unitPriceTextField.getText());
            double sale = saleTextField.getText().isEmpty() ? 0 : Double.parseDouble(saleTextField.getText());
            int productId = productMap.get(selectedProduct);
            int entityId = entityMap.get(selectedEntity);

            if (isSale) {
                if (!isStockAvailable(productId, quantity)) return;
                processSale(entityId, productId, quantity, unitPrice, sale);
            } else {
                processPurchase(entityId, productId, quantity, unitPrice, sale);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid number format.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Processes a sales order and submits it through the service.
     */
    private void processSale(int customerId, int productId, int quantity, double unitPrice, double sale) {
        Customer customer = customerDAO.getCustomer(customerId);
        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);
        Product product = productDAO.getProductById(productId);
        SalesOrderItem item = new SalesOrderItem(salesOrder, product, quantity, unitPrice, sale);
        salesService.processSalesOrder(salesOrder, Collections.singletonList(item));
        showAlert("Success", "Sales order processed successfully.", Alert.AlertType.INFORMATION);
        closeForm();
    }

    /**
     * Processes a purchase order and submits it through the service.
     */
    private void processPurchase(int supplierId, int productId, int quantity, double unitPrice, double sale) {
        Supplier supplier = supplierDAO.getSupplier(supplierId);
        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier);
        purchaseOrderDAO.addPurchaseOrder(purchaseOrder);
        Product product = productDAO.getProductById(productId);
        PurchaseOrderItem item = new PurchaseOrderItem(purchaseOrder, product, quantity, unitPrice, sale);
        purchaseService.processPurchaseOrder(purchaseOrder, Collections.singletonList(item));
        showAlert("Success", "Purchase order processed successfully.", Alert.AlertType.INFORMATION);
        closeForm();
    }

    /**
     * Handles cancel button click by clearing the form.
     */
    @FXML
    private void handleCancel() {
        clearForm();
    }

    /**
     * Checks whether the requested quantity is available in stock.
     */
    private boolean isStockAvailable(int productId, int requestedQuantity) {
        Product product = productDAO.getProductById(productId);
        int availableStock = product.getQuantity();
        if (requestedQuantity > availableStock) {
            showAlert("Stock Error", "Not enough stock available. Only " + availableStock + " units left.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Displays an alert with the given title, message, and alert type.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Clears all form fields and selections.
     */
    private void clearForm() {
        entityComboBox.getSelectionModel().clearSelection();
        productComboBox.getSelectionModel().clearSelection();
        quantityTextField.clear();
        unitPriceTextField.clear();
        saleTextField.clear();
    }

    /**
     * Clears the form and closes the current window.
     */
    private void closeForm() {
        clearForm();
        cancelButton.getScene().getWindow().hide();
    }
}
