package com.komeetta.service;

import com.komeetta.dao.ProductDAO;
import com.komeetta.dao.SalesOrderDAO;
import com.komeetta.dao.SalesOrderItemDAO;
import com.komeetta.dao.CustomerDAO;
import com.komeetta.model.Product;
import com.komeetta.model.SalesOrder;
import com.komeetta.model.SalesOrderItem;
import com.komeetta.model.Customer;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalesServiceTest {
    private static EntityManager entityManager;
    private static EntityManagerFactory emf;
    private static ProductDAO productDAO;
    private static SalesOrderDAO salesOrderDAO;
    private static SalesOrderItemDAO salesOrderItemDAO;
    private static SalesService salesService;
    private static CustomerDAO customerDAO;

    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));

        emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnitTesting");
        entityManager = emf.createEntityManager();
        productDAO = new ProductDAO();
        customerDAO = new CustomerDAO();
        salesOrderDAO = new SalesOrderDAO();
        salesOrderItemDAO = new SalesOrderItemDAO();
        salesService = new SalesService();
    }

    @BeforeEach
    void cleanUp() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery("DELETE FROM SalesOrderItem").executeUpdate();
        entityManager.createQuery("DELETE FROM SalesOrder").executeUpdate();
        entityManager.createQuery("DELETE FROM Product").executeUpdate();
        transaction.commit();
    }

    @Test
    void processSalesOrder_ShouldUpdateStockAndTotal() {
        // Create customer
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john@mail.com");
        customer.setPhoneNumber("123456789");
        customer.setAddress("123 Main St");
        customerDAO.addCustomer(customer);

        // Create products
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setBrand("Dell");
        laptop.setCategory("Electronics");
        laptop.setQuantity(10); // Initial stock is 10
        productDAO.addProduct(laptop);

        // Create a sales order
        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);

        SalesOrderItem item = new SalesOrderItem(salesOrder, laptop, 2, 700.00, 0);

        // Process sales order
        salesService.processSalesOrder(salesOrder, List.of(item));

        // Assert
        Product updatedProduct = productDAO.getProductById(laptop.getProductId());
        assertEquals(8, updatedProduct.getQuantity());

        SalesOrder updatedOrder = salesOrderDAO.getSalesOrder(salesOrder.getOrderId());
        assertEquals(1400.0, updatedOrder.getOrderTotal(), 0.01); // 700.00 * 2
    }

    @Test
    void processSalesOrder_ShouldThrowExceptionWhenStockInsufficient() {
        // Create customer
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john@mail.com");
        customer.setPhoneNumber("123456789");
        customer.setAddress("123 Main St");
        customerDAO.addCustomer(customer);

        // Create products
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setBrand("Dell");
        laptop.setCategory("Electronics");
        laptop.setQuantity(1); // Only 1 in stock
        productDAO.addProduct(laptop);

        // Create a sales order
        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);

        SalesOrderItem item = new SalesOrderItem(salesOrder, laptop, 2, 700.00, 0); // Requesting 2 when only 1 is in stock

        // Assert that exception is thrown due to insufficient stock
        Exception exception = assertThrows(RuntimeException.class, () -> {
            salesService.processSalesOrder(salesOrder, List.of(item));
        });

        assertTrue(exception.getMessage().contains("Not enough stock for product"));
    }

    @AfterAll
    static void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
