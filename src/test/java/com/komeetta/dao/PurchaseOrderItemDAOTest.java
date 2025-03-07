package com.komeetta.dao;

import com.komeetta.model.*;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderItemDAOTest {
    private static EntityManager entityManager;
    private static EntityManagerFactory emf;
    private static ProductDAO productDAO;
    private static SupplierDAO supplierDAO;
    private static PurchaseOrderDAO purchaseOrderDAO;
    private static PurchaseOrderItemDAO purchaseOrderItemDAO;

    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));

        emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnitTesting");
        entityManager = emf.createEntityManager();
        productDAO = new ProductDAO();
        supplierDAO = new SupplierDAO();
        purchaseOrderDAO = new PurchaseOrderDAO();
        purchaseOrderItemDAO = new PurchaseOrderItemDAO();
    }

    @BeforeEach
    void cleanUp() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery("DELETE FROM PurchaseOrderItem").executeUpdate();
        entityManager.createQuery("DELETE FROM PurchaseOrder").executeUpdate();
        entityManager.createQuery("DELETE FROM Product").executeUpdate();
        entityManager.createQuery("DELETE FROM Supplier").executeUpdate();
        transaction.commit();
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

    @AfterAll
    static void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
