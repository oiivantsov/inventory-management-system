package com.komeetta.dao;

import com.komeetta.InitDBTest;
import com.komeetta.model.OrderStatus;
import com.komeetta.model.PurchaseOrder;
import com.komeetta.model.PurchaseOrderItem;
import com.komeetta.model.Supplier;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderDAOTest extends InitDBTest {

    private static PurchaseOrderDAO purchaseOrderDAO;
    private static SupplierDAO supplierDAO;

    @BeforeAll
    static void setupDAOs() {
        purchaseOrderDAO = new PurchaseOrderDAO();
        supplierDAO = new SupplierDAO();

        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setEmail("supplier@example.com");
        supplier.setPhoneNumber("123456789");
        supplier.setAddress("Supplier Address");

        supplierDAO.addSupplier(supplier);
    }

    @BeforeEach
    void cleanUp() {
        truncate("PurchaseOrder");
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

    @Test
    void testGetNumberOfPurchaseOrders() {
        Supplier supplier = supplierDAO.getSuppliers().get(0);

        PurchaseOrder order1 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 200.00);
        PurchaseOrder order2 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 300.00);
        PurchaseOrder order3 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 500.00);

        purchaseOrderDAO.addPurchaseOrder(order1);
        purchaseOrderDAO.addPurchaseOrder(order2);
        purchaseOrderDAO.addPurchaseOrder(order3);

        int numberOfOrders = purchaseOrderDAO.getPurchaseOrders().size();
        assertEquals(3, numberOfOrders);
    }

    @Test
    void testGetNumberOfPurchaseOrders_WhenNoOrders_ShouldReturnZero() {
        purchaseOrderDAO.deleteAll();
        int numberOfOrders = purchaseOrderDAO.getPurchaseOrders().size();
        assertEquals(0, numberOfOrders);
    }

    @Test
    void testGetThreeMonthOrders() {
        Supplier supplier = supplierDAO.getSuppliers().get(0);

        PurchaseOrder order1 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 200.00);
        PurchaseOrder order2 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 300.00);
        PurchaseOrder order3 = new PurchaseOrder(supplier, new Date(), OrderStatus.PENDING, 500.00);

        // older than 3 months
        LocalDate fourMonthsAgo = LocalDate.now().minusMonths(4);
        Date fourMonthsAgoDate = Date.from(fourMonthsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
        PurchaseOrder order4 = new PurchaseOrder(supplier, fourMonthsAgoDate, OrderStatus.PENDING, 100.00);

        purchaseOrderDAO.addPurchaseOrder(order1);
        purchaseOrderDAO.addPurchaseOrder(order2);
        purchaseOrderDAO.addPurchaseOrder(order3);
        purchaseOrderDAO.addPurchaseOrder(order4);

        double total = purchaseOrderDAO.getThreeMonthOrders();

        assertEquals(1000.00, total, 0.01);
    }

    @Test
    void testGetThreeMonthOrders_WhenNoOrders_ShouldReturnZero() {
        purchaseOrderDAO.deleteAll();
        double total = purchaseOrderDAO.getThreeMonthOrders();
        assertEquals(0.0, total, 0.01);
    }
}
