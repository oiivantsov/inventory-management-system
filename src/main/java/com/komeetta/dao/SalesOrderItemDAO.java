package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.OrderItemId;
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

        em.getTransaction().begin();

        // Ensure PurchaseOrder and Product are managed before persisting PurchaseOrderItem
        salesOrderItem.setSalesOrder(em.merge(salesOrderItem.getSalesOrder()));
        salesOrderItem.setProduct(em.merge(salesOrderItem.getProduct()));

        em.persist(salesOrderItem);
        em.getTransaction().commit();

        em.close();

    }

    /**
     * Fetches a sales order item by sales order item ID
     * @param salesOrderItemId Sales order item ID
     * @return Sales order item object
     */
    public SalesOrderItem getSalesOrderItem(OrderItemId salesOrderItemId) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        SalesOrderItem result = em.find(SalesOrderItem.class, salesOrderItemId);

        em.close();
        return result;
    }

    /**
     * Updates a sales order item in the database
     * @param salesOrderItem Sales order item to update
     */
    public void updateSalesOrderItem(SalesOrderItem salesOrderItem) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        em.getTransaction().begin();
        em.merge(salesOrderItem);
        em.getTransaction().commit();

        em.close();

    }

    /**
     * Deletes a sales order item from the database
     * @param salesOrderItemId Sales order item ID
     */
    public void deleteSalesOrderItem(OrderItemId salesOrderItemId) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        em.getTransaction().begin();
        SalesOrderItem salesOrderItem = em.find(SalesOrderItem.class, salesOrderItemId);
        em.remove(salesOrderItem);
        em.getTransaction().commit();


        em.close();
    }

    /**
     * Fetches all sales order items for a given sales order
     * @param salesOrderId Sales order id
     * @return List of sales order items
     */
    public List<SalesOrderItem> getSalesOrderItems(int salesOrderId) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        List<SalesOrderItem> result = em.createQuery("SELECT s FROM SalesOrderItem s WHERE s.salesOrder.orderId = :salesOrderId", SalesOrderItem.class)
                    .setParameter("salesOrderId", salesOrderId)
                    .getResultList();

        em.close();

        return result;

    }

}