package com.komeetta.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

/** SalesOrder class represents a sales order in the database. It has the following attributes:
 * - orderId: the unique identifier of the order
 * - customer: the customer who placed the order
 * - orderDate: the date and time when the order was placed
 * - status: the status of the order (pending, in_progress, completed)
 * - orderTotal: the total amount of the order
 * - items: a list of items in the order
 */

@Entity
@Table(name = "SalesOrder")
public class SalesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date", nullable = false, updatable = false)
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(20) DEFAULT 'completed'")
    private OrderStatus status;

    @Column(name = "order_total", columnDefinition = "DOUBLE DEFAULT 0")
    private double orderTotal;

    @OneToMany(mappedBy = "salesOrder", fetch = FetchType.EAGER)
    private List<SalesOrderItem> items;

    //Constructors
    /** Default constructor
     * (required by JPA)
     * */
    public SalesOrder() {}

    /** Parameterized constructor
     * @param customer: the customer who placed the order
     * @param orderDate: the date and time when the order was placed
     * @param orderStatus: the status of the order (pending, in_progress, completed)
     * @param orderTotal: the total amount of the order
     */
    public SalesOrder(Customer customer, Date orderDate, OrderStatus orderStatus, double orderTotal) {
        this.customer = customer;
        this.orderDate = orderDate;
        this.status = orderStatus;
        this.orderTotal = orderTotal;
    }

    /** Parameterized constructor
     * @param customer: the customer who placed the order
     */
    public SalesOrder(Customer customer) {
        this.customer = customer;
        this.orderDate = new Date();
        this.orderTotal = 0;
        this.status = OrderStatus.COMPLETED;
    }

    // Getters and setters

    /** Get the order ID
     * @return order ID
     */
    public int getOrderId() {
        return orderId;
    }

    /** Set the order ID
     * @param orderId: the unique identifier of the order
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /** Get the customer who placed the order
     * @return customer
     */
    public Customer getCustomer() {
        return customer;
    }

    public int getCustomerId() {return customer.getCustomerId();}

    /** Set the customer who placed the order
     * @param customer: the customer who placed the order
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /** Get the date and time when the order was placed
     * @return order date
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /** Set the date and time when the order was placed
     * @param orderDate: the date and time when the order was placed
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /** Get the status of the order
     * @return order status
     */
    public OrderStatus getOrderStatus() {
        return status;
    }

    /** Set the status of the order
     * @param orderStatus: the status of the order (pending, in_progress, completed)
     */
    public void setOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    /** Get the total amount of the order
     * @return order total
     */
    public double getOrderTotal() {
        return orderTotal;
    }

    /** Set the total amount of the order
     * @param order_total: the total amount of the order
     */
    public void setOrderTotal(double order_total) {
        this.orderTotal = order_total;
    }

    /** Get the list of items in the order
     * @return items
     */
    public List<SalesOrderItem> getItems() {
        return items;
    }
}
