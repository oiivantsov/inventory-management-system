package com.komeetta.dao;

import com.komeetta.model.User;
import com.komeetta.model.UserRole;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private static UserDAO userDAO;
    private static EntityManager entityManager;
    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("TEST_JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("TEST_JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("TEST_JDBC_PASSWORD"));

        userDAO = new UserDAO();
        emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnitTesting");
        entityManager = emf.createEntityManager();
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
        assertNotEquals("testPassword", fetchedUser.getPassword()); // Ensure password is encrypted
        assertEquals(UserRole.USER, fetchedUser.getRole());
    }

    @Test
    void testAuthenticateUser() {
        User user = new User("authUser", "securePassword");
        userDAO.addUser(user);

        assertTrue(userDAO.authenticate("authUser", "securePassword"));
        assertFalse(userDAO.authenticate("authUser", "wrongPassword"));
        assertFalse(userDAO.authenticate("nonExistingUser", "securePassword"));
    }

    @Test
    void testIsUsernameAvailable() {
        User user = new User("uniqueUser", "password123");
        userDAO.addUser(user);

        assertFalse(userDAO.isUsernameAvailable("uniqueUser"));
        assertTrue(userDAO.isUsernameAvailable("newUser"));
    }

    @Test
    void testDeleteAllUsers() {
        User user1 = new User("User1", "pass1");
        User user2 = new User("User2", "pass2");

        userDAO.addUser(user1);
        userDAO.addUser(user2);

        userDAO.deleteAll();

        assertNull(userDAO.getUser("User1"));
        assertNull(userDAO.getUser("User2"));
        assertTrue(userDAO.isUsernameAvailable("User1"));
        assertTrue(userDAO.isUsernameAvailable("User2"));
    }

    @AfterAll
    static void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
