package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.SalesOrder;
import jakarta.persistence.EntityManager;

/**
 * Data Access Object for SalesOrder
 */
public class SalesOrderDAO {

    /**
     * Adds a new sales order to the database
     * @param salesOrder A new sales order
     */
    public void addSalesOrder(SalesOrder salesOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.persist(salesOrder);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add sales order", e);
        } finally {
            em.close();
        }
    }

    /**
     * Fetches a sales order by sales order ID
     * @param salesOrderId Sales order ID
     * @return Sales order object
     */
    public SalesOrder getSalesOrder(int salesOrderId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(SalesOrder.class, salesOrderId);
        } finally {
            em.close();
        }
    }

    /**
     * Updates a sales order in the database
     * @param salesOrder Sales order to update
     */
    public void updateSalesOrder(SalesOrder salesOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.merge(salesOrder);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update sales order", e);
        } finally {
            em.close();
        }
    }

    /**
     * Deletes a sales order from the database
     * @param salesOrder Sales order to delete
     */
    public void deleteSalesOrder(SalesOrder salesOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.remove(salesOrder);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete sales order", e);
        } finally {
            em.close();
        }
    }

    /**
     * Deletes all sales orders (for testing purposes)
     */
    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM SalesOrder").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete sales orders", e);
        } finally {
            em.close();
        }
    }

    /**
     * Fetches a sales order by its ID along with its items.
     * @param orderId The ID of the sales order.
     * @return The sales order with its items.
     */
    public SalesOrder getSalesOrderWithItems(int orderId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.createQuery("SELECT s FROM SalesOrder s JOIN FETCH s.items WHERE s.orderId = :orderId", SalesOrder.class)
                    .setParameter("orderId", orderId)
                    .getSingleResult();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to fetch sales order with items", e);
        } finally {
            em.close();
        }
    }

}
