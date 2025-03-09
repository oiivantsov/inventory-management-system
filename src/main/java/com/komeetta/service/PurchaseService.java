package com.komeetta.service;

import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.PurchaseOrderDAO;
import com.komeetta.dao.PurchaseOrderItemDAO;
import com.komeetta.model.*;

import java.util.List;

public class PurchaseService {

    private final PurchaseOrderItemDAO purchaseOrderItemDAO = new PurchaseOrderItemDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();

    public void processPurchaseOrder(PurchaseOrder purchaseOrder, List<PurchaseOrderItem> items) {

        for (PurchaseOrderItem item : items) {
            // step 1: add purchase order item to the database
            purchaseOrderItemDAO.addPurchaseOrderItem(item);

            // step 2: update product stock
            Product product = item.getProduct();
            int newStock = product.getQuantity() + item.getQuantity();
            product.setQuantity(newStock);
            productDAO.updateProduct(product);

            // step 3: update order total
            double newTotal = purchaseOrder.getOrderTotal() + (item.getUnitPrice() * item.getQuantity() * (1 - item.getSale() / 100));
            purchaseOrder.setOrderTotal(newTotal);
            purchaseOrderDAO.updatePurchaseOrder(purchaseOrder);
        }

        System.out.println("Purchase order processed successfully!");
    }
}
