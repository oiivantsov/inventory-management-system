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
        em.getTransaction().begin();
        em.persist(supplier);
        em.getTransaction().commit();
        em.close();
    }

    public List<Supplier> getSuppliers() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        List<Supplier> suppliers = em.createQuery("SELECT s FROM Supplier s", Supplier.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return suppliers;
    }

    public Supplier getSupplier(int supplierId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Supplier supplier = em.find(Supplier.class, supplierId);
        em.close();
        return supplier;
    }

    public void updateSupplier(Supplier supplier) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        // Ensure the supplier exists
        Supplier existingSupplier = em.find(Supplier.class, supplier.getSupplierId());
        if (existingSupplier == null) {
            throw new RuntimeException("Failed to update supplier: Supplier not found");
        }
        em.merge(supplier);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteSupplier(Supplier supplier) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        // merge
        Supplier managedCustomer = em.merge(supplier); // Ensure the entity is managed
        em.remove(managedCustomer);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Supplier").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}
