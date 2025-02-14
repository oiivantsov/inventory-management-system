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
     * Fetches a sales order by sales order ID with items (use getItems() to fetch items)
     * @param salesOrderId Sales order ID
     * @return Sales order object
     */
    public SalesOrder getSalesOrder(int salesOrderId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(SalesOrder.class, salesOrderId);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to fetch sales order with items", e);
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

            // Check if the order is managed by the entity manager
            SalesOrder managedOrder = em.find(SalesOrder.class, salesOrder.getOrderId());
            if (managedOrder != null) {
                em.remove(managedOrder);
            }

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
     * Fetches all sales orders from the database
     * @return List of sales orders
     */
    public List<SalesOrder> getSalesOrders() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.createQuery("SELECT s FROM SalesOrder s", SalesOrder.class).getResultList();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to retrieve sales orders", e);
        } finally {
            em.close();
        }

    }

    /**
     * Sum of all sales orders
     */
    public double getTotalSaleOrders() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.createQuery("SELECT SUM(s.orderTotal) FROM SalesOrder s", Double.class).getSingleResult();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to sum sales orders", e);
        } finally {
            em.close();
        }
    }

}
