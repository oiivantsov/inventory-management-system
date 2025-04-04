package com.komeetta.controller;

import com.komeetta.dao.*;
import com.komeetta.model.*;
import com.komeetta.view.SalesPurchaseGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.komeetta.view.AddEntityGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import com.komeetta.view.EditObjectGUI;
import com.komeetta.view.MassImportGUI;
import statistics.DashboardStats;
import statistics.Stat;

public class DashboardController {

    //  Middle StackPane and VBoxes
    @FXML
    private StackPane contentArea;

    @FXML
    private VBox productVBox;

    @FXML
    private VBox customerVBox;

    @FXML
    private VBox supplierVBox;

    @FXML
    private VBox purchaseVBox;

    @FXML
    private VBox saleVBox;

    @FXML
    private VBox HomeVBox;

    @FXML
    private Button buttonEdit;

    @FXML
    private Button buttonAdd;

    //  Customer Table and Columns
    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> colCustomerID;

    @FXML
    private TableColumn<Customer, String> colCustomerName;

    @FXML
    private TableColumn<Customer, String> colCustomerMail;

    @FXML
    private TableColumn<Customer, Integer> colCustomerNum;

    @FXML
    private TableColumn<Customer, String> colCustomerAddr;

    //  Supplier Table and Columns
    @FXML
    private TableView<Supplier> supplierTable;

    @FXML
    private TableColumn<Supplier, Integer> colSupplierID;

    @FXML
    private TableColumn<Supplier, String> colSupplierName;

    @FXML
    private TableColumn<Supplier, String> colSupplierMail;

    @FXML
    private TableColumn<Supplier, Integer> colSupplierNum;

    @FXML
    private TableColumn<Supplier, String> colSupplierAddr;

    //  Product Table and Columns
    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> colProductID;

    @FXML
    private TableColumn<Product, String> colProductName;

    @FXML
    private TableColumn<Product, String> colProductCategory;

    @FXML
    private TableColumn<Product, String> colProductBrand;

    @FXML
    private TableColumn<Product, Integer> colProductStock;

    @FXML
    private TableColumn<Product, String> colProductDescription;

    //  PurchaseOrder Table and Columns
    @FXML
    private TableView<PurchaseOrder> purchaseOrderTable;

    @FXML
    private TableColumn<PurchaseOrder, Integer> colPurchaseID;

    @FXML
    private TableColumn<PurchaseOrder, Integer> colPurchaseSupplierID;

    @FXML
    private TableColumn<PurchaseOrder, Date> colPurchaseDate;

    @FXML
    private TableColumn<PurchaseOrder, OrderStatus> colPurchaseState;

    @FXML
    private TableColumn<PurchaseOrder, Integer> colPurchaseTotalPrice;

    //  SalesOrder Table and Columns
    @FXML
    private TableView<SalesOrder> salesOrderTable;

    @FXML
    private TableColumn<SalesOrder, Integer> colSaleID;

    @FXML
    private TableColumn<SalesOrder, Integer> colSaleCustomerID;

    @FXML
    private TableColumn<SalesOrder, Date> colSaleDate;

    @FXML
    private TableColumn<SalesOrder, OrderStatus> colSaleState;

    @FXML
    private TableColumn<SalesOrder, Integer> colSaleTotalPrice;

    // Stats Table and Columns
    @FXML
    private TableView<Stat> ordersTable;

    @FXML
    private TableColumn<Stat, String> colOrderStatName;

    @FXML
    private TableColumn<Stat, String> colOrderStatValue;

    @FXML
    private TableView<Stat> revenueTable;

    @FXML
    private TableColumn<Stat, String> colRevenueStatName;

    @FXML
    private TableColumn<Stat, String> colRevenueStatValue;

    @FXML
    private Label greetingLabel;

    @FXML
    private Label statsLabel;

    @FXML
    private Button statsButton;

    @FXML
    private ComboBox<LanguageOption> languageSelector;

    @FXML
    private Button importButton;

    private Object selectedItem = null;

    private User user;

    private String currentView;

    private DashboardStats dashboardStats;

    @FXML
    public void initialize() {
        dashboardStats = new DashboardStats();

        // === Table column initializations ===
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCustomerNum.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colCustomerAddr.setCellValueFactory(new PropertyValueFactory<>("address"));

        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupplierID.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSupplierNum.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colSupplierAddr.setCellValueFactory(new PropertyValueFactory<>("address"));

        colProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProductCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colProductBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colProductStock.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        colPurchaseID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colPurchaseSupplierID.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colPurchaseDate.setCellValueFactory(new PropertyValueFactory<>("Order_date"));
        colPurchaseState.setCellValueFactory(new PropertyValueFactory<>("OrderStatus"));
        colPurchaseTotalPrice.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));

        colSaleID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colSaleCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colSaleDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colSaleState.setCellValueFactory(new PropertyValueFactory<>("OrderStatus"));
        colSaleTotalPrice.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));

        colOrderStatName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colOrderStatValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        colRevenueStatName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRevenueStatValue.setCellValueFactory(new PropertyValueFactory<>("value"));

        importButton.setOnAction(event -> handleImportButtonAction());

        // === Table selection listeners ===
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                buttonEdit.setDisable(false);
                System.out.println("Selected Product: " + newSelection);
            }
        });

        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                buttonEdit.setDisable(false);
                System.out.println("Selected Customer: " + newSelection);
            }
        });

        supplierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                buttonEdit.setDisable(false);
                System.out.println("Selected Supplier: " + newSelection);
            }
        });

        salesOrderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                System.out.println("Selected Sale Order: " + newSelection);
            }
        });

        purchaseOrderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                System.out.println("Selected Purchase Order: " + newSelection);
            }
        });

        // === Language selector ===
        languageSelector.getItems().setAll(
                new LanguageOption("English", "EN"),
                new LanguageOption("Finnish", "FI"),
                new LanguageOption("Russian", "RU"),
                new LanguageOption("Japanese", "JA")
        );

        // Set current locale selection
        String currentLang = LanguageUtil.getCurrentLocale().getLanguage().toUpperCase();
        for (LanguageOption option : languageSelector.getItems()) {
            if (option.getCode().equalsIgnoreCase(currentLang)) {
                languageSelector.getSelectionModel().select(option);
                break;
            }
        }

        languageSelector.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(LanguageOption item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " - " + item.getCode());
            }
        });

        languageSelector.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(LanguageOption item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCode());
            }
        });

        // === Buttons ===
        if (buttonEdit == null) {
            System.err.println("ERROR: buttonEdit is NULL! Check fx:id in Dashboard.fxml.");
        } else {
            buttonEdit.setOnAction(event -> handleEditButtonAction());
        }

        if (contentArea == null) {
            System.err.println("ERROR: contentArea is NULL! Check fx:id in Dashboard.fxml.");
        }
    }


    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        refreshStatsView();
        showView(HomeVBox);
    }

    @FXML
    private void handleEditButtonAction() {
        EditObjectGUI.display(selectedItem);
        if (selectedItem instanceof Product) {
            refreshProductView();
        } else if (selectedItem instanceof Customer) {
            refreshCustomerView();
        } else if (selectedItem instanceof Supplier) {
            refreshSupplierView();
        }
    }

    @FXML
    private void handleAddButtonAction() {
        if (currentView.equals("Sales")) {
            SalesPurchaseGUI.display(true);
            refreshSaleView();
        } else if (currentView.equals("Purchases")) {
            SalesPurchaseGUI.display(false);
            refreshPurchaseView();
        }else {
            AddEntityGUI.display();
            refreshCustomerView();
            refreshProductView();
            refreshSupplierView();
        }
    }

    @FXML
    private void handleImportButtonAction() {
        MassImportGUI.display();
        refreshProductView();
    }

    // onAction methods for view selection buttons
    @FXML
    private void handleProductButtonAction() {
        currentView = "Product";
        showView(productVBox);
    }

    @FXML
    private void handleCustomerButtonAction() {
        currentView = "Customer";
        showView(customerVBox);
    }

    @FXML
    private void handleSupplierButtonAction() {
        currentView = "Supplier";
        showView(supplierVBox);
    }

    @FXML
    private void handlePurchaseButtonAction() {
        currentView = "Purchases";
        showView(purchaseVBox);
    }

    @FXML
    private void handleSaleButtonAction() {
        currentView = "Sales";
        showView(saleVBox);
    }

    @FXML
    public void handleLogoutButtonAction() {
        this.user = null;

        //Close current window
        Stage stage = (Stage) greetingLabel.getScene().getWindow();
        stage.close();

        try{
            // Open login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Login.fxml"), ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale()));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(loader.load()));
            newStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



// TODO: Implement language change
    @FXML
    private void handleLanguageChange(ActionEvent event) {
        LanguageOption selected = languageSelector.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Locale newLocale = switch (selected.getCode()) {
                case "FI" -> new Locale("fi");
                case "RU" -> new Locale("ru");
                case "JA" -> new Locale("ja");
                default -> new Locale("en");
            };
            if (!LanguageUtil.getCurrentLocale().equals(newLocale)) {
                LanguageUtil.setLocale(newLocale);
                reloadUIWithBundle(ResourceBundle.getBundle("UIMessages", newLocale));
            }
        }
    }


    private void reloadUIWithBundle(ResourceBundle bundle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Dashboard.fxml"), bundle);
            Parent root = loader.load();

            Stage stage = (Stage) languageSelector.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Shows corresponding View to passed variable
    private void showView(VBox view) {
        if (contentArea == null) {
            System.err.println("ERROR: contentArea is NULL! Check fx:id in Dashboard.fxml.");
            return;
        }

        for (Node node : contentArea.getChildren()) {
            node.setVisible(node == view);
            node.setManaged(node == view);
            if(view == customerVBox){
                refreshCustomerView();
            } else if (view == supplierVBox) {
                refreshSupplierView();
            } else if (view == productVBox) {
                refreshProductView();
            } else if(view == purchaseVBox){
                refreshPurchaseView();
            } else if (view == saleVBox) {
                refreshSaleView();
            } else if (view == HomeVBox) {
                refreshStatsView();
            }
        }
    }

    public void setInitialView(User user) {
        this.user = user;

        greetingLabel.setText("Welcome, " + user.getUsername() + "\n" +
                "Use the buttons on the left to manage your company inventory.");

        statsLabel.setText("Statistics:");

        buttonEdit.setDisable(true);

        // Show initial view
        showView(HomeVBox);
    }

    public void refreshProductView() {

        productTable.getItems().clear();

        // load data from a database
        ProductDAO productDAO = new ProductDAO();
        List<Product> productList = productDAO.getProducts();
        ObservableList<Product> products = FXCollections.observableArrayList(productList);

        productTable.setItems(products);

        buttonEdit.setDisable(true);
    }
    public void refreshCustomerView() {

        customerTable.getItems().clear();

        // load data from a database
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customerList = customerDAO.getCustomers();
        ObservableList<Customer> customers = FXCollections.observableArrayList(customerList);

        customerTable.setItems(customers);

        buttonEdit.setDisable(true);
    }
    public void refreshSupplierView() {
        // load data from a database
        supplierTable.getItems().clear();

        SupplierDAO supplierDAO = new SupplierDAO();
        List<Supplier> supplierList = supplierDAO.getSuppliers();
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList(supplierList);

        supplierTable.setItems(suppliers);

        buttonEdit.setDisable(true);
    }

    public void refreshPurchaseView() {
        //Load data from a database
        purchaseOrderTable.getItems().clear();

        PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
        List<PurchaseOrder> purchaseOrderList = purchaseOrderDAO.getPurchaseOrders();
        ObservableList<PurchaseOrder> purchaseOrders = FXCollections.observableArrayList(purchaseOrderList);
        purchaseOrderTable.setItems(purchaseOrders);
    }

    public void refreshSaleView() {
        //Load data from a database
        salesOrderTable.getItems().clear();

        SalesOrderDAO salesOrderDAO = new SalesOrderDAO();
        List<SalesOrder> salesOrderList = salesOrderDAO.getSalesOrders();
        ObservableList<SalesOrder> salesOrders = FXCollections.observableArrayList(salesOrderList);
        salesOrderTable.setItems(salesOrders);
    }


    public void refreshStatsView() {
        salesOrderTable.getItems().clear();
        updateStatsTables();
    }

    /**
     * Handles the action when the stats button is clicked
     * Opens a dialog to enter the filename for the CSV report
     * Creates the CSV file with the statistics
     * Shows an error message if the filename is invalid
     */
    @FXML
    private void handleStatsButtonAction() {
        TextInputDialog dialog = new TextInputDialog("stats_report");
        dialog.setTitle("Save CSV Report");
        dialog.setHeaderText("Enter the filename for the CSV report:");
        dialog.setContentText("Filename:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(filename -> {
            if (filename != null && !filename.isEmpty() && filename.matches("^[a-zA-Z0-9_]*$")) {
                // Use the filename to create the CSV file
                dashboardStats.createCvsStatsFile(filename);
            } else {
                // Show an error message if the filename is invalid
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Filename");
                alert.setHeaderText("The filename is invalid.");
                alert.setContentText("Please enter a valid filename without special characters.");
                alert.showAndWait();
            }
        });
    }


    // Updates the stats tables with the latest statistics
    private void updateStatsTables() {
        dashboardStats.updateStats();

        ObservableList<Stat> orderStats = FXCollections.observableArrayList(
                new Stat("Total Sales Orders", String.valueOf(dashboardStats.getTotalSalesOrders())),
                new Stat("Total Purchase Orders", String.valueOf(dashboardStats.getTotalPurchaseOrders())),
                new Stat("Difference", String.valueOf(dashboardStats.getOrderDifference()))
        );

        ObservableList<Stat> revenueStats = FXCollections.observableArrayList(
                new Stat("Total Revenue", String.format("%.2f", dashboardStats.getTotalRevenue())),
                new Stat("Last Three Months Revenue", String.format("%.2f", dashboardStats.getLastThreeMonthsRevenue()))
        );

        ordersTable.setItems(orderStats);
        revenueTable.setItems(revenueStats);
    }



}
