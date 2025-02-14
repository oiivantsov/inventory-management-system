package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.SalesOrderItem;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data Access Object for SalesOrderItem
 */
public class SalesOrderItemDAO {

    /**
     * Adds a new sales order item to the database
     * @param salesOrderItem A new sales order item
     */
    public void addSalesOrderItem(SalesOrderItem salesOrderItem) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();

            // Ensure PurchaseOrder and Product are managed before persisting PurchaseOrderItem
            salesOrderItem.setSalesOrder(em.merge(salesOrderItem.getSalesOrder()));
            salesOrderItem.setProduct(em.merge(salesOrderItem.getProduct()));

            em.persist(salesOrderItem);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add sales order item", e);
        } finally {
            em.close();
        }
    }

    /**
     * Fetches a sales order item by sales order item ID
     * @param salesOrderItemId Sales order item ID
     * @return Sales order item object
     */
    public SalesOrderItem getSalesOrderItem(int salesOrderItemId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(SalesOrderItem.class, salesOrderItemId);
        } finally {
            em.close();
        }
    }

    /**
     * Updates a sales order item in the database
     * @param salesOrderItem Sales order item to update
     */
    public void updateSalesOrderItem(SalesOrderItem salesOrderItem) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.merge(salesOrderItem);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update sales order item", e);
        } finally {
            em.close();
        }
    }

    /**
     * Deletes a sales order item from the database
     * @param salesOrderItemId Sales order item ID
     */
    public void deleteSalesOrderItem(int salesOrderItemId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            SalesOrderItem salesOrderItem = em.find(SalesOrderItem.class, salesOrderItemId);
            em.remove(salesOrderItem);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete sales order item", e);
        } finally {
            em.close();
        }
    }

    /**
     * Fetches all sales order items for a given sales order
     * @param salesOrderId Sales order id
     * @return List of sales order items
     */
    public List<SalesOrderItem> getSalesOrderItems(int salesOrderId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.createQuery("SELECT s FROM SalesOrderItem s WHERE s.salesOrder.orderId = :salesOrderId", SalesOrderItem.class)
                    .setParameter("salesOrderId", salesOrderId)
                    .getResultList();
        } catch (Exception e){
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to fetch sales order items", e);
        } finally {
            em.close();
        }
    }

}