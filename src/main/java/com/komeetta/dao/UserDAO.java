package com.komeetta.dao;

import jakarta.persistence.EntityManager;
import java.util.Base64;
import com.komeetta.model.User;
import com.komeetta.datasource.MariaDbJpaConnection;

public class UserDAO {

    /**
     * Adds a new user with an encrypted password
     * @param user A new user
     */
    public void addUser(User user) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            if (!isUsernameAvailable(user.getUsername())) {
                throw new RuntimeException("Username is already taken.");
            }

            // Encrypt the password before storing
            String encryptedPassword = encrypt(user.getPassword());
            user.setPassword(encryptedPassword);

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
     * Authenticates a user during login by decrypting the stored password
     * @param username Username
     * @param password Plain text password
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            User user = getUser(username);
            return user != null && decrypt(user.getPassword()).equals(password);
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

    // Encryption and decryption methods

    /**
     * Encrypts data by adding 1 to each byte and encoding it with Base64
     * @param data Plain text data
     * @return Encrypted data as a Base64 string
     */
    private String encrypt(String data) {
        byte[] result = data.getBytes();
        for (int i = 0; i < result.length; i++) {
            result[i] += (byte) 1;
        }
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * Decrypts data by decoding from Base64 and subtracting 1 from each byte
     * @param data Encrypted Base64 string
     * @return Decrypted plain text data
     */
    private String decrypt(String data) {
        byte[] result = Base64.getDecoder().decode(data);
        for (int i = 0; i < result.length; i++) {
            result[i] -= (byte) 1;
        }
        return new String(result);
    }
}
