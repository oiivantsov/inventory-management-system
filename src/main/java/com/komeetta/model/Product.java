package com.komeetta.model;

import jakarta.persistence.*;

/**
 * Represents a product entity in the database.
 * It has the following attributes:
 * - productId: the unique identifier of the product
 * - name: the name of the product
 * - category: the category of the product
 * - brand: the brand of the product
 * - description: the description of the product
 * - quantity: the quantity of the product in stock
 */
@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "brand")
    private String brand;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "quantity ", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int quantity ;

    // Constructors
    /** Default constructor (required by JPA). */
    public Product() {}

    /**
     * Parameterized constructor to create a product with the specified details.
     *
     * @param name The name of the product.
     * @param category The category of the product.
     * @param brand The brand of the product.
     * @param description The description of the product.
     * @param quantity  The quantity of the product in stock.
     */
    public Product(String name, String category, String brand, String description, int quantity ) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.quantity  = quantity ;
    }

    public Product(String name, String category, String brand, String description) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.quantity  = 0;
    }

    // Getters and Setters

    /**
     * Returns the product ID.
     *
     * @return The product ID.
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the product ID.
     *
     * @param productId The product ID.
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Returns the name of the product.
     *
     * @return The name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name The name of the product.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the category of the product.
     *
     * @return The category of the product.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the product.
     *
     * @param category The category of the product.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the brand of the product.
     *
     * @return The brand of the product.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the product.
     *
     * @param brand The brand of the product.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the description of the product.
     *
     * @return The description of the product.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the product.
     *
     * @param description The description of the product.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the quantity of the product in stock.
     *
     * @return The quantity of the product in stock.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product in stock.
     *
     * @param quantity The quantity of the product in stock.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
