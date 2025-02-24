package com.komeetta.controller;

import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.SupplierDAO;
import com.komeetta.model.Customer;
import com.komeetta.model.Product;
import com.komeetta.model.Supplier;
import com.komeetta.dao.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;


public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private VBox productVBox;

    @FXML
    private VBox customerVBox;

    @FXML
    private VBox supplierVBox;

    @FXML
    private VBox HomeVBox;

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




        // Ensure only one view is visible at a time
        showView(HomeVBox);
    }

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

    /* Currently unused
    @FXML
    private void handleHomeButtonAction() {
        showView(HomeVBox);
    }
    */

    //Shows corresponding View to passed variable
    private void showView(VBox view) {
        for (Node node : contentArea.getChildren()) {
            node.setVisible(node == view);
            node.setManaged(node == view);
            if(view == customerVBox){
                refreshCustomerView();
            } else if (view == supplierVBox) {
                refreshSupplierView();
            } else if (view == productVBox) {
                refreshProductView();
            }
        }
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
}

