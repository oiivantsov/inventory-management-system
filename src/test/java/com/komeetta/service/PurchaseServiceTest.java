package com.komeetta.service;

import com.komeetta.InitDBTest;
import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.PurchaseOrderDAO;
import com.komeetta.dao.PurchaseOrderItemDAO;
import com.komeetta.dao.SupplierDAO;
import com.komeetta.model.Product;
import com.komeetta.model.PurchaseOrder;
import com.komeetta.model.PurchaseOrderItem;
import com.komeetta.model.Supplier;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseServiceTest extends InitDBTest {
    private static ProductDAO productDAO;
    private static PurchaseOrderDAO purchaseOrderDAO;
    private static PurchaseOrderItemDAO purchaseOrderItemDAO;
    private static PurchaseService purchaseService;
    private static SupplierDAO supplierDAO;

    @BeforeAll
    static void setUp() {
        productDAO = new ProductDAO();
        supplierDAO = new SupplierDAO();
        purchaseOrderDAO = new PurchaseOrderDAO();
        purchaseOrderItemDAO = new PurchaseOrderItemDAO();
        purchaseService = new PurchaseService();
    }

    @BeforeEach
    void cleanUp() {
        truncate("PurchaseOrderItem", "PurchaseOrder", "Product");
    }

    @Test
    void processPurchaseOrder_ShouldUpdateStockAndTotal() {

        // Create supplier
        Supplier supplier = new Supplier();
        supplier.setName("Tech Supplier");
        supplier.setEmail("supplier@mail.com");
        supplier.setPhoneNumber("987654321");
        supplier.setAddress("456 Elm St");
        supplierDAO.addSupplier(supplier);

        // Create products
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setBrand("Dell");
        laptop.setCategory("Electronics");
        laptop.setQuantity(0); // Initial stock is 0
        productDAO.addProduct(laptop);

        // Create a purchase order
        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier);
        purchaseOrderDAO.addPurchaseOrder(purchaseOrder);

        PurchaseOrderItem item = new PurchaseOrderItem(purchaseOrder, laptop, 10, 500.00, 0);

        // Process purchase order
        purchaseService.processPurchaseOrder(purchaseOrder, List.of(item));

        // Assert
        Product updatedProduct = productDAO.getProductById(laptop.getProductId());
        assertEquals(10, updatedProduct.getQuantity());

        PurchaseOrder updatedOrder = purchaseOrderDAO.getPurchaseOrder(purchaseOrder.getOrderId());
        assertEquals(5000.0, updatedOrder.getOrderTotal(), 0.01); // 500.00 * 10
    }
}

