package com.komeetta.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.PurchaseOrderDAO;
import com.komeetta.dao.SalesOrderDAO;
import com.komeetta.dao.CustomerDAO;
import com.komeetta.dao.SupplierDAO;
import com.komeetta.model.*;
import com.komeetta.service.PurchaseService;
import com.komeetta.service.SalesService;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// TODO: Add method to check stock availability

public class StockController {

    @FXML
    private ComboBox<String> entityComboBox;

    @FXML
    private ComboBox<String> productComboBox;

    @FXML
    private TextField quantityTextField;

    @FXML
    private TextField unitPriceTextField;

    @FXML
    private TextField saleTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private final ProductDAO productDAO = new ProductDAO();
    private final PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
    private final SalesOrderDAO salesOrderDAO = new SalesOrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final PurchaseService purchaseService = new PurchaseService();
    private final SalesService salesService = new SalesService();

    public boolean isSale;
    private final Map<String, Integer> entityMap = new HashMap<>();
    private final Map<String, Integer> productMap = new HashMap<>();

    public void setIsSale(boolean value) {
        isSale = value;
        loadEntities();
        loadProducts();
    }

    private void loadEntities() {
        entityComboBox.getItems().clear();
        entityMap.clear();
        if (isSale) {
            System.out.println("Loading customers...");
            for (Customer customer : customerDAO.getCustomers()) {
                entityComboBox.getItems().add(customer.getName());
                entityMap.put(customer.getName(), customer.getCustomerId());
            }
        } else {
            for (Supplier supplier : supplierDAO.getSuppliers()) {
                entityComboBox.getItems().add(supplier.getName());
                entityMap.put(supplier.getName(), supplier.getSupplierId());
            }
        }
    }

    private void loadProducts() {
        productComboBox.getItems().clear();
        productMap.clear();
        List<Product> products = productDAO.getProducts();
        for (Product product : products) {
            productComboBox.getItems().add(product.getName());
            productMap.put(product.getName(), product.getProductId());
        }
    }

    @FXML
    private void handleSave() {
        String selectedEntity = entityComboBox.getValue();
        String selectedProduct = productComboBox.getValue();

        if (selectedEntity == null || selectedProduct == null || quantityTextField.getText().isEmpty() || unitPriceTextField.getText().isEmpty()) {
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
                processSale(entityId, productId, quantity, unitPrice, sale);
            } else {
                processPurchase(entityId, productId, quantity, unitPrice, sale);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid number format.", Alert.AlertType.ERROR);
        }
    }

    private void processSale(int customerId, int productId, int quantity, double unitPrice, double sale) {
        Customer customer = customerDAO.getCustomer(customerId);
        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);
        Product product = productDAO.getProductById(productId);
        SalesOrderItem salesOrderItem = new SalesOrderItem(salesOrder, product, quantity, unitPrice, sale);
        salesService.processSalesOrder(salesOrder, Collections.singletonList(salesOrderItem));
        showAlert("Success", "Sales order processed successfully.", Alert.AlertType.INFORMATION);
    }

    private void processPurchase(int supplierId, int productId, int quantity, double unitPrice, double sale) {
        Supplier supplier = supplierDAO.getSupplier(supplierId);
        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier);
        purchaseOrderDAO.addPurchaseOrder(purchaseOrder);
        Product product = productDAO.getProductById(productId);
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem(purchaseOrder, product, quantity, unitPrice, sale);
        purchaseService.processPurchaseOrder(purchaseOrder, Collections.singletonList(purchaseOrderItem));
        showAlert("Success", "Purchase order processed successfully.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleCancel() {
        entityComboBox.getSelectionModel().clearSelection();
        productComboBox.getSelectionModel().clearSelection();
        quantityTextField.clear();
        unitPriceTextField.clear();
        saleTextField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
