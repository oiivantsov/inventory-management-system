// BaseDAOTest.java
package com.komeetta;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class InitDBTest {
    protected static EntityManagerFactory emf;
    protected static EntityManager entityManager;

    @BeforeAll
    public static void initEntityManager() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));

        emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnitTesting");
        entityManager = emf.createEntityManager();
    }

    protected void truncate(String... entitiesInDeleteOrder) {
        EntityTransaction tx = entityManager.getTransaction();
        if (!tx.isActive()) tx.begin();

        for (String entity : entitiesInDeleteOrder) {
            entityManager.createQuery("DELETE FROM " + entity).executeUpdate();
        }

        tx.commit();
    }

    @AfterAll
    static void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }

}
