package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
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
            em.remove(supplier);
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

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("JDBC_PASSWORD"));

        SupplierDAO supplierDAO = new SupplierDAO();

        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setEmail("test@supplier.com");
        supplier.setPhoneNumber("123456789");
        supplier.setAddress("Supplier Address");

        supplierDAO.addSupplier(supplier);

        List<Supplier> suppliers = supplierDAO.getSuppliers();
        System.out.println("Number of suppliers: " + suppliers.size());
        if (!suppliers.isEmpty()) {
            System.out.println("First Supplier Name: " + suppliers.get(0).getName());
        }
    }
}
