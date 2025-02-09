package com.komeetta.model;

/** UserRole enum represents the role of a user in the system. It has the following values:
 * - ADMIN: the user has administrative privileges
 * - USER: the user is a regular user
 */

public enum UserRole {
    ADMIN {
        @Override
        public String toString() {
            return "Admin";
        }
    },
    USER {
        @Override
        public String toString() {
            return "User";
        }
    }
}
