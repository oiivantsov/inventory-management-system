package com.komeetta.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.util.List;
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
     */
    public static <T> void display(String itemType, String action, TableView<T> tableView, T selectedItem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LanguageUtil.getString("str_confirm_title"));
        alert.setHeaderText(String.format(LanguageUtil.getString("str_confirm_header"), action));
        alert.setContentText(String.format(LanguageUtil.getString("str_confirm_content"), action, itemType));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deletedSuccessfully = deleteFromDatabase(selectedItem);
            if (deletedSuccessfully) {
                tableView.getItems().remove(selectedItem);
            }
        }
    }

    /**
     * Deletes the selected item from the database.
     */
    private static <T> boolean deleteFromDatabase(T selectedItem) {
        try {
            if (selectedItem instanceof Product) {
                Product product = (Product) selectedItem;
                ProductDAO productDAO = new ProductDAO();

                List<Integer> salesOrderIds = productDAO.getSalesOrderIdsByProductId(product.getProductId());
                List<Integer> purchaseOrderIds = productDAO.getPurchaseOrderIdsByProductId(product.getProductId());

                if (!salesOrderIds.isEmpty() || !purchaseOrderIds.isEmpty()) {
                    StringBuilder details = new StringBuilder();

                    if (!salesOrderIds.isEmpty()) {
                        details.append(LanguageUtil.getString("str_sales_orders"))
                                .append(": ")
                                .append(salesOrderIds)
                                .append("\n");
                    }

                    if (!purchaseOrderIds.isEmpty()) {
                        details.append(LanguageUtil.getString("str_purchase_orders"))
                                .append(": ")
                                .append(purchaseOrderIds)
                                .append("\n");
                    }

                    showProductUsedInOrdersDialog(details.toString());
                    return false;
                }

                productDAO.deleteProduct(product);
                return true;

            } else if (selectedItem instanceof Customer) {
                new CustomerDAO().deleteCustomer((Customer) selectedItem);
            } else if (selectedItem instanceof Supplier) {
                new SupplierDAO().deleteSupplier((Supplier) selectedItem);
            } else if (selectedItem instanceof PurchaseOrder) {
                new PurchaseOrderDAO().deletePurchaseOrderWithItems((PurchaseOrder) selectedItem);
            } else if (selectedItem instanceof SalesOrder) {
                new SalesOrderDAO().deleteSalesOrderWithItems((SalesOrder) selectedItem);
            }
            return true;
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

    /**
     * Shows a localized warning dialog if the product is used in existing orders.
     */
    private static void showProductUsedInOrdersDialog(String details) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(LanguageUtil.getString("str_cannot_delete_product_title"));
        alert.setHeaderText(LanguageUtil.getString("str_product_used_header"));
        alert.setContentText(String.format(LanguageUtil.getString("str_product_used_details"), details));
        alert.showAndWait();
    }

}
