package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.Customer;
import com.komeetta.model.Supplier;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CustomerDAO {

    public void addCustomer(Customer customer) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(customer);
        em.getTransaction().commit();
        em.close();
    }

    public List<Customer> getCustomers() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        List<Customer> customers = em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return customers;
    }


    public Customer getCustomer(int customerId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Customer customer = em.find(Customer.class, customerId);
        em.close();
        return customer;
    }

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

    public void deleteCustomer(Customer customer) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        Customer managedCustomer = em.merge(customer); // Ensure the entity is managed
        em.remove(managedCustomer);
        em.getTransaction().commit();
        em.close();
    }


    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Customer").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}
