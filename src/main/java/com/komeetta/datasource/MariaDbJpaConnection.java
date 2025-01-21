package com.komeetta.datasource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Class for managing the EntityManager instances for MariaDB.
 */
public class MariaDbJpaConnection {

    private static EntityManagerFactory emf;

    /**
     * Initializes the EntityManagerFactory if not already initialized.
     * Singleton pattern for EntityManagerFactory.
     */
    private static synchronized void initializeFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnit");
        }
    }

    /**
     * Returns a new EntityManager instance.
     */
    public static EntityManager getInstance() {
        initializeFactory(); // Ensure the factory is initialized
        return emf.createEntityManager();
    }

    /**
     * Closes the EntityManagerFactory.
     */
    public static void closeFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
