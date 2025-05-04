/**
 * CustomerDAO handles database operations related to Customer entities.
 * It provides methods to add, retrieve, update, and delete customers
 * using JPA and a MariaDB connection.
 */
package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.Customer;
import jakarta.persistence.EntityManager;
import java.util.List;

public class CustomerDAO {

    /**
     * Persists a new customer to the database.
     * @param customer the Customer object to be saved
     */
    public void addCustomer(Customer customer) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(customer);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Retrieves all customers from the database.
     * @return a list of all Customer objects
     */
    public List<Customer> getCustomers() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        List<Customer> customers = em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return customers;
    }

    /**
     * Retrieves a specific customer by ID.
     * @param customerId the ID of the customer to retrieve
     * @return the Customer object if found, otherwise null
     */
    public Customer getCustomer(int customerId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Customer customer = em.find(Customer.class, customerId);
        em.close();
        return customer;
    }

    /**
     * Updates an existing customer in the database.
     * @param customer the Customer object with updated fields
     * @throws RuntimeException if the customer does not exist in the database
     */
    public void updateCustomer(Customer customer) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        Customer existingCustomer = em.find(Customer.class, customer.getCustomerId());
        if (existingCustomer == null) {
            throw new RuntimeException("Failed to update customer: Customer not found");
        }
        em.merge(customer);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Deletes a customer from the database.
     * @param customer the Customer object to delete
     */
    public void deleteCustomer(Customer customer) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        Customer managedCustomer = em.merge(customer); // Ensure the entity is managed
        em.remove(managedCustomer);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Deletes all customer records from the database.
     */
    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Customer").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }


}