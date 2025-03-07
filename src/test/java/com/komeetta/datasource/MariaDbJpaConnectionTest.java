package com.komeetta.datasource;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MariaDbJpaConnectionTest {

    @Test
    void testEntityManagerCreation() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        assertNotNull(em);
        assertTrue(em.isOpen());
        em.close();
    }
}
