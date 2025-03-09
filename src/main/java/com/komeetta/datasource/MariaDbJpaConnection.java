package com.komeetta.datasource;

import io.github.cdimascio.dotenv.Dotenv;
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
        Dotenv dotenv = Dotenv.load();
        String persistenceUnit = dotenv.get("DB_MODE");
        System.out.println("DB_MODE: " + persistenceUnit);
        if (emf == null) {
            if ("reset".equalsIgnoreCase(persistenceUnit)) {
                emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnitDummy");
                System.out.println("Using persistence unit: CompanyMariaDbUnitTesting (drop-and-create)");
            } else {
                emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnit");
                System.out.println("Using persistence unit: CompanyMariaDbUnit (no reset)");
            }
        }
    }

    /**
     * Returns a new EntityManager instance.
     */
    public static EntityManager getInstance() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_MODE", dotenv.get("DB_MODE"));
        initializeFactory(); // Ensure the factory is initialized
        return emf.createEntityManager();
    }
}
