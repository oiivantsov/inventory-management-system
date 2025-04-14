package com.komeetta.model;

import com.komeetta.InitDBTest;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest extends InitDBTest {

    @BeforeEach
    void cleanUp() {
        truncate("PurchaseOrderItem", "SalesOrderItem", "PurchaseOrder", "SalesOrder", "Product");
    }

    @Test
    void testProductEntityCreation() {
        Product product = new Product("Laptop", "Electronics", "Dell", "A powerful laptop", 10);

        assertEquals("Laptop", product.getName());
        assertEquals("Electronics", product.getCategory());
        assertEquals("Dell", product.getBrand());
        assertEquals("A powerful laptop", product.getDescription());
        assertEquals(10, product.getQuantity());
    }

    @Test
    void testProductPersistence() {
        Product product = new Product("Smartphone", "Electronics", "Samsung", "Latest model", 20);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(product);
        transaction.commit();

        assertNotNull(product.getProductId()); // Ensure ID is assigned
        Product retrievedProduct = entityManager.find(Product.class, product.getProductId());
        assertNotNull(retrievedProduct);
        assertEquals("Smartphone", retrievedProduct.getName());
        assertEquals("Electronics", retrievedProduct.getCategory());
        assertEquals(20, retrievedProduct.getQuantity());
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product("Tablet", "Electronics", "Apple", "iPad Air", 5);

        // Persist product
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(product);
        transaction.commit();

        // Modify product
        transaction.begin();
        product.setName("Tablet Pro");
        product.setQuantity(15);
        entityManager.merge(product);
        transaction.commit();

        // Verify update
        Product updatedProduct = entityManager.find(Product.class, product.getProductId());
        assertNotNull(updatedProduct);
        assertEquals("Tablet Pro", updatedProduct.getName());
        assertEquals(15, updatedProduct.getQuantity());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product("Headphones", "Audio", "Sony", "Wireless headphones", 30);

        // Persist product
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(product);
        transaction.commit();

        // Delete product
        transaction.begin();
        Product productToDelete = entityManager.find(Product.class, product.getProductId());
        entityManager.remove(productToDelete);
        transaction.commit();

        // Verify deletion
        Product deletedProduct = entityManager.find(Product.class, product.getProductId());
        assertNull(deletedProduct);
    }

}
