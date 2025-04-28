package com.komeetta.dao;

import com.komeetta.datasource.MariaDbJpaConnection;
import com.komeetta.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.util.Arrays;
import java.util.List;

/**
 * Data Access Object for Product
 */

public class ProductDAO {

    /**
     * Adds a new product to the database
     * @param product A new product
     */
    public void addProduct(Product product) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.persist(product);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add product", e);
        } finally {
            em.close();
        }

    }

    /**
     * Fetches a product by product ID
     * @param productId Product ID
     * @return Product object
     */
    public Product getProductById(int productId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(Product.class, productId);
        } finally {
            em.close();
        }
    }

    /**
     * Updates a product in the database
     * @param product Product to update
     */
    public void updateProduct(Product product) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.merge(product);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update product", e);
        } finally {
            em.close();
        }

    }

    /**
     * Fetches all products
     * @return List of products
     */
    public List<Product> getProducts() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
            em.getTransaction().commit();
            return products;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to retrieve products", e);
        } finally {
            em.close();
        }
    }

    /**
     * Deletes a product from the database
     * @param productId Product ID
     */
    public void deleteProduct(int productId) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            Product product = em.find(Product.class, productId);
            em.remove(product);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete product", e);
        } finally {
            em.close();
        }
    }

    /**
     * Deletes a product from the database
     * @param product Product to delete
     */
    public void deleteProduct(Product product) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            Product managedProduct = em.merge(product); // Make sure it’s managed
            em.remove(managedProduct);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete product", e);
        } finally {
            em.close();
        }
    }

    /**
     * Deletes all products (for testing purposes)
     */
    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Product").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete all products", e);
        } finally {
            em.close();
        }
    }

    /**
     * Bulk upload products from a CSV file
     * @param filePath Path to the CSV file
     */

    public static void bulkUploadFromCsv(String filePath) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            transaction.begin(); // Start transaction

            String[] data;
            int batchSize = 30; // Adjust based on performance testing
            int count = 0;

            reader.readNext(); // Skip header if present

            while ((data = reader.readNext()) != null) {
                // osassa csv filejä mitä tein oli ; eikä , joten muutetaan kaikki ; -> ,
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].replace(';', ',');
                }

                data = String.join(",", data).split(",");

                if (data.length < 5) {
                    System.err.println("Skipping line due to insufficient data: " + Arrays.toString(data));
                    continue;
                }

                Product product = new Product();
                product.setName(data[0].trim());
                product.setCategory(data[1].trim());
                product.setBrand(data[2].trim());
                product.setQuantity(Integer.parseInt(data[3].trim()));
                product.setDescription(data[4].trim());

                em.persist(product); // Persist entity

                if (++count % batchSize == 0) {
                    em.flush();  // Flush changes to database
                    em.clear();  // Clear the persistence context
                }
            }

            transaction.commit(); // Commit transaction
            System.out.println("CSV file data uploaded successfully!");

        } catch (IOException | CsvValidationException e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Rollback only if the transaction is active
            }
            e.printStackTrace();
        } finally {
            em.close(); // Close EntityManager
        }
    }

}
