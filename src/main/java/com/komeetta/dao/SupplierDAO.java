/**
 * SupplierDAO handles database operations related to Supplier entities.
 * It provides methods for creating, retrieving, updating, and deleting suppliers
 * using JPA and a MariaDB connection.
 */
package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.Supplier;
import jakarta.persistence.EntityManager;
import java.util.List;

public class SupplierDAO {

    /**
     * Adds a new supplier to the database.
     * @param supplier the Supplier object to persist
     */
    public void addSupplier(Supplier supplier) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(supplier);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Retrieves all suppliers from the database.
     * @return a list of all Supplier entities
     */
    public List<Supplier> getSuppliers() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        List<Supplier> suppliers = em.createQuery("SELECT s FROM Supplier s", Supplier.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return suppliers;
    }

    /**
     * Retrieves a single supplier by its ID.
     * @param supplierId the ID of the supplier to fetch
     * @return the Supplier object if found, otherwise null
     */
    public Supplier getSupplier(int supplierId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Supplier supplier = em.find(Supplier.class, supplierId);
        em.close();
        return supplier;
    }

    /**
     * Updates an existing supplier in the database.
     * @param supplier the Supplier entity with updated values
     * @throws RuntimeException if the supplier does not exist in the database
     */
    public void updateSupplier(Supplier supplier) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        Supplier existingSupplier = em.find(Supplier.class, supplier.getSupplierId());
        if (existingSupplier == null) {
            throw new RuntimeException("Failed to update supplier: Supplier not found");
        }
        em.merge(supplier);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Deletes a supplier from the database.
     * @param supplier the Supplier entity to delete
     */
    public void deleteSupplier(Supplier supplier) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        Supplier managedSupplier = em.merge(supplier); // Ensure the entity is managed
        em.remove(managedSupplier);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Deletes all suppliers from the database.
     */
    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Supplier").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}