package com.komeetta.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for PurchaseOrderItem
 */
@Embeddable
public class OrderItemId implements Serializable {
    /** The unique identifier of the order */
    private int orderId;
    private int productId;

    // Default constructor (required by JPA)
    public OrderItemId() {}

    /** Parameterized constructor
     * @param orderId the sales order to which the item belongs
     * @param productId the product being ordered
     */
    public OrderItemId(int orderId, int productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    // Getters and Setters
    /** Getter for orderId
     * @return the orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /** Setter for orderId
     * @param orderId the orderId to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /** Getter for productId
     * @return the productId
     */
    public int getProductId() {
        return productId;
    }

    /** Setter for productId
     * @param productId the productId to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /** equals method
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemId that = (OrderItemId) o;
        return orderId == that.orderId && productId == that.productId;
    }

    /** hashCode method
     * @return the hash code of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }
}
