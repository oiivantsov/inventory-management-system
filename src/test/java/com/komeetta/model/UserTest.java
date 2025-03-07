package com.komeetta.model;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static EntityManager entityManager;
    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));

        emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnitTesting");
        entityManager = emf.createEntityManager();
    }

    @BeforeEach
    void cleanUp() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery("DELETE FROM User").executeUpdate();
        transaction.commit();
    }

    @Test
    void testUserEntityCreation() {
        User user = new User("testUser", "securePassword");

        assertEquals("testUser", user.getUsername());
        assertEquals("securePassword", user.getPassword());
        assertEquals(UserRole.USER, user.getRole()); // Default role should be USER
    }

    @Test
    void testUserPersistence() {
        User user = new User("admin", "adminPass", UserRole.ADMIN);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();

        User retrievedUser = entityManager.find(User.class, "admin");
        assertNotNull(retrievedUser);
        assertEquals("admin", retrievedUser.getUsername());
        assertEquals("adminPass", retrievedUser.getPassword());
        assertEquals(UserRole.ADMIN, retrievedUser.getRole());
    }

    @Test
    void testUpdateUser() {
        User user = new User("user123", "pass123", UserRole.USER);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();

        transaction.begin();
        User existingUser = entityManager.find(User.class, "user123");
        existingUser.setPassword("newPass123");
        existingUser.setRole(UserRole.ADMIN);
        entityManager.merge(existingUser);
        transaction.commit();

        User updatedUser = entityManager.find(User.class, "user123");
        assertNotNull(updatedUser);
        assertEquals("newPass123", updatedUser.getPassword());
        assertEquals(UserRole.ADMIN, updatedUser.getRole());
    }

    @Test
    void testDeleteUser() {
        User user = new User("deleteMe", "password");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();

        transaction.begin();
        User userToDelete = entityManager.find(User.class, "deleteMe");
        entityManager.remove(userToDelete);
        transaction.commit();

        User deletedUser = entityManager.find(User.class, "deleteMe");
        assertNull(deletedUser);
    }

    @AfterAll
    static void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
