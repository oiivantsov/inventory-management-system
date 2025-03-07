package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.SalesOrder;
import jakarta.persistence.EntityManager;

import java.util.List;

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

        em.getTransaction().begin();
        em.persist(salesOrder);
        em.getTransaction().commit();


        em.close();
    }

    /**
     * Fetches a sales order by sales order ID with items (use getItems() to fetch items)
     * @param salesOrderId Sales order ID
     * @return Sales order object
     */
    public SalesOrder getSalesOrder(int salesOrderId) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        SalesOrder result = em.find(SalesOrder.class, salesOrderId);

        em.close();

        return result;

    }

    /**
     * Updates a sales order in the database
     * @param salesOrder Sales order to update
     */
    public void updateSalesOrder(SalesOrder salesOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        em.getTransaction().begin();
        em.merge(salesOrder);
        em.getTransaction().commit();

        em.close();
    }

    /**
     * Deletes a sales order from the database
     * @param salesOrder Sales order to delete
     */
    public void deleteSalesOrder(SalesOrder salesOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        em.getTransaction().begin();

        // Check if the order is managed by the entity manager
        SalesOrder managedOrder = em.find(SalesOrder.class, salesOrder.getOrderId());
        if (managedOrder != null) {
            em.remove(managedOrder);
        }

        em.getTransaction().commit();


        em.close();
    }


    /**
     * Deletes all sales orders (for testing purposes)
     */
    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();

        em.getTransaction().begin();
        em.createQuery("DELETE FROM SalesOrder").executeUpdate();
        em.getTransaction().commit();

        em.close();
    }

    /**
     * Fetches all sales orders from the database
     * @return List of sales orders
     */
    public List<SalesOrder> getSalesOrders() {
        EntityManager em = MariaDbJpaConnection.getInstance();

        List<SalesOrder> result = em.createQuery("SELECT s FROM SalesOrder s", SalesOrder.class).getResultList();

        em.close();

        return result;

    }

    /**
     * Sum of all sales orders
     */
    public double getTotalSaleOrders() {
        EntityManager em = MariaDbJpaConnection.getInstance();

        if (getSalesOrders().isEmpty()) {
            return 0;
        }
        double result = em.createQuery("SELECT SUM(s.orderTotal) FROM SalesOrder s", Double.class).getSingleResult();

        em.close();
        return result;

    }

}
