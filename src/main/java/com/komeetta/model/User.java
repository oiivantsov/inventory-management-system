package com.komeetta.model;

import jakarta.persistence.*;

/**
 *
 * User data
 */

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "ENUM('ADMIN', 'USER', DEFAULT 'USER')")
    private UserRole role; // e.g., "ADMIN", "USER"

    /**
     * Default constructor for the Distribution class.
     * Required by JPA for entity instantiation.
     */
    public User() {}

    /**
     * Parameterized constructor
     * @param username
     * @param password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
