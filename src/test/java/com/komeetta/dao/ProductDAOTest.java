package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.Product;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest {
    private static EntityManager entityManager;
    private static ProductDAO productDAO;

    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));
        entityManager = MariaDbJpaConnection.getInstance();
        productDAO = new ProductDAO();
    }

    @BeforeEach
    void cleanUp() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery("DELETE FROM Product").executeUpdate();
        transaction.commit();
    }

    @Test
    void addProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setBrand("Test Brand");

        productDAO.addProduct(product);

        Product fetchedProduct = productDAO.getProductById(product.getProductId());
        assertNotNull(fetchedProduct);
        assertEquals("Test Product", fetchedProduct.getName());
        assertEquals("Test Brand", fetchedProduct.getBrand());
        assertEquals(0, fetchedProduct.getStockQuantity());
    }

    @Test
    void getProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setStockQuantity(50);

        productDAO.addProduct(product);

        Product fetchedProduct = productDAO.getProductById(product.getProductId());
        assertNotNull(fetchedProduct);
        assertEquals("Test Product", fetchedProduct.getName());
        assertEquals(50, fetchedProduct.getStockQuantity());
    }

    @Test
    void updateProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setStockQuantity(50);

        productDAO.addProduct(product);

        product.setName("Updated Product");
        product.setStockQuantity(150);
        productDAO.updateProduct(product);

        Product updatedProduct = productDAO.getProductById(product.getProductId());
        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(150, updatedProduct.getStockQuantity());
    }

    @Test
    void deleteProduct() {
        Product product = new Product();
        product.setName("Test Product");

        productDAO.addProduct(product);
        productDAO.deleteProduct(product.getProductId());

        Product deletedProduct = productDAO.getProductById(product.getProductId());
        assertNull(deletedProduct);
    }

    @Test
    void deleteAll() {
        Product product1 = new Product();
        product1.setName("Test Product 1");
        product1.setStockQuantity(100);

        Product product2 = new Product();
        product2.setName("Test Product 2");
        product2.setStockQuantity(200);

        productDAO.addProduct(product1);
        productDAO.addProduct(product2);

        productDAO.deleteAll();

        assertNull(productDAO.getProductById(product1.getProductId()));
        assertNull(productDAO.getProductById(product2.getProductId()));
    }

    @AfterAll
    static void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}