package com.komeetta.model;

import com.komeetta.InitDBTest;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest extends InitDBTest {

    @BeforeEach
    void cleanUp() {
        truncate("Customer");
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

}

