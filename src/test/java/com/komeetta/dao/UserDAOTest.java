package com.komeetta.dao;

import com.komeetta.model.User;
import com.komeetta.model.UserRole;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private static UserDAO userDAO;
    private static EntityManager entityManager;

    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));
        userDAO = new UserDAO();
        entityManager = com.komeetta.datasource.MariaDbJpaConnection.getInstance();
    }

    @BeforeEach
    void beforeEach() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery("DELETE FROM User").executeUpdate();
        transaction.commit();
    }

    @Test
    void testAddUser() {
        User user = new User("testUser", "testPassword");
        userDAO.addUser(user);

        User fetchedUser = userDAO.getUser("testUser");

        assertNotNull(fetchedUser);
        assertEquals("testUser", fetchedUser.getUsername());
        assertEquals("testPassword", fetchedUser.getPassword());
        assertEquals(UserRole.USER, fetchedUser.getRole());
    }

    @AfterAll
    static void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
