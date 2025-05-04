/**
 * TableInitializer provides static helper methods for initializing
 * JavaFX TableView columns for different data models.
 * It uses PropertyValueFactory to bind data model properties to UI table columns.
 */
package com.komeetta.util;

import com.komeetta.model.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * TableInitializer class provides methods to initialize columns for various tables
 * in the JavaFX application. It uses PropertyValueFactory to bind data model properties
 * to the UI table columns.
 */
public class TableInitializer {

    /**
     * Initializes columns for the Customer table.
     */
    public static void initializeCustomerTable(TableView<Customer> table,
                                               TableColumn<Customer, Integer> idCol,
                                               TableColumn<Customer, String> nameCol,
                                               TableColumn<Customer, String> emailCol,
                                               TableColumn<Customer, Integer> numCol,
                                               TableColumn<Customer, String> addrCol) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    /**
     * Initializes columns for the Supplier table.
     */
    public static void initializeSupplierTable(TableView<Supplier> table,
                                               TableColumn<Supplier, Integer> idCol,
                                               TableColumn<Supplier, String> nameCol,
                                               TableColumn<Supplier, String> emailCol,
                                               TableColumn<Supplier, Integer> numCol,
                                               TableColumn<Supplier, String> addrCol) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    /**
     * Initializes columns for the Product table.
     */
    public static void initializeProductTable(TableView<Product> table,
                                              TableColumn<Product, Integer> idCol,
                                              TableColumn<Product, String> nameCol,
                                              TableColumn<Product, String> categoryCol,
                                              TableColumn<Product, String> brandCol,
                                              TableColumn<Product, Integer> stockCol,
                                              TableColumn<Product, String> descCol) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    /**
     * Initializes columns for the PurchaseOrder table.
     */
    public static void initializePurchaseOrderTable(TableView<PurchaseOrder> table,
                                                    TableColumn<PurchaseOrder, Integer> idCol,
                                                    TableColumn<PurchaseOrder, Integer> supIdCol,
                                                    TableColumn<PurchaseOrder, java.util.Date> dateCol,
                                                    TableColumn<PurchaseOrder, OrderStatus> statusCol,
                                                    TableColumn<PurchaseOrder, Integer> totalCol) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        supIdCol.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Order_date"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("OrderStatus"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));
    }

    /**
     * Initializes columns for the SalesOrder table.
     */
    public static void initializeSalesOrderTable(TableView<SalesOrder> table,
                                                 TableColumn<SalesOrder, Integer> idCol,
                                                 TableColumn<SalesOrder, Integer> custIdCol,
                                                 TableColumn<SalesOrder, java.util.Date> dateCol,
                                                 TableColumn<SalesOrder, OrderStatus> statusCol,
                                                 TableColumn<SalesOrder, Integer> totalCol) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("OrderStatus"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));
    }

    /**
     * Initializes generic Stat table columns.
     */
    public static void initializeStatTable(TableColumn<?, String> nameCol, TableColumn<?, String> valueCol) {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
    }
}