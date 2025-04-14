package com.komeetta.dao;

import com.komeetta.InitDBTest;
import com.komeetta.model.Supplier;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SupplierDAOTest extends InitDBTest {

    private static SupplierDAO supplierDAO;

    @BeforeAll
    static void initDAO() {
        supplierDAO = new SupplierDAO();
    }

    @BeforeEach
    void cleanUp() {
        truncate("Supplier");
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
}
