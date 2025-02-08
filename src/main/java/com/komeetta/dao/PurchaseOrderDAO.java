package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.PurchaseOrder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * Data Access Object for PurchaseOrder
 */
public class PurchaseOrderDAO {

    /**
     * Adds a new purchase order to the database
     * @param purchaseOrder A new purchase order
     */
    public void addPurchaseOrder(PurchaseOrder purchaseOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.persist(purchaseOrder);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add purchase order", e);
        } finally {
            em.close();
        }
    }

    /**
     * Fetches a purchase order by purchase order ID
     * @param purchaseOrderId Purchase order ID
     * @return Purchase order object
     */
    public PurchaseOrder getPurchaseOrder(int purchaseOrderId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(PurchaseOrder.class, purchaseOrderId);
        } finally {
            em.close();
        }
    }

    /**
     * Updates a purchase order in the database
     * @param purchaseOrder Purchase order to update
     */
    public void updatePurchaseOrder(PurchaseOrder purchaseOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.merge(purchaseOrder);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update purchase order", e);
        } finally {
            em.close();
        }
    }

    /**
     * Fetches a purchase order by its ID along with its items.
     * @param orderId The ID of the purchase order.
     * @return The purchase order with its items.
     */
    public PurchaseOrder getPurchaseOrderWithItems(int orderId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            TypedQuery<PurchaseOrder> query = em.createQuery(
                    "SELECT p FROM PurchaseOrder p JOIN FETCH p.items WHERE p.orderId = :orderId",
                    PurchaseOrder.class
            );
            query.setParameter("orderId", orderId);
            return query.getSingleResult();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to fetch purchase order with items", e);
        }
        finally {
            em.close();
        }
    }

}
