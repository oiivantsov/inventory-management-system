package com.komeetta.dao;

import com.komeetta.model.Customer;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class CustomerDAOTest {

    private static CustomerDAO customerDAO;
    private static EntityManager entityManager;
    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));
        customerDAO = new CustomerDAO();
        emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnitTesting");
        entityManager = emf.createEntityManager();
    }

    @BeforeEach
    void beforeEach() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery("DELETE FROM Customer").executeUpdate();
        transaction.commit();
    }

    @Test
    void testAddCustomer() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("customer@example.com");
        customer.setPhoneNumber("987654321");
        customer.setAddress("Customer Address");

        customerDAO.addCustomer(customer);

        List<Customer> customers = customerDAO.getCustomers();
        assertFalse(customers.isEmpty());
        assertEquals("Test Customer", customers.get(0).getName());
    }

    @Test
    void testGetCustomer() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("customer@example.com");
        customer.setPhoneNumber("987654321");
        customer.setAddress("Customer Address");
        customerDAO.addCustomer(customer);

        Customer fetchedCustomer = customerDAO.getCustomer(customer.getCustomerId());
        assertNotNull(fetchedCustomer);
        assertEquals("Test Customer", fetchedCustomer.getName());
    }

    @Test
    void testGetNonExistentCustomer() {
        Customer fetchedCustomer = customerDAO.getCustomer(99999); // Assuming 99999 doesn't exist
        assertNull(fetchedCustomer, "Fetching a non-existent customer should return null.");
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setName("Old Name");
        customerDAO.addCustomer(customer);

        customer.setName("Updated Name");
        customerDAO.updateCustomer(customer);

        Customer updatedCustomer = customerDAO.getCustomer(customer.getCustomerId());
        assertEquals("Updated Name", updatedCustomer.getName());
    }

    @Test
    void testUpdateNonExistentCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(99999); // Fake ID
        customer.setName("Should Not Exist");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerDAO.updateCustomer(customer);
        });

        assertTrue(exception.getMessage().contains("Failed to update customer"),
                "Updating a non-existent customer should throw an error.");
    }


    @Test
    void testDeleteCustomer() {
        Customer customer = new Customer();
        customer.setName("To Be Deleted");
        customerDAO.addCustomer(customer);

        customerDAO.deleteCustomer(customer);
        Customer deletedCustomer = customerDAO.getCustomer(customer.getCustomerId());

        assertNull(deletedCustomer);
    }


    @AfterAll
    static void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
