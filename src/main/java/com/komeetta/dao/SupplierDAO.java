package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.Customer;
import com.komeetta.model.Supplier;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SupplierDAO {

    public void addSupplier(Supplier supplier) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.persist(supplier);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add supplier", e);
        } finally {
            em.close();
        }
    }

    public List<Supplier> getSuppliers() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            List<Supplier> suppliers = em.createQuery("SELECT s FROM Supplier s", Supplier.class).getResultList();
            em.getTransaction().commit();
            return suppliers;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to retrieve suppliers", e);
        } finally {
            em.close();
        }
    }

    public Supplier getSupplier(int supplierId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(Supplier.class, supplierId);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to fetch supplier", e);
        } finally {
            em.close();
        }
    }

    public void updateSupplier(Supplier supplier) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();

            // Ensure the supplier exists
            Supplier existingSupplier = em.find(Supplier.class, supplier.getSupplierId());
            if (existingSupplier == null) {
                throw new RuntimeException("Failed to update supplier: Supplier not found");
            }

            em.merge(supplier);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update supplier", e);
        } finally {
            em.close();
        }
    }

    public void deleteSupplier(Supplier supplier) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            // merge
            Supplier managedCustomer = em.merge(supplier); // Ensure the entity is managed
            em.remove(managedCustomer);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete supplier", e);
        } finally {
            em.close();
        }
    }

    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Supplier").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete suppliers", e);
        } finally {
            em.close();
        }
    }
}
