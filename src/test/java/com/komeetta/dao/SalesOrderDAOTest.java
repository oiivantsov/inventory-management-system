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

import java.time.LocalDate;
import java.time.ZoneId;
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

    @Test
    void testGetAllSalesOrders() {
        Customer customer = customerDAO.getCustomers().get(0);

        SalesOrder order1 = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 200.00);
        SalesOrder order2 = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 500.00);

        salesOrderDAO.addSalesOrder(order1);
        salesOrderDAO.addSalesOrder(order2);

        List<SalesOrder> orders = salesOrderDAO.getSalesOrders();

        assertEquals(2, orders.size());
    }


    @Test
    void testGetTotalSalesOrders() {
        Customer customer = customerDAO.getCustomers().get(0);

        SalesOrder order1 = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 300.00);
        SalesOrder order2 = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 500.00);

        salesOrderDAO.addSalesOrder(order1);
        salesOrderDAO.addSalesOrder(order2);

        double total = salesOrderDAO.getTotalSaleOrders();

        assertEquals(800.00, total, 0.01);
    }

    @Test
    void testGetSalesOrder_NonExistent_ShouldReturnNull() {
        SalesOrder nonExistentOrder = salesOrderDAO.getSalesOrder(99999);
        assertNull(nonExistentOrder);
    }

    @Test
    void testDeleteSalesOrder_NonExistent_ShouldNotThrowException() {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderId(99999); // Fake order ID

        assertDoesNotThrow(() -> salesOrderDAO.deleteSalesOrder(salesOrder),
                "Deleting a non-existent order should not throw an exception.");
    }

    @Test
    void testGetTotalSalesOrders_WhenNoOrders_ShouldReturnZero() {
        salesOrderDAO.deleteAll();
        double total = salesOrderDAO.getTotalSaleOrders();
        assertEquals(0.0, total, 0.01);
    }

    @Test
    void testGetNumberOfSalesOrders() {
        Customer customer = customerDAO.getCustomers().get(0);

        SalesOrder order1 = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 300.00);
        SalesOrder order2 = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 500.00);

        salesOrderDAO.addSalesOrder(order1);
        salesOrderDAO.addSalesOrder(order2);

        int total = salesOrderDAO.getNumberOfSalesOrders();

        assertEquals(2, total);
    }

    @Test
    void testGetThreeMonthOrders() {
        Customer customer = customerDAO.getCustomers().get(0);

        SalesOrder order1 = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 300.00);
        SalesOrder order2 = new SalesOrder(customer, new Date(), OrderStatus.PENDING, 500.00);

        // Add a sales order older than 3 months
        LocalDate fourMonthsAgo = LocalDate.now().minusMonths(4);
        Date fourMonthsAgoDate = Date.from(fourMonthsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
        SalesOrder order3 = new SalesOrder(customer, fourMonthsAgoDate, OrderStatus.PENDING, 100.00);

        salesOrderDAO.addSalesOrder(order1);
        salesOrderDAO.addSalesOrder(order2);
        salesOrderDAO.addSalesOrder(order3);

        double total = salesOrderDAO.getThreeMonthOrders();

        assertEquals(800.00, total, 0.01);
    }

    @Test
    void testGetThreeMonthOrders_WhenNoOrders_ShouldReturnZero() {
        salesOrderDAO.deleteAll();
        double total = salesOrderDAO.getThreeMonthOrders();
        assertEquals(0.0, total, 0.01);
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
