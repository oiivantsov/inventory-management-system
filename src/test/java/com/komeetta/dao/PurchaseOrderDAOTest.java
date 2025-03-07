package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.OrderStatus;
import com.komeetta.model.PurchaseOrder;
import com.komeetta.model.PurchaseOrderItem;
import com.komeetta.model.Supplier;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderDAOTest {

    private static PurchaseOrderDAO purchaseOrderDAO;
    private static SupplierDAO supplierDAO;
    private static EntityManager entityManager;
    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUp() {
        // Load environment variables for test database
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));

        purchaseOrderDAO = new PurchaseOrderDAO();
        supplierDAO = new SupplierDAO();
        emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnitTesting");
        entityManager = emf.createEntityManager();

        Supplier supplier = new Supplier();

        // Add supplier for testing
        supplier.setName("Test Supplier");
        supplier.setEmail("supplier@example.com");
        supplier.setPhoneNumber("123456789");
        supplier.setAddress("Supplier Address");

        supplierDAO.addSupplier(supplier);
    }

    @BeforeEach
    void beforeEach() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery("DELETE FROM PurchaseOrder").executeUpdate();
        transaction.commit();
    }

    @Test
    void testAddPurchaseOrder() {
        Supplier supplier = supplierDAO.getSuppliers().get(0);

        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier);
        purchaseOrderDAO.addPurchaseOrder(purchaseOrder);

        PurchaseOrder fetchedOrder = purchaseOrderDAO.getPurchaseOrder(purchaseOrder.getOrderId());

        assertNotNull(fetchedOrder);
    }

    @Test
    void testUpdatePurchaseOrder() {
        Supplier supplier = supplierDAO.getSuppliers().get(0);

        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 150.00);
        purchaseOrderDAO.addPurchaseOrder(purchaseOrder);

        PurchaseOrder fetchedOrder = purchaseOrderDAO.getPurchaseOrder(purchaseOrder.getOrderId());
        fetchedOrder.setOrderStatus(OrderStatus.COMPLETED);
        fetchedOrder.setOrderTotal(300.00);

        purchaseOrderDAO.updatePurchaseOrder(fetchedOrder);

        PurchaseOrder updatedOrder = purchaseOrderDAO.getPurchaseOrder(fetchedOrder.getOrderId());
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getOrderStatus());
        assertEquals(300.00, updatedOrder.getOrderTotal());
    }

    @Test
    void testDeletePurchaseOrder() {
        Supplier supplier = supplierDAO.getSuppliers().get(0);

        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier);
        purchaseOrderDAO.addPurchaseOrder(purchaseOrder);

        // Fetch the managed entity before deleting
        PurchaseOrder purchaseOrderToDelete = purchaseOrderDAO.getPurchaseOrder(purchaseOrder.getOrderId());

        purchaseOrderDAO.deletePurchaseOrder(purchaseOrderToDelete);

        PurchaseOrder deletedOrder = purchaseOrderDAO.getPurchaseOrder(purchaseOrderToDelete.getOrderId());
        assertNull(deletedOrder);
    }

    @Test
    void testGetPurchaseOrderWithItems() {
        Supplier supplier = supplierDAO.getSuppliers().get(0);

        PurchaseOrder purchaseOrder = new PurchaseOrder(supplier);
        purchaseOrderDAO.addPurchaseOrder(purchaseOrder);

        PurchaseOrder fetchedOrder = purchaseOrderDAO.getPurchaseOrder(purchaseOrder.getOrderId());

        List<PurchaseOrderItem> items = purchaseOrder.getItems();

        assertNotNull(fetchedOrder);
        assertNotNull(fetchedOrder.getItems()); // Ensure items are fetched
    }

    @Test
    void testGetAllPurchaseOrders() {
        Supplier supplier = supplierDAO.getSuppliers().get(0);

        PurchaseOrder order1 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 150.00);
        PurchaseOrder order2 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 300.00);

        purchaseOrderDAO.addPurchaseOrder(order1);
        purchaseOrderDAO.addPurchaseOrder(order2);

        List<PurchaseOrder> orders = purchaseOrderDAO.getPurchaseOrders();

        assertEquals(2, orders.size());
    }

    @Test
    void testGetTotalPurchaseOrders() {
        Supplier supplier = supplierDAO.getSuppliers().get(0);

        PurchaseOrder order1 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 150.00);
        PurchaseOrder order2 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 300.00);

        purchaseOrderDAO.addPurchaseOrder(order1);
        purchaseOrderDAO.addPurchaseOrder(order2);

        double total = purchaseOrderDAO.getTotalPurchaseOrders();

        assertEquals(450.00, total, 0.01);
    }


    @Test
    void testGetTotalPurchaseOrdersBySupplier() {
        Supplier supplier = supplierDAO.getSuppliers().get(0);

        PurchaseOrder order1 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 200.00);
        PurchaseOrder order2 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 300.00);

        purchaseOrderDAO.addPurchaseOrder(order1);
        purchaseOrderDAO.addPurchaseOrder(order2);

        double totalBySupplier = purchaseOrderDAO.getTotalPurchaseOrdersBySupplier(supplier.getSupplierId());

        assertEquals(500.00, totalBySupplier, 0.01);
    }

    @Test
    void testGetPurchaseOrder_NonExistent_ShouldReturnNull() {
        PurchaseOrder nonExistentOrder = purchaseOrderDAO.getPurchaseOrder(99999);
        assertNull(nonExistentOrder);
    }

    @Test
    void testUpdatePurchaseOrder_NonExistent_ShouldThrowException() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderId(99999); // Fake order ID

        Exception exception = assertThrows(RuntimeException.class, () -> {
            purchaseOrderDAO.updatePurchaseOrder(purchaseOrder);
        });

        assertTrue(exception.getMessage().contains("Failed to update purchase order"),
                "Updating a non-existent purchase order should throw an error.");
    }

    @Test
    void testDeletePurchaseOrder_NonExistent_ShouldNotThrowException() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderId(99999); // Fake order ID

        assertDoesNotThrow(() -> purchaseOrderDAO.deletePurchaseOrder(purchaseOrder),
                "Deleting a non-existent purchase order should not throw an exception.");
    }

    @Test
    void testGetTotalPurchaseOrders_WhenNoOrders_ShouldReturnZero() {
        purchaseOrderDAO.deleteAll();
        double total = purchaseOrderDAO.getTotalPurchaseOrders();
        assertEquals(0.0, total, 0.01);
    }

    @Test
    void testGetTotalPurchaseOrdersBySupplier_WhenNoOrders_ShouldReturnZero() {
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplierDAO.addSupplier(supplier);

        double total = purchaseOrderDAO.getTotalPurchaseOrdersBySupplier(supplier.getSupplierId());
        assertEquals(0.0, total, 0.01);
    }

    @AfterAll
    static void tearDown() {
        purchaseOrderDAO.deleteAll();
        supplierDAO.deleteAll();
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
