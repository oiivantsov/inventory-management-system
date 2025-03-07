package com.komeetta.model;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderTest {
    private static EntityManager entityManager;
    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));

        emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnitTesting");
        entityManager = emf.createEntityManager();
    }

    @BeforeEach
    void cleanUp() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery("DELETE FROM PurchaseOrderItem").executeUpdate();
        entityManager.createQuery("DELETE FROM PurchaseOrder").executeUpdate();
        entityManager.createQuery("DELETE FROM Supplier").executeUpdate();
        transaction.commit();
    }

    @Test
    void testPurchaseOrderEntityCreation() {
        Supplier supplier = new Supplier();
        supplier.setName("Tech Supplier");
        supplier.setEmail("supplier@mail.com");
        supplier.setPhoneNumber("987654321");
        supplier.setAddress("456 Elm St");

        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 500.00);

        assertEquals(supplier, purchaseOrder.getSupplier());
        assertEquals(OrderStatus.PENDING, purchaseOrder.getOrderStatus());
        assertEquals(500.00, purchaseOrder.getOrderTotal());
    }

    @Test
    void testPurchaseOrderPersistence() {
        Supplier supplier = new Supplier();
        supplier.setName("Supplier A");
        supplier.setEmail("supplierA@example.com");
        supplier.setPhoneNumber("123456789");
        supplier.setAddress("789 Supplier St");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(supplier);
        transaction.commit();

        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier, new Date(), OrderStatus.IN_PROGRESS, 250.00);

        transaction.begin();
        entityManager.persist(purchaseOrder);
        transaction.commit();

        assertNotNull(purchaseOrder.getOrderId()); // Ensure ID is assigned
        PurchaseOrder retrievedOrder = entityManager.find(PurchaseOrder.class, purchaseOrder.getOrderId());
        assertNotNull(retrievedOrder);
        assertEquals(OrderStatus.IN_PROGRESS, retrievedOrder.getOrderStatus());
        assertEquals(250.00, retrievedOrder.getOrderTotal());
    }

    @Test
    void testUpdatePurchaseOrder() {
        Supplier supplier = new Supplier();
        supplier.setName("Supplier B");
        supplier.setEmail("supplierB@example.com");
        supplier.setPhoneNumber("111222333");
        supplier.setAddress("101 Supplier Ave");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(supplier);
        transaction.commit();

        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 180.00);

        transaction.begin();
        entityManager.persist(purchaseOrder);
        transaction.commit();

        // Modify purchase order
        transaction.begin();
        purchaseOrder.setOrderStatus(OrderStatus.COMPLETED);
        purchaseOrder.setOrderTotal(300.00);
        entityManager.merge(purchaseOrder);
        transaction.commit();

        // Verify update
        PurchaseOrder updatedOrder = entityManager.find(PurchaseOrder.class, purchaseOrder.getOrderId());
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getOrderStatus());
        assertEquals(300.00, updatedOrder.getOrderTotal());
    }

    @Test
    void testDeletePurchaseOrder() {
        Supplier supplier = new Supplier();
        supplier.setName("Supplier C");
        supplier.setEmail("supplierC@example.com");
        supplier.setPhoneNumber("555666777");
        supplier.setAddress("202 Market St");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(supplier);
        transaction.commit();

        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 500.00);

        transaction.begin();
        entityManager.persist(purchaseOrder);
        transaction.commit();

        // Delete purchase order
        transaction.begin();
        PurchaseOrder orderToDelete = entityManager.find(PurchaseOrder.class, purchaseOrder.getOrderId());
        entityManager.remove(orderToDelete);
        transaction.commit();

        // Verify deletion
        PurchaseOrder deletedOrder = entityManager.find(PurchaseOrder.class, purchaseOrder.getOrderId());
        assertNull(deletedOrder);
    }

    @AfterAll
    static void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
