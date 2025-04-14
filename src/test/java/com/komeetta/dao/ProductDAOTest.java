package com.komeetta.dao;

import com.komeetta.InitDBTest;
import com.komeetta.model.Product;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest extends InitDBTest {
    private static ProductDAO productDAO;

    @BeforeAll
    static void setupDAO() {
        productDAO = new ProductDAO();
    }

    @BeforeEach
    void cleanUp() {
        truncate("Product");
    }

    @Test
    void addProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setBrand("Test Brand");
        product.setDescription("Test Description");
        product.setCategory("Test Category");
        product.setQuantity(0);

        productDAO.addProduct(product);

        Product fetchedProduct = productDAO.getProductById(product.getProductId());
        assertNotNull(fetchedProduct);
        assertEquals("Test Product", fetchedProduct.getName());
        assertEquals("Test Brand", fetchedProduct.getBrand());
        assertEquals(0, fetchedProduct.getQuantity());
    }

    @Test
    void bulkUploadFromCsv() {
        productDAO.bulkUploadFromCsv("src/main/resources/BulkTest.csv");
        List<Product> products = productDAO.getProducts(); // to see if there is that many products indeed
        assertEquals(36, products.size()); // 36 products in the csv file
    }

    @Test
    void getProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setQuantity(50);

        productDAO.addProduct(product);

        Product fetchedProduct = productDAO.getProductById(product.getProductId());
        assertNotNull(fetchedProduct);
        assertEquals("Test Product", fetchedProduct.getName());
        assertEquals(50, fetchedProduct.getQuantity());
    }

    @Test
    void getProduct_ShouldReturnNullIfNotFound() {
        Product fetchedProduct = productDAO.getProductById(99999);
        assertNull(fetchedProduct);
    }

    @Test
    void updateProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setQuantity(50);

        productDAO.addProduct(product);

        product.setName("Updated Product");
        product.setQuantity(150);
        productDAO.updateProduct(product);

        Product updatedProduct = productDAO.getProductById(product.getProductId());
        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(150, updatedProduct.getQuantity());
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
    void deleteNonExistentProduct_ShouldThrowException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productDAO.deleteProduct(99999);
        });

        assertTrue(exception.getMessage().contains("Failed to delete product"),
                "Deleting a non-existent product should throw an error.");
    }

    @Test
    void deleteAll() {
        Product product1 = new Product();
        product1.setName("Test Product 1");
        product1.setQuantity(100);

        Product product2 = new Product();
        product2.setName("Test Product 2");
        product2.setQuantity(200);

        productDAO.addProduct(product1);
        productDAO.addProduct(product2);

        productDAO.deleteAll();

        assertNull(productDAO.getProductById(product1.getProductId()));
        assertNull(productDAO.getProductById(product2.getProductId()));
    }

    @Test
    void getProducts_ShouldReturnAllProducts() {
        Product product1 = new Product();
        product1.setName("Product A");
        product1.setBrand("Brand A");
        product1.setCategory("Category A");
        product1.setQuantity(10);
        productDAO.addProduct(product1);

        Product product2 = new Product();
        product2.setName("Product B");
        product2.setBrand("Brand B");
        product2.setCategory("Category B");
        product2.setQuantity(5);
        productDAO.addProduct(product2);

        List<Product> products = productDAO.getProducts();

        assertEquals(2, products.size());
        assertEquals("Product A", products.get(0).getName());
        assertEquals("Product B", products.get(1).getName());
    }

}