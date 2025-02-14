package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.PurchaseOrder;
import jakarta.persistence.EntityManager;

import java.util.List;

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
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to fetch purchases order with items", e);
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
     * Deletes a purchase order from the database
     * @param purchaseOrder Purchase order to delete
     */

    public void deletePurchaseOrder(PurchaseOrder purchaseOrder) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();

            // Check if the order is managed by the entity manager
            PurchaseOrder managedOrder = em.find(PurchaseOrder.class, purchaseOrder.getOrderId());
            if (managedOrder != null) {
                em.remove(managedOrder);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete purchase order", e);
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
            em.createQuery("DELETE FROM PurchaseOrder").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete purchases orders", e);
        } finally {
            em.close();
        }
    }

    /**
     * Fetches all sales orders from the database
     * @return List of sales orders
     */
    public List<PurchaseOrder> getPurchaseOrders() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.createQuery("SELECT s FROM PurchaseOrder s", PurchaseOrder.class).getResultList();
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
     * Sum of all purchase orders
     * @return Total purchase orders
     */
    public double getTotalPurchaseOrders() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.createQuery("SELECT SUM(s.orderTotal) FROM PurchaseOrder s", Double.class).getSingleResult();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to retrieve total purchase orders", e);
        } finally {
            em.close();
        }
    }

    /**
     * Sum of all purchase orders for a specific supplier
     * @param supplierId Supplier ID
     * @return Total purchase orders
     */
    public double getTotalPurchaseOrdersBySupplier(int supplierId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.createQuery("SELECT SUM(s.orderTotal) FROM PurchaseOrder s WHERE s.supplier.supplierId = :supplierId", Double.class)
                    .setParameter("supplierId", supplierId)
                    .getSingleResult();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to retrieve total purchase orders by supplier", e);
        } finally {
            em.close();
        }
    }


}
