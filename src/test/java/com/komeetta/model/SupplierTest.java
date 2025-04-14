package com.komeetta.model;

import com.komeetta.InitDBTest;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest extends InitDBTest {

    @BeforeEach
    void cleanUp() {
        truncate("Supplier");
    }

    @Test
    void testSupplierEntityCreation() {
        Supplier supplier = new Supplier();
        supplier.setName("Tech Supplies Ltd.");
        supplier.setEmail("contact@techsupplies.com");
        supplier.setPhoneNumber("987654321");
        supplier.setAddress("789 Industrial Park");

        assertEquals("Tech Supplies Ltd.", supplier.getName());
        assertEquals("contact@techsupplies.com", supplier.getEmail());
        assertEquals("987654321", supplier.getPhoneNumber());
        assertEquals("789 Industrial Park", supplier.getAddress());
    }

    @Test
    void testSupplierPersistence() {
        Supplier supplier = new Supplier();
        supplier.setName("Hardware Distributors");
        supplier.setEmail("support@hardware.com");
        supplier.setPhoneNumber("123456789");
        supplier.setAddress("456 Elm St");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(supplier);
        transaction.commit();

        assertNotNull(supplier.getSupplierId()); // Ensure ID is assigned
        Supplier retrievedSupplier = entityManager.find(Supplier.class, supplier.getSupplierId());
        assertNotNull(retrievedSupplier);
        assertEquals("Hardware Distributors", retrievedSupplier.getName());
        assertEquals("support@hardware.com", retrievedSupplier.getEmail());
    }

    @Test
    void testUpdateSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName("Electro Parts Ltd.");
        supplier.setEmail("info@electroparts.com");
        supplier.setPhoneNumber("111222333");
        supplier.setAddress("654 Maple Ave");

        // Persist supplier
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(supplier);
        transaction.commit();

        // Modify supplier
        transaction.begin();
        supplier.setName("Electro Components Ltd.");
        entityManager.merge(supplier);
        transaction.commit();

        // Verify update
        Supplier updatedSupplier = entityManager.find(Supplier.class, supplier.getSupplierId());
        assertNotNull(updatedSupplier);
        assertEquals("Electro Components Ltd.", updatedSupplier.getName());
    }

    @Test
    void testDeleteSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName("Wholesale Suppliers");
        supplier.setEmail("sales@wholesale.com");
        supplier.setPhoneNumber("555666777");
        supplier.setAddress("321 Market St");

        // Persist supplier
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(supplier);
        transaction.commit();

        // Delete supplier
        transaction.begin();
        Supplier supplierToDelete = entityManager.find(Supplier.class, supplier.getSupplierId());
        entityManager.remove(supplierToDelete);
        transaction.commit();

        // Verify deletion
        Supplier deletedSupplier = entityManager.find(Supplier.class, supplier.getSupplierId());
        assertNull(deletedSupplier);
    }
}
