package com.komeetta.controller;

import com.komeetta.dao.*;
import com.komeetta.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.komeetta.view.AddEntityGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;

import com.komeetta.view.EditProductGUI;

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

    @FXML
    private Label greetingLabel;

    private Object selectedItem;
    private User user;

    @FXML
    public void initialize() {

        //initialize columns in customerTable.
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCustomerNum.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colCustomerAddr.setCellValueFactory(new PropertyValueFactory<>("address"));

        //initialize columns in supplierTable.
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupplierID.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSupplierNum.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colSupplierAddr.setCellValueFactory(new PropertyValueFactory<>("address"));

        //initialize columns in productTable.
        colProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProductCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colProductBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colProductStock.setCellValueFactory(new PropertyValueFactory<>("stock_level "));
        colProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        //initialize columns in purchaseOrderTable.
        colPurchaseID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colPurchaseSupplierID.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        colPurchaseDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colPurchaseState.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPurchaseTotalPrice.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));

        //initialize columns in saleOrderTable.
        colSaleID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colSaleCustomerID.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colSaleDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colSaleState.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSaleTotalPrice.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));

        // Add selection listeners for each table
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                System.out.println("Selected Product: " + newSelection);
            }
        });

        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                System.out.println("Selected Customer: " + newSelection);
            }
        });
        // Ensure only one view is visible at a time
        showView(HomeVBox);

        // Debugging: Check if buttonEdit is null
        if (buttonEdit == null) {
            System.err.println("ERROR: buttonEdit is NULL! Check fx:id in Dashboard.fxml.");
        } else {
            buttonEdit.setOnAction(event -> handleEditButtonAction());
        }

        // Debugging: Check if contentArea is null
        if (contentArea == null) {
            System.err.println("ERROR: contentArea is NULL! Check fx:id in Dashboard.fxml.");
        }
    }

    @FXML
    private void handleEditButtonAction() {
        EditProductGUI.display();
    }

    @FXML
    private void handleAddButtonAction() {
        AddEntityGUI.display();
    }

        supplierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                System.out.println("Selected Supplier: " + newSelection);
            }
        });

        salesOrderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                System.out.println("Selected Supplier: " + newSelection);
            }
        });

        purchaseOrderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                System.out.println("Selected Supplier: " + newSelection);
            }
        });
    }

    // onAction methods for view selection buttons
    @FXML
    private void handleProductButtonAction() {
        showView(productVBox);
    }

    @FXML
    private void handleCustomerButtonAction() {
        showView(customerVBox);
    }

    @FXML
    private void handleSupplierButtonAction() {
        showView(supplierVBox);
    }

    @FXML
    private void handlePurchaseButtonAction() {
        showView(purchaseVBox);
    }

    @FXML
    private void handleSaleButtonAction() {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Login.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(loader.load()));
            newStage.show();
        }catch (Exception e){
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
            }
        }
    }

    public void setInitialView(User user) {
        this.user = user;

        greetingLabel.setText("Welcome, " + user.getUsername() + "\n" +
                "Use the buttons on the left to manage your company inventory.");

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
    }
    public void refreshCustomerView() {

        customerTable.getItems().clear();

        // load data from a database
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customerList = customerDAO.getCustomers();
        ObservableList<Customer> customers = FXCollections.observableArrayList(customerList);

        customerTable.setItems(customers);

    }
    public void refreshSupplierView() {
        // load data from a database
        supplierTable.getItems().clear();

        SupplierDAO supplierDAO = new SupplierDAO();
        List<Supplier> supplierList = supplierDAO.getSuppliers();
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList(supplierList);

        supplierTable.setItems(suppliers);
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
}
