package com.komeetta.controller;

import com.komeetta.dao.CustomerDAO;
import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.SupplierDAO;
import com.komeetta.model.Customer;
import com.komeetta.model.Product;
import com.komeetta.model.Supplier;
import com.komeetta.service.TranslateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;

public class AddWindowController {

    @FXML
    private Label headlineLabel;

    @FXML
    private VBox formContainer;

    @FXML
    private TextField firstTextField;

    @FXML
    private TextField secondTextField;

    @FXML
    private TextField thirdTextField;

    @FXML
    private TextField fourthTextField;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> typeCBox;

    @FXML
    private void initialize() {
        // Populate ComboBox with types
        typeCBox.getItems().addAll("Customer", "Product", "Supplier");

        // Add listener for selection changes
        typeCBox.setOnAction(event -> updateFormFields());
    }

    private void updateFormFields() {
        String selectedType = typeCBox.getValue();

        headlineLabel.setText("Add New " + selectedType);
        if ("Product".equals(selectedType)) {
            firstTextField.setPromptText("Product Name");
            secondTextField.setPromptText("Product Category");
            thirdTextField.setPromptText("Product Brand");
            fourthTextField.setPromptText("Product Description");
        }else if ("Supplier".equals(selectedType) || "Customer".equals(selectedType)) {
            firstTextField.setPromptText("Name");
            secondTextField.setPromptText("Email");
            thirdTextField.setPromptText("Phone");
            fourthTextField.setPromptText("Address");
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        String selectedType = typeCBox.getValue();

        String firstFieldV = firstTextField.getText().trim();
        String secondFieldV = secondTextField.getText().trim();
        String thirdFieldV = thirdTextField.getText().trim();
        String fourthFieldV = fourthTextField.getText().trim();

        if (firstFieldV.isEmpty() || secondFieldV.isEmpty() || thirdFieldV.isEmpty() || fourthFieldV.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled before adding an entity.");
            return;
        }

        if (!secondFieldV.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            if (selectedType.equals("Customer") || selectedType.equals("Supplier")){
                showAlert("Invalid Email", "Please enter a valid email address.");
                return;
            }
        }

        if (!thirdFieldV.matches("^\\d{10,15}$")) {
            if (selectedType.equals("Customer") || selectedType.equals("Supplier")){
                showAlert("Invalid Phone Number", "Phone number must be between 10-15 digits.");
                return;
            }
        }

        switch (selectedType){
            case "Customer":
                Customer customer = new Customer();
                customer.setName(firstFieldV);
                customer.setEmail(secondFieldV);
                customer.setAddress(fourthFieldV);
                customer.setPhoneNumber(thirdFieldV);

                CustomerDAO customerDAO = new CustomerDAO();
                customerDAO.addCustomer(customer);

                showAlert("Success", "Entity added successfully!");
                break;

            case "Product":
                ProductDAO productDAO = new ProductDAO();

                try {
                    String inputName = firstFieldV;
                    String inputCategory = secondFieldV;
                    String inputDescription = fourthFieldV;
                    String inputBrand = thirdFieldV;

                    // async translation
                    CompletableFuture<String> name = TranslateUtil.translate(inputName, "en");
                    CompletableFuture<String> nameFi = TranslateUtil.translate(inputName, "fi");
                    CompletableFuture<String> nameJa = TranslateUtil.translate(inputName, "ja");
                    CompletableFuture<String> nameRu = TranslateUtil.translate(inputName, "ru");

                    CompletableFuture<String> category = TranslateUtil.translate(inputCategory, "en");
                    CompletableFuture<String> categoryFi = TranslateUtil.translate(inputCategory, "fi");
                    CompletableFuture<String> categoryJa = TranslateUtil.translate(inputCategory, "ja");
                    CompletableFuture<String> categoryRu = TranslateUtil.translate(inputCategory, "ru");

                    CompletableFuture<String> description = TranslateUtil.translate(inputDescription, "en");
                    CompletableFuture<String> descriptionFi = TranslateUtil.translate(inputDescription, "fi");
                    CompletableFuture<String> descriptionJa = TranslateUtil.translate(inputDescription, "ja");
                    CompletableFuture<String> descriptionRu = TranslateUtil.translate(inputDescription, "ru");

                    // wait for all translations to complete
                    CompletableFuture.allOf(
                            name, nameFi, nameJa, nameRu,
                            category, categoryFi, categoryJa, categoryRu,
                            description, descriptionFi, descriptionJa, descriptionRu
                    ).join();

                    Product product = new Product();
                    product.setName(name.join());
                    product.setNameFi(nameFi.join());
                    product.setNameJa(nameJa.join());
                    product.setNameRu(nameRu.join());

                    product.setCategory(category.join());
                    product.setCategoryFi(categoryFi.join());
                    product.setCategoryJa(categoryJa.join());
                    product.setCategoryRu(categoryRu.join());

                    product.setDescription(description.join());
                    product.setDescriptionFi(descriptionFi.join());
                    product.setDescriptionJa(descriptionJa.join());
                    product.setDescriptionRu(descriptionRu.join());

                    product.setBrand(inputBrand);

                    productDAO.addProduct(product);

                } catch (Exception e) {
                    showAlert("Failed to add product", "Please try again.");
                    e.printStackTrace();
                }
                break;

            case "Supplier":
                Supplier supplier = new Supplier();
                supplier.setName(firstFieldV);
                supplier.setEmail(secondFieldV);
                supplier.setAddress(fourthFieldV);
                supplier.setPhoneNumber(thirdFieldV);

                SupplierDAO supplierDAO = new SupplierDAO();
                supplierDAO.addSupplier(supplier);

                showAlert("Success", "Entity added successfully!");
                break;
        }
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        firstTextField.clear();
        secondTextField.clear();
        thirdTextField.clear();
        fourthTextField.clear();

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

