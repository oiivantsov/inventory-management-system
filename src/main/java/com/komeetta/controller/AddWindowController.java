package com.komeetta.controller;

import com.komeetta.dao.CustomerDAO;
import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.SupplierDAO;
import com.komeetta.model.Customer;
import com.komeetta.model.Product;
import com.komeetta.model.Supplier;
import com.komeetta.service.TranslateUtil;
import com.komeetta.util.LanguageUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * Controller class for Add Entity form.
 * Handles entity selection, form field population, validation, and database insertion.
 */
public class AddWindowController {

    @FXML private Label headlineLabel;
    @FXML private VBox formContainer;
    @FXML private TextField firstTextField, secondTextField, thirdTextField, fourthTextField;
    @FXML private Button addButton, cancelButton;
    @FXML private ComboBox<String> typeCBox;

    private String customerLabel, productLabel, supplierLabel;

    /**
     * Initializes the form and sets up localized ComboBox values and listeners.
     */
    @FXML
    private void initialize() {
        customerLabel = LanguageUtil.getString("str_customer");
        productLabel = LanguageUtil.getString("str_product");
        supplierLabel = LanguageUtil.getString("str_supplier");

        // Populate ComboBox with localized types
        typeCBox.getItems().addAll(customerLabel, productLabel, supplierLabel);

        // Add listener for selection changes
        typeCBox.setOnAction(event -> updateFormFields());
    }

    /**
     * Updates placeholder texts and form headline based on selected entity type.
     */
    private void updateFormFields() {
        String selectedType = typeCBox.getValue();
        headlineLabel.setText(LanguageUtil.getString("str_add_new") + " " + selectedType);

        if (selectedType.equals(productLabel)) {
            firstTextField.setPromptText(LanguageUtil.getString("str_product_name"));
            secondTextField.setPromptText(LanguageUtil.getString("str_product_category"));
            thirdTextField.setPromptText(LanguageUtil.getString("str_product_brand"));
            fourthTextField.setPromptText(LanguageUtil.getString("str_product_description"));
        } else {
            firstTextField.setPromptText(LanguageUtil.getString("str_name"));
            secondTextField.setPromptText(LanguageUtil.getString("str_email"));
            thirdTextField.setPromptText(LanguageUtil.getString("str_phone"));
            fourthTextField.setPromptText(LanguageUtil.getString("str_address"));
        }
    }

    /**
     * Handles form submission and entity creation.
     * Performs validation and initiates appropriate DAO actions.
     */
    @FXML
    private void handleAdd() {
        String selectedType = typeCBox.getValue();
        String firstFieldV = firstTextField.getText().trim();
        String secondFieldV = secondTextField.getText().trim();
        String thirdFieldV = thirdTextField.getText().trim();
        String fourthFieldV = fourthTextField.getText().trim();

        if (firstFieldV.isEmpty() || secondFieldV.isEmpty() || thirdFieldV.isEmpty() || fourthFieldV.isEmpty()) {
            showAlert("Validation Error", LanguageUtil.getString("str_fill_all_fields"));
            return;
        }

        boolean isEntity = selectedType.equals(customerLabel) || selectedType.equals(supplierLabel);

        if (isEntity && !secondFieldV.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert("Invalid Email", LanguageUtil.getString("str_invalid_email"));
            return;
        }

        if (isEntity && !thirdFieldV.matches("^\\d{10,15}$")) {
            showAlert("Invalid Phone Number", LanguageUtil.getString("str_invalid_phone"));
            return;
        }

        if (selectedType.equals(customerLabel)) {
            Customer customer = new Customer();
            customer.setName(firstFieldV);
            customer.setEmail(secondFieldV);
            customer.setAddress(fourthFieldV);
            customer.setPhoneNumber(thirdFieldV);
            new CustomerDAO().addCustomer(customer);
            showAlert("Success", LanguageUtil.getString("str_entity_added"));

        } else if (selectedType.equals(supplierLabel)) {
            Supplier supplier = new Supplier();
            supplier.setName(firstFieldV);
            supplier.setEmail(secondFieldV);
            supplier.setAddress(fourthFieldV);
            supplier.setPhoneNumber(thirdFieldV);
            new SupplierDAO().addSupplier(supplier);
            showAlert("Success", LanguageUtil.getString("str_entity_added"));

        } else if (selectedType.equals(productLabel)) {
            try {
                CompletableFuture<String> name = TranslateUtil.translate(firstFieldV, "en");
                CompletableFuture<String> nameFi = TranslateUtil.translate(firstFieldV, "fi");
                CompletableFuture<String> nameJa = TranslateUtil.translate(firstFieldV, "ja");
                CompletableFuture<String> nameRu = TranslateUtil.translate(firstFieldV, "ru");

                CompletableFuture<String> category = TranslateUtil.translate(secondFieldV, "en");
                CompletableFuture<String> categoryFi = TranslateUtil.translate(secondFieldV, "fi");
                CompletableFuture<String> categoryJa = TranslateUtil.translate(secondFieldV, "ja");
                CompletableFuture<String> categoryRu = TranslateUtil.translate(secondFieldV, "ru");

                CompletableFuture<String> description = TranslateUtil.translate(fourthFieldV, "en");
                CompletableFuture<String> descriptionFi = TranslateUtil.translate(fourthFieldV, "fi");
                CompletableFuture<String> descriptionJa = TranslateUtil.translate(fourthFieldV, "ja");
                CompletableFuture<String> descriptionRu = TranslateUtil.translate(fourthFieldV, "ru");

                CompletableFuture.allOf(name, nameFi, nameJa, nameRu,
                        category, categoryFi, categoryJa, categoryRu,
                        description, descriptionFi, descriptionJa, descriptionRu).join();

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

                product.setBrand(thirdFieldV);
                new ProductDAO().addProduct(product);
                showAlert("Success", LanguageUtil.getString("str_entity_added"));

            } catch (Exception e) {
                showAlert("Failed to add product", LanguageUtil.getString("str_product_add_fail"));
                e.printStackTrace();
            }
        }
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    /**
     * Closes the window and clears form fields.
     */
    @FXML
    private void handleCancel() {
        firstTextField.clear();
        secondTextField.clear();
        thirdTextField.clear();
        fourthTextField.clear();
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    /**
     * Displays an informational alert dialog.
     *
     * @param title dialog title
     * @param message dialog content message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}