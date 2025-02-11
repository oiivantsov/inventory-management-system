package com.komeetta.dao;


import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.PurchaseOrder;
import com.komeetta.model.PurchaseOrderItem;
import jakarta.persistence.EntityManager;

/**
 * Data Access Object for PurchaseOrderItem
 */
public class PurchaseOrderItemDAO {

    /**
     * Adds a new purchase order item to the database
     * @param purchaseOrderItem A new purchase order item
     */
    public void addPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();

            // Ensure PurchaseOrder and Product are managed before persisting PurchaseOrderItem
            purchaseOrderItem.setPurchaseOrder(em.merge(purchaseOrderItem.getPurchaseOrder()));
            purchaseOrderItem.setProduct(em.merge(purchaseOrderItem.getProduct()));

            em.persist(purchaseOrderItem);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add purchase order item", e);
        } finally {
            em.close();
        }
    }

    /**
     * Fetches a purchase order item by purchase order item ID
     * @param purchaseOrderItemId Purchase order item ID
     * @return Purchase order item object
     */
    public PurchaseOrderItem getPurchaseOrderItem(int purchaseOrderItemId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(PurchaseOrderItem.class, purchaseOrderItemId);
        } finally {
            em.close();
        }
    }

    /**
     * Updates a purchase order item in the database
     * @param purchaseOrderItem Purchase order item to update
     */
    public void updatePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.merge(purchaseOrderItem);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update purchase order item", e);
        } finally {
            em.close();
        }
    }

    /**
     * Get all purchase order items of a purchase order
     * @param purchaseOrder Purchase order
     * @return purchase order items
     */
    public void getPurchaseOrderItems(PurchaseOrder purchaseOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.createQuery("SELECT p FROM PurchaseOrderItem p WHERE p.purchaseOrder.id = :order_id")
                    .setParameter("order_id", purchaseOrder)
                    .getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to get purchase order items", e);
        } finally {
            em.close();
        }
    }

}


