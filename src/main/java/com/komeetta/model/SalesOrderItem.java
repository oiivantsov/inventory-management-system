package com.komeetta.model;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Represents an item in a sales order
 * It has the following attributes:
 * - salesOrder: the sales order to which the item belongs
 * - product: the product being ordered
 * - quantity: the quantity of the product being ordered
 * - unitPrice: the unit price of the product
 * - sale: the sale percentage of the product
 */
@Entity
@Table(name = "SalesOrderItems")
@IdClass(OrderItemId.class)
public class SalesOrderItem {
    @Id
    @Column(name = "order_id")
    private int orderId;

    @Id
    @Column(name = "product_id")
    private int productId;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private SalesOrder salesOrder;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private double unitPrice;

    @Column(name = "sale", columnDefinition = "DOUBLE DEFAULT 0")
    private double sale;

    // Constructors
    /** Default constructor
     * (required by JPA)
     * */
    public SalesOrderItem() {
    }

    /** Parameterized constructor
     * @param salesOrder the sales order to which the item belongs
     * @param product the product being ordered
     * @param quantity the quantity of the product being ordered
     * @param unitPrice the unit price of the product
     * @param sale the sale percentage of the product
     */
    public SalesOrderItem(SalesOrder salesOrder, Product product, int quantity, double unitPrice, double sale) {
        this.salesOrder = salesOrder;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.sale = sale;
    }

    // Getters and Setters

    /** get sales order to which the item belongs
     * @return the sales order
     */
    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    /** set sales order to which the item belongs
     * @param salesOrder the sales order
     */
    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    /** get product being ordered
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /** set product being ordered
     * @param product the product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /** get quantity of the product being ordered
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /** set quantity of the product being ordered
     * @param quantity the quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /** get unit price of the product
     * @return the unit price
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /** set unit price of the product
     * @param unitPrice the unit price
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /** get sale percentage of the product
     * @return the sale percentage
     */
    public double getSale() {
        return sale;
    }

    /** set sale percentage of the product
     * @param sale the sale percentage
     */
    public void setSale(double sale) {
        this.sale = sale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesOrderItem that = (SalesOrderItem) o;
        return orderId == that.orderId &&
                productId == that.productId &&
                quantity == that.quantity &&
                Double.compare(that.unitPrice, unitPrice) == 0 &&
                Objects.equals(salesOrder, that.salesOrder) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId, salesOrder, product, quantity, unitPrice);
    }

}
