package com.komeetta.dao;


import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.OrderItemId;
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

        em.getTransaction().begin();

        purchaseOrderItem.setPurchaseOrder(em.merge(purchaseOrderItem.getPurchaseOrder()));
        purchaseOrderItem.setProduct(em.merge(purchaseOrderItem.getProduct()));

        em.persist(purchaseOrderItem);
        em.getTransaction().commit();

        em.close();

    }

    /**
     * Fetches a purchase order item by purchase order item ID
     * @param purchaseOrderItemId Purchase order item ID
     * @return Purchase order item object
     */
    public PurchaseOrderItem getPurchaseOrderItem(OrderItemId purchaseOrderItemId) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        PurchaseOrderItem result = em.find(PurchaseOrderItem.class, purchaseOrderItemId);

        em.close();

        return result;
    }

    /**
     * Updates a purchase order item in the database
     * @param purchaseOrderItem Purchase order item to update
     */
    public void updatePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        em.getTransaction().begin();
        em.merge(purchaseOrderItem);
        em.getTransaction().commit();

        em.close();
    }

    /**
     * Get all purchase order items of a purchase order
     * @param purchaseOrder Purchase order
     * @return purchase order items
     */
    public void getPurchaseOrderItems(PurchaseOrder purchaseOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.createQuery("SELECT p FROM PurchaseOrderItem p WHERE p.purchaseOrder.id = :order_id")
                .setParameter("order_id", purchaseOrder)
                .getResultList();
        em.getTransaction().commit();

        em.close();
    }

}


