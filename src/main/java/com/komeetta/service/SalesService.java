package com.komeetta.service;

import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.SalesOrderDAO;
import com.komeetta.dao.SalesOrderItemDAO;
import com.komeetta.model.*;

import java.util.List;

public class SalesService {

    private final SalesOrderItemDAO salesOrderItemDAO = new SalesOrderItemDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final SalesOrderDAO salesOrderDAO = new SalesOrderDAO();

    public void processSalesOrder(SalesOrder salesOrder, List<SalesOrderItem> items) {
        // step 0: check stock availability
        for (SalesOrderItem item : items) {
            Product product = item.getProduct();
            System.out.println("Stock level for product " + product.getName() + ": " + product.getStockQuantity());
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }
        }

        for (SalesOrderItem item : items) {
            // step 1: add sales order item to the database
            salesOrderItemDAO.addSalesOrderItem(item);

            // step 2: update product stock
            Product product = item.getProduct();
            int newStock = product.getStockQuantity() - item.getQuantity();
            product.setStockQuantity(newStock);
            productDAO.updateProduct(product);

            // step 3: update order total
            double newTotal = salesOrder.getOrderTotal() + (item.getUnitPrice() * item.getQuantity() * (1 - item.getSale()));
            salesOrder.setOrderTotal(newTotal);
            salesOrderDAO.updateSalesOrder(salesOrder);
        }

        System.out.println("Sales order processed successfully!");
    }
}
