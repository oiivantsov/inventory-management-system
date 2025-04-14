package com.komeetta.model;

import com.komeetta.InitDBTest;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SalesOrderTest extends InitDBTest {

    @BeforeEach
    void cleanUp() {
        truncate("PurchaseOrderItem", "SalesOrderItem", "PurchaseOrder", "SalesOrder", "Customer");
    }

    @Test
    void testSalesOrderEntityCreation() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setPhoneNumber("123456789");
        customer.setAddress("123 Main St");

        SalesOrder salesOrder = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 100.00);

        assertEquals(customer, salesOrder.getCustomer());
        assertEquals(OrderStatus.PENDING, salesOrder.getOrderStatus());
        assertEquals(100.00, salesOrder.getOrderTotal());
    }

    @Test
    void testSalesOrderPersistence() {
        Customer customer = new Customer();
        customer.setName("Jane Doe");
        customer.setEmail("jane@example.com");
        customer.setPhoneNumber("987654321");
        customer.setAddress("456 Elm St");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(customer);
        transaction.commit();

        SalesOrder salesOrder = new SalesOrder(customer, new Date(), OrderStatus.IN_PROGRESS, 250.00);

        transaction.begin();
        entityManager.persist(salesOrder);
        transaction.commit();

        assertNotNull(salesOrder.getOrderId()); // Ensure ID is assigned
        SalesOrder retrievedOrder = entityManager.find(SalesOrder.class, salesOrder.getOrderId());
        assertNotNull(retrievedOrder);
        assertEquals(OrderStatus.IN_PROGRESS, retrievedOrder.getOrderStatus());
        assertEquals(250.00, retrievedOrder.getOrderTotal());
    }

    @Test
    void testUpdateSalesOrder() {
        Customer customer = new Customer();
        customer.setName("Mark Smith");
        customer.setEmail("mark@example.com");
        customer.setPhoneNumber("111222333");
        customer.setAddress("789 Oak St");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(customer);
        transaction.commit();

        SalesOrder salesOrder = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 180.00);

        transaction.begin();
        entityManager.persist(salesOrder);
        transaction.commit();

        // Modify sales order
        transaction.begin();
        salesOrder.setOrderStatus(OrderStatus.COMPLETED);
        salesOrder.setOrderTotal(300.00);
        entityManager.merge(salesOrder);
        transaction.commit();

        // Verify update
        SalesOrder updatedOrder = entityManager.find(SalesOrder.class, salesOrder.getOrderId());
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getOrderStatus());
        assertEquals(300.00, updatedOrder.getOrderTotal());
    }

    @Test
    void testDeleteSalesOrder() {
        Customer customer = new Customer();
        customer.setName("Alice Brown");
        customer.setEmail("alice@example.com");
        customer.setPhoneNumber("555666777");
        customer.setAddress("101 Pine St");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(customer);
        transaction.commit();

        SalesOrder salesOrder = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 500.00);

        transaction.begin();
        entityManager.persist(salesOrder);
        transaction.commit();

        // Delete sales order
        transaction.begin();
        SalesOrder orderToDelete = entityManager.find(SalesOrder.class, salesOrder.getOrderId());
        entityManager.remove(orderToDelete);
        transaction.commit();

        // Verify deletion
        SalesOrder deletedOrder = entityManager.find(SalesOrder.class, salesOrder.getOrderId());
        assertNull(deletedOrder);
    }
}
