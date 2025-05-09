package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.PurchaseOrder;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for PurchaseOrder
 */
public class PurchaseOrderDAO {

    /**
     * Adds a new purchase order to the database
     * @param purchaseOrder  A new purchase order
     */
    public void addPurchaseOrder(PurchaseOrder purchaseOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        em.getTransaction().begin();
        em.persist(purchaseOrder);
        em.getTransaction().commit();

        em.close();
    }

    /**
     * Fetches a purchase  order by purchase order ID
     * @param purchaseOrderId Purchase order ID
     * @return Purchase order object
     */
    public PurchaseOrder getPurchaseOrder(int purchaseOrderId) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        PurchaseOrder result = em.find(PurchaseOrder.class, purchaseOrderId);

        em.close();

        return result;

    }

    /**
     * Updates a purchase order in the database
     * @param purchaseOrder Purchase order to update
     */
    public void updatePurchaseOrder(PurchaseOrder purchaseOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        em.getTransaction().begin();
        em.merge(purchaseOrder);
        em.getTransaction().commit();

        em.close();

    }

    /**
     * Deletes a purchase order from  the database
     * @param purchaseOrder Purchase order to delete
     */

    public void deletePurchaseOrder(PurchaseOrder purchaseOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        em.getTransaction().begin();

        // Check if the order is managed by the entity manager
        PurchaseOrder managedOrder = em.find(PurchaseOrder.class, purchaseOrder.getOrderId());
        if (managedOrder != null) {
            em.remove(managedOrder);
        }

        em.getTransaction().commit();

        em.close();
    }

    /**
     * Deletes a purchase order and its items from the database.
     * @param purchaseOrder Purchase order to delete
     */
    public void deletePurchaseOrderWithItems(PurchaseOrder purchaseOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();

            em.createQuery("DELETE FROM PurchaseOrderItem p WHERE p.purchaseOrder.orderId = :orderId")
                    .setParameter("orderId", purchaseOrder.getOrderId())
                    .executeUpdate();

            PurchaseOrder managedOrder = em.find(PurchaseOrder.class, purchaseOrder.getOrderId());
            if (managedOrder != null) {
                em.remove(managedOrder);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete PurchaseOrder with items", e);
        } finally {
            em.close();
        }
    }

    /**
     * Deletes all sales orders (for testing purposes)
     */
    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();

        em.getTransaction().begin();
        em.createQuery("DELETE FROM PurchaseOrder").executeUpdate();
        em.getTransaction().commit();


        em.close();
    }

    /**
     * Fetches all sales orders from the database
     * @return List of sales orders
     */
    public List<PurchaseOrder> getPurchaseOrders() {
        EntityManager em = MariaDbJpaConnection.getInstance();

        List<PurchaseOrder> result = em.createQuery("SELECT s FROM PurchaseOrder s", PurchaseOrder.class).getResultList();

        em.close();

        return result;
    }

    /**
     * Sum of all purchase orders
     * @return Total purhcase orders
     */
    public double getTotalPurchaseOrders() {
        EntityManager em = MariaDbJpaConnection.getInstance();

        // if no orders, return 0
        if (em.createQuery("SELECT COUNT(s) FROM PurchaseOrder s", Long.class).getSingleResult() == 0) {
            return 0.0;
        }
        double result = em.createQuery("SELECT SUM(s.orderTotal) FROM PurchaseOrder s", Double.class).getSingleResult();

        em.close();

        return result;
    }

    /**
     * Sum of all purchase orders for a specific supplier
     * @param supplierId Supplier ID
     * @return Total purchase orders
     */
    public double getTotalPurchaseOrdersBySupplier(int supplierId) {
        EntityManager em = MariaDbJpaConnection.getInstance();

            // if no orders, return 0
        if (em.createQuery("SELECT COUNT(s) FROM PurchaseOrder s WHERE s.supplier.supplierId = :supplierId", Long.class)
                .setParameter("supplierId", supplierId)
                .getSingleResult() == 0) {
            return 0.0;
        }
        double result = em.createQuery("SELECT SUM(s.orderTotal) FROM PurchaseOrder s WHERE s.supplier.supplierId = :supplierId", Double.class)
                .setParameter("supplierId", supplierId)
                .getSingleResult();


        em.close();

        return result;

    }

    /**
     * Fetches the total number of purchase orders
     * @return Total number of purchase orders (int)
     */
    public int getNumberOfPurchaseOrders() {
        EntityManager em = MariaDbJpaConnection.getInstance();

        long count = em.createQuery("SELECT COUNT(s) FROM PurchaseOrder s", long.class).getSingleResult();
        int result = (int) count;

        em.close();

        return result;
    }

    /**
     * Sum of all purchase orders from the last three months
     * @return Total number of purchase orders from the last three months (double)
     */
    public double getThreeMonthOrders() {
        EntityManager em = MariaDbJpaConnection.getInstance();

        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        Date threeMonthsAgoDate = Date.from(threeMonthsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Double result = em.createQuery("SELECT SUM(s.orderTotal) FROM PurchaseOrder s WHERE s.orderDate >= :threeMonthsAgo", Double.class)
                .setParameter("threeMonthsAgo", threeMonthsAgoDate)
                .getSingleResult();

        em.close();

        return (result != null) ? result : 0.0;
    }
}
