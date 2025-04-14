package com.komeetta.datasource;

import com.komeetta.InitDBTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MariaDbJpaConnectionTest extends InitDBTest {

    @Test
    void testEntityManagerCreation() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        assertNotNull(em);
        assertTrue(em.isOpen());
        em.close();
    }
}
