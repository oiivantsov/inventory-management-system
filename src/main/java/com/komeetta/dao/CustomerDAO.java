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
        try {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add customer", e);
        } finally {
            em.close();
        }
    }

    public List<Customer> getCustomers() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            List<Customer> customers = em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
            em.getTransaction().commit();
            return customers;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to retrieve customers", e);
        } finally {
            em.close();
        }
    }


    public Customer getCustomer(int customerId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(Customer.class, customerId);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to fetch customer", e);
        } finally {
            em.close();
        }
    }

    public void updateCustomer(Customer customer) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.merge(customer);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update customer", e);
        } finally {
            em.close();
        }
    }

    public void deleteCustomer(Customer customer) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            Customer managedCustomer = em.merge(customer); // Ensure the entity is managed
            em.remove(managedCustomer);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete customer", e);
        } finally {
            em.close();
        }
    }


    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Customer").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete customers", e);
        } finally {
            em.close();
        }
    }
}
