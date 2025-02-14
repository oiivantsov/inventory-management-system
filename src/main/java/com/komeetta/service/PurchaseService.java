package com.komeetta.service;

import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.PurchaseOrderDAO;
import com.komeetta.dao.PurchaseOrderItemDAO;
import com.komeetta.model.*;

import java.util.List;

public class PurchaseService {

    private final PurchaseOrderItemDAO purchaseOrderItemDAO = new PurchaseOrderItemDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public void processPurchaseOrder(PurchaseOrder purchaseOrder, List<PurchaseOrderItem> items) {

        for (PurchaseOrderItem item : items) {
            // step 1: add purchase order item to the database
            purchaseOrderItemDAO.addPurchaseOrderItem(item);

            // step 2: update product stock
            Product product = item.getProduct();
            int newStock = product.getStockQuantity() + item.getQuantity();
            product.setStockQuantity(newStock);
            productDAO.updateProduct(product);

            // step 3: update order total
            double newTotal = purchaseOrder.getOrderTotal() + (item.getUnitPrice() * item.getQuantity() * (1 - item.getSale()));
            purchaseOrder.setOrderTotal(newTotal);
        }

        System.out.println("Purchase order processed successfully!");
    }
}
