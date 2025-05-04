package com.komeetta.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import java.util.Optional;

import com.komeetta.dao.CustomerDAO;
import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.SupplierDAO;
import com.komeetta.dao.PurchaseOrderDAO;
import com.komeetta.dao.SalesOrderDAO;
import com.komeetta.model.*;

/**
 * DeleteConfirmationDialog class provides a method to display a confirmation dialog
 * before deleting an item from the database and the table view.
 * It has the following methods:
 * - display: displays the confirmation dialog
 * - deleteFromDatabase: deletes the selected item from the database
 * - showDeletionErrorDialog: shows an error dialog if deletion fails
 */
public class DeleteConfirmationDialog {

    /**
     * Displays a confirmation dialog to the user before deleting an item.
     *
     * @param <T>         the type of the item
     * @param itemType    the type of the item (e.g., "Customer", "Product", etc.)
     * @param action      the action being performed (e.g., "delete")
     * @param tableView   the TableView containing the items
     * @param selectedItem the selected item to be deleted
     */
    public static <T> void display(String itemType, String action, TableView<T> tableView, T selectedItem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LanguageUtil.getString("str_confirm_title"));
        alert.setHeaderText(String.format(LanguageUtil.getString("str_confirm_header"), action));
        alert.setContentText(String.format(LanguageUtil.getString("str_confirm_content"), action, itemType));


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteFromDatabase(selectedItem);
            tableView.getItems().remove(selectedItem);
        }
    }

    /**
     * Deletes the selected item from the database.
     *
     * @param <T>         the type of the item
     * @param selectedItem the selected item to be deleted
     */
    private static <T> void deleteFromDatabase(T selectedItem) {
        try {
            if (selectedItem instanceof Customer) {
                new CustomerDAO().deleteCustomer((Customer) selectedItem);
            } else if (selectedItem instanceof Product) {
                new ProductDAO().deleteProduct((Product) selectedItem);
            } else if (selectedItem instanceof Supplier) {
                new SupplierDAO().deleteSupplier((Supplier) selectedItem);
            } else if (selectedItem instanceof PurchaseOrder) {
                new PurchaseOrderDAO().deletePurchaseOrderWithItems((PurchaseOrder) selectedItem);
            } else if (selectedItem instanceof SalesOrder) {
                new SalesOrderDAO().deleteSalesOrderWithItems((SalesOrder) selectedItem);
            }
        } catch (Exception e) {
            showDeletionErrorDialog();
            throw new RuntimeException(e);
        }
    }

    /**
     * Shows an error dialog if deletion fails.
     */
    private static void showDeletionErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(LanguageUtil.getString("str_delete_error_title"));
        alert.setHeaderText(LanguageUtil.getString("str_delete_error_header"));
        alert.setContentText(LanguageUtil.getString("str_delete_error_content"));
        alert.showAndWait();
    }

}
