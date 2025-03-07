package com.komeetta.dao;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import com.komeetta.model.User;
import com.komeetta.datasource.MariaDbJpaConnection;

public class UserDAO {

    /**
     * Adds a new user
     * @param user A new user
     */
    public void addUser(User user) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            if (!isUsernameAvailable(user.getUsername())) {
                throw new RuntimeException("Username is already taken.");
            }
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add user", e);
        } finally {
            em.close();
        }
    }

    /**
     * Authenticates a user during login by checking the username and password
     * @param username Username
     * @param password Plain text password
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            User user = em.find(User.class, username);
            return user != null && user.getPassword().equals(password);
        } finally {
            em.close();
        }
    }

    /**
     * Checks if the username is available (not already taken)
     * @param username Username to check
     * @return true if the username is available, false otherwise
     */
    public boolean isUsernameAvailable(String username) {
        return getUser(username) == null;
    }

    /**
     * Fetches a user by username
     * @param username Username
     * @return User object or null if the user does not exist
     */
    public User getUser(String username) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(User.class, username);
        } finally {
            em.close();
        }
    }

    /**
     * Deletes all users (for testing purposes)
     */
    public void deleteAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
