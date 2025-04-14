package com.komeetta.dao;

import com.komeetta.InitDBTest;
import com.komeetta.model.*;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalesOrderItemDAOTest extends InitDBTest {
    private static ProductDAO productDAO;
    private static CustomerDAO customerDAO;
    private static SalesOrderDAO salesOrderDAO;
    private static SalesOrderItemDAO salesOrderItemDAO;

    @BeforeAll
    static void initDAOs() {
        productDAO = new ProductDAO();
        customerDAO = new CustomerDAO();
        salesOrderDAO = new SalesOrderDAO();
        salesOrderItemDAO = new SalesOrderItemDAO();
    }

    @BeforeEach
    void cleanUp() {
        truncate("SalesOrderItem", "SalesOrder",  "Product", "Customer");
    }

    @Test
    void addSalesOrderItem_ShouldPersistItem() {
        // Create customer
        Customer customer = new Customer();
        customer.setName("John Doe");
        customerDAO.addCustomer(customer);

        // Create product
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setBrand("Dell");
        laptop.setCategory("Electronics");
        laptop.setQuantity(10); // Initial stock is 10
        productDAO.addProduct(laptop);

        // Create sales order
        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);

        // Create composite key
        OrderItemId itemId = new OrderItemId(salesOrder.getOrderId(), laptop.getProductId());

        // Create sales order item
        SalesOrderItem item = new SalesOrderItem(salesOrder, laptop, 2, 700.00, 0);
        salesOrderItemDAO.addSalesOrderItem(item);

        // Fetch and verify item
        SalesOrderItem fetchedItem = salesOrderItemDAO.getSalesOrderItem(itemId);
        assertNotNull(fetchedItem);
        assertEquals(2, fetchedItem.getQuantity());
        assertEquals(700.00, fetchedItem.getUnitPrice(), 0.01);
    }

    @Test
    void updateSalesOrderItem_ShouldModifyItem() {
        // Create customer
        Customer customer = new Customer();
        customer.setName("John Doe");
        customerDAO.addCustomer(customer);

        // Create product
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setBrand("Dell");
        laptop.setCategory("Electronics");
        laptop.setQuantity(10);
        productDAO.addProduct(laptop);

        // Create sales order
        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);

        // Create composite key
        OrderItemId itemId = new OrderItemId(salesOrder.getOrderId(), laptop.getProductId());

        // Create and persist item
        SalesOrderItem item = new SalesOrderItem(salesOrder, laptop, 2, 700.00, 0);
        salesOrderItemDAO.addSalesOrderItem(item);

        // Modify item
        item.setQuantity(5);
        item.setUnitPrice(650.00);
        salesOrderItemDAO.updateSalesOrderItem(item);

        // Verify update
        SalesOrderItem updatedItem = salesOrderItemDAO.getSalesOrderItem(itemId);
        assertEquals(5, updatedItem.getQuantity());
        assertEquals(650.00, updatedItem.getUnitPrice(), 0.01);
    }

    @Test
    void deleteSalesOrderItem_ShouldRemoveItem() {
        // Create customer
        Customer customer = new Customer();
        customer.setName("John Doe");
        customerDAO.addCustomer(customer);

        // Create product
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setBrand("Dell");
        laptop.setCategory("Electronics");
        laptop.setQuantity(10);
        productDAO.addProduct(laptop);

        // Create sales order
        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);

        // Create composite key
        OrderItemId itemId = new OrderItemId(salesOrder.getOrderId(), laptop.getProductId());

        // Create and persist item
        SalesOrderItem item = new SalesOrderItem(salesOrder, laptop, 2, 700.00, 0);
        salesOrderItemDAO.addSalesOrderItem(item);

        // Delete item
        salesOrderItemDAO.deleteSalesOrderItem(itemId);

        // Verify deletion
        SalesOrderItem deletedItem = salesOrderItemDAO.getSalesOrderItem(itemId);
        assertNull(deletedItem);
    }

    @Test
    void getSalesOrderItems_ShouldReturnItemsForOrder() {
        // Create customer
        Customer customer = new Customer();
        customer.setName("John Doe");
        customerDAO.addCustomer(customer);

        // Create product
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setBrand("Dell");
        laptop.setCategory("Electronics");
        laptop.setQuantity(10);
        productDAO.addProduct(laptop);

        // Create sales order
        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);

        // Create composite key
        OrderItemId itemId = new OrderItemId(salesOrder.getOrderId(), laptop.getProductId());

        // Create and persist item
        SalesOrderItem item = new SalesOrderItem(salesOrder, laptop, 2, 700.00, 0);
        salesOrderItemDAO.addSalesOrderItem(item);

        // Fetch items
        List<SalesOrderItem> items = salesOrderItemDAO.getSalesOrderItems(salesOrder.getOrderId());

        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getQuantity());
    }
}
