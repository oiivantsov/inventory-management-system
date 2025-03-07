package com.komeetta.model;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
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
        entityManager.createQuery("DELETE FROM Customer").executeUpdate();
        transaction.commit();
    }

    @Test
    void testCustomerEntityCreation() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhoneNumber("123456789");
        customer.setAddress("123 Main St");

        assertEquals("John Doe", customer.getName());
        assertEquals("john.doe@example.com", customer.getEmail());
        assertEquals("123456789", customer.getPhoneNumber());
        assertEquals("123 Main St", customer.getAddress());
    }

    @Test
    void testCustomerPersistence() {
        Customer customer = new Customer();
        customer.setName("Alice Brown");
        customer.setEmail("alice.brown@example.com");
        customer.setPhoneNumber("987654321");
        customer.setAddress("456 Elm St");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(customer);
        transaction.commit();

        assertNotNull(customer.getCustomerId()); // Ensure ID is assigned
        Customer retrievedCustomer = entityManager.find(Customer.class, customer.getCustomerId());
        assertNotNull(retrievedCustomer);
        assertEquals("Alice Brown", retrievedCustomer.getName());
        assertEquals("alice.brown@example.com", retrievedCustomer.getEmail());
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setName("Charlie Smith");
        customer.setEmail("charlie.smith@example.com");
        customer.setPhoneNumber("555111222");
        customer.setAddress("789 Oak St");

        // Persist customer
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(customer);
        transaction.commit();

        // Modify customer
        transaction.begin();
        customer.setName("Charlie Johnson");
        entityManager.merge(customer);
        transaction.commit();

        // Verify update
        Customer updatedCustomer = entityManager.find(Customer.class, customer.getCustomerId());
        assertNotNull(updatedCustomer);
        assertEquals("Charlie Johnson", updatedCustomer.getName());
    }

    @Test
    void testDeleteCustomer() {
        Customer customer = new Customer();
        customer.setName("Eve White");
        customer.setEmail("eve.white@example.com");
        customer.setPhoneNumber("777888999");
        customer.setAddress("654 Pine St");

        // Persist customer
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(customer);
        transaction.commit();

        // Delete customer
        transaction.begin();
        Customer customerToDelete = entityManager.find(Customer.class, customer.getCustomerId());
        entityManager.remove(customerToDelete);
        transaction.commit();

        // Verify deletion
        Customer deletedCustomer = entityManager.find(Customer.class, customer.getCustomerId());
        assertNull(deletedCustomer);
    }

    @AfterAll
    static void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}

