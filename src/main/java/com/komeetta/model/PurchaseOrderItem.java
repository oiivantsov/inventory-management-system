package com.komeetta.model;

import jakarta.persistence.*;

/**
 * Represents an item in a purchase order
 * It has the following attributes:
 * - purchaseOrder: the purchase order to which the item belongs
 * - product: the product being ordered
 * - quantity: the quantity of the product being ordered
 * - unitPrice: the unit price of the product
 * - sale: the sale percentage of the product
 */

@Entity
@Table(name = "PurchaseOrderItems")
public class PurchaseOrderItem {
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private PurchaseOrder purchaseOrder;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private double unitPrice;

    @Column(name = "sale", columnDefinition = "DOUBLE DEFAULT 0")
    private double sale;

    /** Default constructor
     * (required by JPA)
     * */
    public PurchaseOrderItem() {
    }

    /** Parameterized constructor
     * @param purchaseOrder the purchase order to which the item belongs
     * @param product the product being ordered
     * @param quantity the quantity of the product being ordered
     * @param unitPrice the unit price of the product
     * @param sale the sale percentage of the product
     */
    public PurchaseOrderItem(PurchaseOrder purchaseOrder, Product product, int quantity, double unitPrice, double sale) {
        this.purchaseOrder = purchaseOrder;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.sale = sale;
    }

    // Getters and Setters
    /** get purchase order that the item belongs to
     * @return the purchase order
     */
    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    /** set purchase order that the item belongs to
     * @param purchaseOrder the purchase order
     */
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
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

}