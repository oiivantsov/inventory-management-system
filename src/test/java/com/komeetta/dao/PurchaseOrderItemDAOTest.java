package com.komeetta.dao;

import com.komeetta.InitDBTest;
import com.komeetta.model.*;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderItemDAOTest extends InitDBTest {
    private static ProductDAO productDAO;
    private static SupplierDAO supplierDAO;
    private static PurchaseOrderDAO purchaseOrderDAO;
    private static PurchaseOrderItemDAO purchaseOrderItemDAO;

    @BeforeAll
    static void initDAOs() {
        productDAO = new ProductDAO();
        supplierDAO = new SupplierDAO();
        purchaseOrderDAO = new PurchaseOrderDAO();
        purchaseOrderItemDAO = new PurchaseOrderItemDAO();
    }

    @BeforeEach
    void cleanUp() {
        truncate("PurchaseOrderItem", "PurchaseOrder", "Supplier", "Product");
    }

    @Test
    void addPurchaseOrderItem_ShouldPersistItem() {
        // Create supplier
        Supplier supplier = new Supplier();
        supplier.setName("Tech Supplier");
        supplierDAO.addSupplier(supplier);

        // Create product
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setBrand("Dell");
        laptop.setCategory("Electronics");
        laptop.setQuantity(0); // Initial stock is 0
        productDAO.addProduct(laptop);

        // Create purchase order
        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier);
        purchaseOrderDAO.addPurchaseOrder(purchaseOrder);

        // Create composite key
        OrderItemId itemId = new OrderItemId(purchaseOrder.getOrderId(), laptop.getProductId());

        // Create purchase order item
        PurchaseOrderItem item = new PurchaseOrderItem(purchaseOrder, laptop, 5, 500.00, 0);
        purchaseOrderItemDAO.addPurchaseOrderItem(item);

        // Fetch and verify item
        PurchaseOrderItem fetchedItem = purchaseOrderItemDAO.getPurchaseOrderItem(itemId);
        assertNotNull(fetchedItem);
        assertEquals(5, fetchedItem.getQuantity());
        assertEquals(500.00, fetchedItem.getUnitPrice(), 0.01);
    }

    @Test
    void updatePurchaseOrderItem_ShouldModifyItem() {
        // Create supplier
        Supplier supplier = new Supplier();
        supplier.setName("Tech Supplier");
        supplierDAO.addSupplier(supplier);

        // Create product
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setBrand("Dell");
        laptop.setCategory("Electronics");
        laptop.setQuantity(0);
        productDAO.addProduct(laptop);

        // Create purchase order
        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier);
        purchaseOrderDAO.addPurchaseOrder(purchaseOrder);

        // Create composite key
        OrderItemId itemId = new OrderItemId(purchaseOrder.getOrderId(), laptop.getProductId());

        // Create and persist item
        PurchaseOrderItem item = new PurchaseOrderItem(purchaseOrder, laptop, 5, 500.00, 0);
        purchaseOrderItemDAO.addPurchaseOrderItem(item);

        // Modify item
        item.setQuantity(10);
        item.setUnitPrice(450.00);
        purchaseOrderItemDAO.updatePurchaseOrderItem(item);

        // Verify update
        PurchaseOrderItem updatedItem = purchaseOrderItemDAO.getPurchaseOrderItem(itemId);
        assertEquals(10, updatedItem.getQuantity());
        assertEquals(450.00, updatedItem.getUnitPrice(), 0.01);
    }

    @Test
    void getPurchaseOrderItems_ShouldReturnItemsForOrder() {
        // Create supplier
        Supplier supplier = new Supplier();
        supplier.setName("Tech Supplier");
        supplierDAO.addSupplier(supplier);

        // Create product
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setBrand("Dell");
        laptop.setCategory("Electronics");
        laptop.setQuantity(0);
        productDAO.addProduct(laptop);

        // Create purchase order
        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier);
        purchaseOrderDAO.addPurchaseOrder(purchaseOrder);

        // Create composite keys
        OrderItemId itemId1 = new OrderItemId(purchaseOrder.getOrderId(), laptop.getProductId());

        // Create and persist item
        PurchaseOrderItem item1 = new PurchaseOrderItem(purchaseOrder, laptop, 5, 500.00, 0);
        purchaseOrderItemDAO.addPurchaseOrderItem(item1);

        // Fetch items
        List<PurchaseOrderItem> items = entityManager.createQuery(
                "SELECT p FROM PurchaseOrderItem p WHERE p.purchaseOrder = :order",
                PurchaseOrderItem.class
        ).setParameter("order", purchaseOrder).getResultList();

        assertEquals(1, items.size());
    }
}
