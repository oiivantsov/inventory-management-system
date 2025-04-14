package com.komeetta.dao;

import com.komeetta.InitDBTest;
import com.komeetta.model.User;
import com.komeetta.model.UserRole;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest extends InitDBTest {

    private static UserDAO userDAO;

    @BeforeAll
    static void setUp() {
        userDAO = new UserDAO();
    }

    @BeforeEach
    void cleanUp() {
        truncate("User");
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

}
