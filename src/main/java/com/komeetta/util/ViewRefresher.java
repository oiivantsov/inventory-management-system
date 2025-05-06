/**
 * ViewRefresher is a utility class that provides static methods to refresh
 * JavaFX TableViews for different entities such as customers, suppliers, products,
 * purchase orders, and sales orders.
 */
package com.komeetta.util;

import com.komeetta.dao.*;
import com.komeetta.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.*;

public class ViewRefresher {

    /**
     * Refreshes the customer table with data from the database.
     * @param customerTable the TableView to update
     */
    public static void refreshCustomerView(TableView<Customer> customerTable) {
        customerTable.getItems().clear();
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customerList = customerDAO.getCustomers();
        customerTable.setItems(FXCollections.observableArrayList(customerList));
    }

    /**
     * Refreshes the supplier table with data from the database.
     * @param supplierTable the TableView to update
     */
    public static void refreshSupplierView(TableView<Supplier> supplierTable) {
        supplierTable.getItems().clear();
        SupplierDAO supplierDAO = new SupplierDAO();
        List<Supplier> supplierList = supplierDAO.getSuppliers();
        supplierTable.setItems(FXCollections.observableArrayList(supplierList));
    }

    /**
     * Refreshes the product table with localized product data.
     * Handles localization of name, category, and description fields.
     */
    public static void refreshProductView(TableView<Product> productTable) {
        productTable.getItems().clear();

        ProductDAO productDAO = new ProductDAO();
        List<Product> fullList = productDAO.getProducts();

        Locale locale = LanguageUtil.getCurrentLocale();
        String lang = locale.getLanguage();

        List<Product> localizedList = new ArrayList<>();
        for (Product original : fullList) {
            Product localized = new Product();
            localized.setProductId(original.getProductId());
            localized.setBrand(original.getBrand());
            localized.setQuantity(original.getQuantity());

            switch (lang) {
                case "fi" -> localized.setName(Optional.ofNullable(original.getNameFi()).orElse(original.getName()));
                case "ru" -> localized.setName(Optional.ofNullable(original.getNameRu()).orElse(original.getName()));
                case "ja" -> localized.setName(Optional.ofNullable(original.getNameJa()).orElse(original.getName()));
                default -> localized.setName(original.getName());
            }

            switch (lang) {
                case "fi" -> localized.setCategory(Optional.ofNullable(original.getCategoryFi()).orElse(original.getCategory()));
                case "ru" -> localized.setCategory(Optional.ofNullable(original.getCategoryRu()).orElse(original.getCategory()));
                case "ja" -> localized.setCategory(Optional.ofNullable(original.getCategoryJa()).orElse(original.getCategory()));
                default -> localized.setCategory(original.getCategory());
            }

            switch (lang) {
                case "fi" -> localized.setDescription(Optional.ofNullable(original.getDescriptionFi()).orElse(original.getDescription()));
                case "ru" -> localized.setDescription(Optional.ofNullable(original.getDescriptionRu()).orElse(original.getDescription()));
                case "ja" -> localized.setDescription(Optional.ofNullable(original.getDescriptionJa()).orElse(original.getDescription()));
                default -> localized.setDescription(original.getDescription());
            }

            localizedList.add(localized);
        }

        ObservableList<Product> products = FXCollections.observableArrayList(localizedList);
        productTable.setItems(products);
    }

    /**
     * Refreshes the purchase order table with data from the database.
     */
    public static void refreshPurchaseView(TableView<PurchaseOrder> purchaseOrderTable) {
        purchaseOrderTable.getItems().clear();
        PurchaseOrderDAO dao = new PurchaseOrderDAO();
        List<PurchaseOrder> list = dao.getPurchaseOrders();
        purchaseOrderTable.setItems(FXCollections.observableArrayList(list));
    }

    /**
     * Refreshes the sales order table with data from the database.
     */
    public static void refreshSaleView(TableView<SalesOrder> salesOrderTable) {
        salesOrderTable.getItems().clear();
        SalesOrderDAO dao = new SalesOrderDAO();
        List<SalesOrder> list = dao.getSalesOrders();
        salesOrderTable.setItems(FXCollections.observableArrayList(list));
    }
}