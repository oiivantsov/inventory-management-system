package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.Customer;
import com.komeetta.model.OrderStatus;
import com.komeetta.model.SalesOrder;
import com.komeetta.model.SalesOrderItem;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalesOrderDAOTest {

    private static SalesOrderDAO salesOrderDAO;
    private static CustomerDAO customerDAO;
    private static EntityManager entityManager;
    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUp() {
        // Load environment variables for test database
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));

        salesOrderDAO = new SalesOrderDAO();
        customerDAO = new CustomerDAO();
        emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnitTesting");
        entityManager = emf.createEntityManager();

        Customer customer = new Customer();

        // add to be able to fetch customer
        customer.setName("Test Customer");
        customer.setEmail("customer@example.com");
        customer.setPhoneNumber("987654321");
        customer.setAddress("Customer Address");

        customerDAO.addCustomer(customer);
    }

    @BeforeEach
    void beforeEach() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery("DELETE FROM SalesOrder").executeUpdate();
        transaction.commit();
    }

    @Test
    void testAddSalesOrder() {

        Customer customer = customerDAO.getCustomers().get(0);

        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);

        SalesOrder fetchedOrder = salesOrderDAO.getSalesOrder(salesOrder.getOrderId());

        assertNotNull(fetchedOrder);
        assertEquals(OrderStatus.COMPLETED, fetchedOrder.getOrderStatus());
    }

    @Test
    void testUpdateSalesOrder() {
        Customer customer = customerDAO.getCustomers().get(0);

        SalesOrder salesOrder = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 100.00);
        salesOrderDAO.addSalesOrder(salesOrder);

        SalesOrder fetchedOrder = salesOrderDAO.getSalesOrder(salesOrder.getOrderId());
        fetchedOrder.setOrderStatus(OrderStatus.COMPLETED);
        fetchedOrder.setOrderTotal(200.00);

        salesOrderDAO.updateSalesOrder(fetchedOrder);

        SalesOrder updatedOrder = salesOrderDAO.getSalesOrder(fetchedOrder.getOrderId());
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getOrderStatus());
        assertEquals(200.00, updatedOrder.getOrderTotal());
    }

    @Test
    void testDeleteSalesOrder() {
        Customer customer = customerDAO.getCustomers().get(0);

        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);

        // Fetch the managed entity before deleting
        SalesOrder salesOrderToDelete = salesOrderDAO.getSalesOrder(salesOrder.getOrderId());

        salesOrderDAO.deleteSalesOrder(salesOrderToDelete);

        SalesOrder deletedOrder = salesOrderDAO.getSalesOrder(salesOrderToDelete.getOrderId());
        assertNull(deletedOrder);
    }


    @Test
    void testGetSalesOrderWithItems() {
        Customer customer = customerDAO.getCustomers().get(0);

        SalesOrder salesOrder = new SalesOrder(customer);
        salesOrderDAO.addSalesOrder(salesOrder);

        SalesOrder fetchedOrder = salesOrderDAO.getSalesOrder(salesOrder.getOrderId());

        List<SalesOrderItem> items = salesOrder.getItems();

        assertNotNull(fetchedOrder);
        assertNotNull(fetchedOrder.getItems()); // Ensure items are fetched
    }

    @AfterAll
    static void tearDown() {
        salesOrderDAO.deleteAll();
        customerDAO.deleteAll();
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
