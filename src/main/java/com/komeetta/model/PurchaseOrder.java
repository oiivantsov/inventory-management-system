package com.komeetta.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

/** Represents a purchase order in the database.
 * It has the following attributes:
 * - orderId: the unique identifier of the order
 * - supplier: the supplier from which the order was made
 * - orderDate: the date and time when the order was placed
 * - status: the status of the order (pending, in_progress, completed)
 * - totalPrice: the total price of the order
 * - items: a list of items in the order
 */

@Entity
@Table(name = "PurchaseOrder")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date", nullable = false, updatable = false)
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(20) DEFAULT 'completed'")
    private OrderStatus status;

    @Column(name = "order_total", columnDefinition = "DOUBLE DEFAULT 0")
    private double orderTotal;

    @OneToMany(mappedBy = "purchaseOrder", fetch = FetchType.EAGER) // to auto-fetch items when loading the order
    private List<PurchaseOrderItem> items;

    /** Default constructor required by JPA */
    public PurchaseOrder() {
    }

    /** Constructor for creating a new purchase order.
     * param: supplier the supplier from which the order was made
     * param: orderDate the date and time the order was made
     * param: status the status of the order
     * param: totalPrice the total price of the order
     */
    public PurchaseOrder(Supplier supplier, Date orderDate, OrderStatus status, double orderTotal) {
        this.supplier = supplier;
        this.orderDate = orderDate;
        this.status = status;
        this.orderTotal = orderTotal;
    }

    /** Parameterized constructor
     * @param supplier: the customer who placed the order
     */
    public PurchaseOrder(Supplier supplier) {
        this.supplier = supplier;
        this.orderDate = new Date();
        this.orderTotal = 0;
        this.status = OrderStatus.COMPLETED;
    }

    /** Get the unique identifier of the order.
     * return: the order ID
     */
    public int getOrderId() {
        return orderId;
    }

    /** Set the unique identifier of the order.
     * param: orderId the order ID
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /** Get the supplier from which the order was made.
     * return: the supplier
     */
    public Supplier getSupplier() {
        return supplier;
    }

    /** Set the supplier from which the order was made.
     * param: supplier the supplier
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    /** Get the date and time the order was made.
     * return: the order date
     */
    public Date getOrder_date() {
        return orderDate;
    }

    /** Set the date and time the order was made.
     * param: orderDate the order date
     */
    public void setOrder_date(Date orderDate) {
        this.orderDate = orderDate;
    }

    /** Get the status of the order.
     * return: the order status
     * @see OrderStatus
     */
    public OrderStatus getOrderStatus() {
        return status;
    }

    /** Set the status of the order.
     * param: orderStatus the order status
     * @see OrderStatus
     */
    public void setOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    /** Get the total price of the order.
     * return: the total price
     */
    public double getOrderTotal() {
        return orderTotal;
    }

    /** Set the total price of the order.
     * param: orderTotal the total price
     */
    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    /** Get the list of items in the order.
     * return: the list of items
     */
    public List<PurchaseOrderItem> getItems() {
        return items;
    }
}
