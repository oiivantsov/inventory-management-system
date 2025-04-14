/**
 * StatsHandler is responsible for handling statistical data presentation and export.
 * It updates order and revenue statistics displayed in the dashboard and handles exporting
 * them to a CSV file.
 */
package com.komeetta.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import statistics.DashboardStats;
import statistics.Stat;

import java.util.Optional;

public class StatsHandler {

    private final DashboardStats dashboardStats;

    /**
     * Constructs a new StatsHandler and initializes the DashboardStats instance.
     */
    public StatsHandler() {
        this.dashboardStats = new DashboardStats();
    }

    /**
     * Refreshes the order and revenue statistics in their respective tables.
     * Retrieves updated stats from the DashboardStats class and populates the tables.
     *
     * @param ordersTable TableView for order-related statistics
     * @param revenueTable TableView for revenue-related statistics
     */
    public void refreshStatsTables(TableView<Stat> ordersTable, TableView<Stat> revenueTable) {
        dashboardStats.updateStats();

        ObservableList<Stat> orderStats = FXCollections.observableArrayList(
                new Stat(LanguageUtil.getString("str_label_total_sales_orders"), String.valueOf(dashboardStats.getTotalSalesOrders())),
                new Stat(LanguageUtil.getString("str_label_total_purchase_orders"), String.valueOf(dashboardStats.getTotalPurchaseOrders())),
                new Stat(LanguageUtil.getString("str_label_difference"), String.valueOf(dashboardStats.getOrderDifference()))
        );

        ObservableList<Stat> revenueStats = FXCollections.observableArrayList(
                new Stat(LanguageUtil.getString("str_label_total_revenue"), String.format("%.2f", dashboardStats.getTotalRevenue())),
                new Stat(LanguageUtil.getString("str_label_last_three_months_revenue"), String.format("%.2f", dashboardStats.getLastThreeMonthsRevenue()))
        );

        ordersTable.setItems(orderStats);
        revenueTable.setItems(revenueStats);
    }

    /**
     * Prompts the user to enter a filename and exports the statistics as a CSV file.
     * Displays an error alert if the filename is invalid.
     *
     * @param statsLabel a Label (optional usage) for attaching messages or localization
     */
    public void handleStatsExport(Label statsLabel) {
        TextInputDialog dialog = new TextInputDialog("stats_report");
        dialog.setTitle(LanguageUtil.getString("str_dialog_title_save_csv"));
        dialog.setHeaderText(LanguageUtil.getString("str_dialog_header_csv"));
        dialog.setContentText(LanguageUtil.getString("str_dialog_content_csv"));

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(filename -> {
            if (filename != null && !filename.isEmpty() && filename.matches("^[a-zA-Z0-9_]*$")) {
                dashboardStats.createCvsStatsFile(filename);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(LanguageUtil.getString("str_error_title_invalid_filename"));
                alert.setHeaderText(LanguageUtil.getString("str_error_header_invalid_filename"));
                alert.setContentText(LanguageUtil.getString("str_error_content_invalid_filename"));
                alert.showAndWait();
            }
        });
    }
}
