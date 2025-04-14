package com.komeetta.dao;

import com.komeetta.InitDBTest;
import com.komeetta.model.Customer;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class CustomerDAOTest extends InitDBTest {

    private static CustomerDAO customerDAO;

    @BeforeAll
    static void setupDAO() {
        customerDAO = new CustomerDAO();
    }

    @BeforeEach
    void beforeEach() {
        truncate("Customer");
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
    void testDeleteCustomer() {
        Customer customer = new Customer();
        customer.setName("To Be Deleted");
        customerDAO.addCustomer(customer);

        customerDAO.deleteCustomer(customer);
        Customer deletedCustomer = customerDAO.getCustomer(customer.getCustomerId());

        assertNull(deletedCustomer);
    }
}
