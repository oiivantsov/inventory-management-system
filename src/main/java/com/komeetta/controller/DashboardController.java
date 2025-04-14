/**
 * DashboardController handles the main dashboard view logic for the inventory system.
 * It initializes views, handles user actions such as editing, adding, importing,
 * switching between views, and logging out. It uses utility classes for modular behavior.
 */
package com.komeetta.controller;

import com.komeetta.model.*;
import com.komeetta.util.*;
import com.komeetta.view.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import statistics.Stat;

import java.util.*;

public class DashboardController {

    // UI components injected via FXML
    @FXML private StackPane contentArea;
    @FXML private VBox productVBox, customerVBox, supplierVBox, purchaseVBox, saleVBox, HomeVBox;
    @FXML private Button buttonEdit, importButton;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> colCustomerID;
    @FXML private TableColumn<Customer, String> colCustomerName, colCustomerMail, colCustomerAddr;
    @FXML private TableColumn<Customer, Integer> colCustomerNum;
    @FXML private TableView<Supplier> supplierTable;
    @FXML private TableColumn<Supplier, Integer> colSupplierID;
    @FXML private TableColumn<Supplier, String> colSupplierName, colSupplierMail, colSupplierAddr;
    @FXML private TableColumn<Supplier, Integer> colSupplierNum;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colProductID, colProductStock;
    @FXML private TableColumn<Product, String> colProductName, colProductCategory, colProductBrand, colProductDescription;
    @FXML private TableView<PurchaseOrder> purchaseOrderTable;
    @FXML private TableColumn<PurchaseOrder, Integer> colPurchaseID, colPurchaseSupplierID, colPurchaseTotalPrice;
    @FXML private TableColumn<PurchaseOrder, Date> colPurchaseDate;
    @FXML private TableColumn<PurchaseOrder, OrderStatus> colPurchaseState;
    @FXML private TableView<SalesOrder> salesOrderTable;
    @FXML private TableColumn<SalesOrder, Integer> colSaleID, colSaleCustomerID, colSaleTotalPrice;
    @FXML private TableColumn<SalesOrder, Date> colSaleDate;
    @FXML private TableColumn<SalesOrder, OrderStatus> colSaleState;
    @FXML private TableView<Stat> ordersTable, revenueTable;
    @FXML private TableColumn<Stat, String> colOrderStatName, colOrderStatValue, colRevenueStatName, colRevenueStatValue;
    @FXML private Label greetingLabel, statsLabel;
    @FXML private ComboBox<LanguageOption> languageSelector;

    private Object selectedItem;
    private User user;
    private String currentView;
    private final StatsHandler statsHandler = new StatsHandler();

    /**
     * Initializes the dashboard view components, sets up table columns, event listeners,
     * language selector, and import functionality.
     */
    @FXML
    public void initialize() {
        TableInitializer.initializeCustomerTable(customerTable, colCustomerID, colCustomerName, colCustomerMail, colCustomerNum, colCustomerAddr);
        TableInitializer.initializeSupplierTable(supplierTable, colSupplierID, colSupplierName, colSupplierMail, colSupplierNum, colSupplierAddr);
        TableInitializer.initializeProductTable(productTable, colProductID, colProductName, colProductCategory, colProductBrand, colProductStock, colProductDescription);
        TableInitializer.initializePurchaseOrderTable(purchaseOrderTable, colPurchaseID, colPurchaseSupplierID, colPurchaseDate, colPurchaseState, colPurchaseTotalPrice);
        TableInitializer.initializeSalesOrderTable(salesOrderTable, colSaleID, colSaleCustomerID, colSaleDate, colSaleState, colSaleTotalPrice);
        TableInitializer.initializeStatTable(colOrderStatName, colOrderStatValue);
        TableInitializer.initializeStatTable(colRevenueStatName, colRevenueStatValue);

        LanguageSelectorHandler.setupLanguageSelector(languageSelector, statsLabel);
        languageSelector.setOnAction(this::handleLanguageChange);

        importButton.setOnAction(event -> {
            MassImportGUI.display();
            ViewRefresher.refreshProductView(productTable);
        });

        setupSelectionListeners();

        if (buttonEdit != null) buttonEdit.setOnAction(event -> handleEditButtonAction());
        if (contentArea == null) System.err.println("ERROR: contentArea is NULL! Check fx:id in Dashboard.fxml.");
    }

    /**
     * Attaches a selection listener to a TableView and enables edit button on selection.
     */
    private <T> void setupSelectionListener(TableView<T> table) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                buttonEdit.setDisable(false);
            }
        });
    }

    /**
     * Sets up selection listeners for all relevant tables.
     */
    private void setupSelectionListeners() {
        setupSelectionListener(productTable);
        setupSelectionListener(customerTable);
        setupSelectionListener(supplierTable);
        setupSelectionListener(purchaseOrderTable);
        setupSelectionListener(salesOrderTable);
    }

    /**
     * Handles switching to the home view and refreshing stats.
     */
    @FXML private void handleHomeButtonAction(ActionEvent event) {
        refreshStatsView();
        showView(HomeVBox);
    }

    /**
     * Opens the edit dialog for the selected item and refreshes relevant view.
     */
    @FXML private void handleEditButtonAction() {
        EditObjectGUI.display(selectedItem);
        if (selectedItem instanceof Product) ViewRefresher.refreshProductView(productTable);
        else if (selectedItem instanceof Customer) ViewRefresher.refreshCustomerView(customerTable);
        else if (selectedItem instanceof Supplier) ViewRefresher.refreshSupplierView(supplierTable);
    }

    /**
     * Opens add dialogs for entities or orders depending on current view, then refreshes data.
     */
    @FXML private void handleAddButtonAction() {
        if ("Sales".equals(currentView)) {
            SalesPurchaseGUI.display(true);
            ViewRefresher.refreshSaleView(salesOrderTable);
        } else if ("Purchases".equals(currentView)) {
            SalesPurchaseGUI.display(false);
            ViewRefresher.refreshPurchaseView(purchaseOrderTable);
        } else {
            AddEntityGUI.display();
            ViewRefresher.refreshCustomerView(customerTable);
            ViewRefresher.refreshProductView(productTable);
            ViewRefresher.refreshSupplierView(supplierTable);
        }
    }

    /**
     * Opens a dialog to export statistics to a CSV file.
     */
    @FXML private void handleStatsButtonAction() {
        statsHandler.handleStatsExport(statsLabel);
    }

    /**
     * Handles changing the application language.
     */
    @FXML private void handleLanguageChange(ActionEvent event) {
        LanguageSelectorHandler.handleLanguageChange(languageSelector);
    }

    /**
     * Logs out the current user and returns to the login screen.
     */
    @FXML public void handleLogoutButtonAction() {
        this.user = null;
        Stage stage = (Stage) greetingLabel.getScene().getWindow();
        stage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Login.fxml"), ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale()));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(loader.load(), 600, 400));
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the user and displays the home view with greeting and stats.
     * @param user The authenticated user
     */
    public void setInitialView(User user) {
        this.user = user;
        greetingLabel.setText(LanguageUtil.getString("str_welcome") + ", " + user.getUsername() + "\n" + LanguageUtil.getString("str_msg_manage_inventory"));

        statsLabel.setText(LanguageUtil.getString("str_label_stats")+":");
        buttonEdit.setDisable(true);
        showView(HomeVBox);
    }

    /**
     * Switches visible view in the dashboard and refreshes associated data.
     */
    private void showView(VBox view) {
        for (Node node : contentArea.getChildren()) {
            node.setVisible(node == view);
            node.setManaged(node == view);
        }

        if (view == customerVBox) ViewRefresher.refreshCustomerView(customerTable);
        else if (view == supplierVBox) ViewRefresher.refreshSupplierView(supplierTable);
        else if (view == productVBox) ViewRefresher.refreshProductView(productTable);
        else if (view == purchaseVBox) ViewRefresher.refreshPurchaseView(purchaseOrderTable);
        else if (view == saleVBox) ViewRefresher.refreshSaleView(salesOrderTable);
        else if (view == HomeVBox) refreshStatsView();
    }

    /**
     * Refreshes the statistics view with the latest data.
     */
    public void refreshStatsView() {
        statsHandler.refreshStatsTables(ordersTable, revenueTable);
    }

    @FXML private void handleProductButtonAction() {
        currentView = "Product";
        showView(productVBox);
    }

    @FXML private void handleCustomerButtonAction() {
        currentView = "Customer";
        showView(customerVBox);
    }

    @FXML private void handleSupplierButtonAction() {
        currentView = "Supplier";
        showView(supplierVBox);
    }

    @FXML private void handlePurchaseButtonAction() {
        currentView = "Purchases";
        showView(purchaseVBox);
    }

    @FXML private void handleSaleButtonAction() {
        currentView = "Sales";
        showView(saleVBox);
    }
}