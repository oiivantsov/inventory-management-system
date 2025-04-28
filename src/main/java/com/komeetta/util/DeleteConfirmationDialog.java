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

public class DeleteConfirmationDialog {

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

    private static void showDeletionErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(LanguageUtil.getString("str_delete_error_title"));
        alert.setHeaderText(LanguageUtil.getString("str_delete_error_header"));
        alert.setContentText(LanguageUtil.getString("str_delete_error_content"));
        alert.showAndWait();
    }

}
