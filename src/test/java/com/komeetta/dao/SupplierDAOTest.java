package com.komeetta.dao;

import com.komeetta.model.Customer;
import com.komeetta.model.Supplier;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SupplierDAOTest {

    private static SupplierDAO supplierDAO;
    private static EntityManager entityManager;
    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));
        supplierDAO = new SupplierDAO();
        emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnitTesting");
        entityManager = emf.createEntityManager();
    }

    @BeforeEach
    void beforeEach() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery("DELETE FROM Supplier").executeUpdate();
        transaction.commit();
    }

    @Test
    void testAddSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setEmail("test@example.com");
        supplier.setPhoneNumber("123456789");
        supplier.setAddress("Test Address");

        supplierDAO.addSupplier(supplier);

        List<Supplier> suppliers = supplierDAO.getSuppliers();
        assertFalse(suppliers.isEmpty());
        assertEquals("Test Supplier", suppliers.get(0).getName());
    }

    @Test
    void testGetSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setEmail("test@supplier.com");
        supplier.setPhoneNumber("123456789");
        supplier.setAddress("Supplier Address");
        supplierDAO.addSupplier(supplier);

        Supplier fetchedSupplier = supplierDAO.getSupplier(supplier.getSupplierId());

        assertNotNull(fetchedSupplier);
        assertEquals("Test Supplier", fetchedSupplier.getName());
    }

    @Test
    void testGetNonExistentCustomer() {
        Supplier fetchedSupplier = supplierDAO.getSupplier(99999); // Assuming 99999 doesn't exist
        assertNull(fetchedSupplier, "Fetching a non-existent supplier should return null.");
    }

    @Test
    void testUpdateSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName("Old Supplier");
        supplierDAO.addSupplier(supplier);

        supplier.setName("Updated Supplier");
        supplierDAO.updateSupplier(supplier);

        Supplier updatedSupplier = supplierDAO.getSupplier(supplier.getSupplierId());
        assertEquals("Updated Supplier", updatedSupplier.getName());
    }

    @Test
    void testDeleteSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName("To Be Deleted");
        supplierDAO.addSupplier(supplier);

        supplierDAO.deleteSupplier(supplier);
        Supplier deletedSupplier = supplierDAO.getSupplier(supplier.getSupplierId());

        assertNull(deletedSupplier);
    }


    @Test
    void testDeleteAllSuppliers() {
        Supplier supplier1 = new Supplier();
        supplier1.setName("Supplier 1");
        Supplier supplier2 = new Supplier();
        supplier2.setName("Supplier 2");

        supplierDAO.addSupplier(supplier1);
        supplierDAO.addSupplier(supplier2);

        supplierDAO.deleteAll();

        List<Supplier> suppliers = supplierDAO.getSuppliers();
        assertTrue(suppliers.isEmpty());
    }

    @Test
    void testUpdateNonExistentSupplier() {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(99999); // Fake ID
        supplier.setName("Should Not Exist");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            supplierDAO.updateSupplier(supplier);
        });

        assertTrue(exception.getMessage().contains("Failed to update supplier"),
                "Updating a non-existent supplier should throw an error.");
    }

    @Test
    void testDeleteNonExistentSupplier() {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(99999); // Fake ID

        Exception exception = assertThrows(RuntimeException.class, () -> {
            supplierDAO.deleteSupplier(supplier);
        });

        assertTrue(exception.getMessage().contains("Failed to delete supplier"),
                "Deleting a non-existent supplier should throw an error.");
    }


    @AfterAll
    static void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
