package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.Product;
import jakarta.persistence.EntityManager;

/**
 * Data Access Object for Product
 */

public class ProductDAO {

    /**
     * Adds a new product to the database
     * @param product A new product
     */
    public void addProduct(Product product) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.persist(product);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add product", e);
        } finally {
            em.close();
        }

    }

    /**
     * Fetches a product by product ID
     * @param productId Product ID
     * @return Product object
     */
    public Product getProduct(int productId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(Product.class, productId);
        } finally {
            em.close();
        }
    }

    /**
     * Updates a product in the database
     * @param product Product to update
     */
    public void updateProduct(Product product) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.merge(product);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update product", e);
        } finally {
            em.close();
        }

    }

    /**
     * Deletes a product from the database
     * @param productId Product ID
     */
    public void deleteProduct(int productId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            Product product = em.find(Product.class, productId);
            em.remove(product);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete product", e);
        } finally {
            em.close();
        }
    }

    /**
     * Deletes all products (for testing purposes)
     */
    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Product").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete all products", e);
        } finally {
            em.close();
        }
    }

}
