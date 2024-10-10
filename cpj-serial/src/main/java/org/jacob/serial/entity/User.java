package org.jacob.serial.entity;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a User with a username and a password.
 * The username is serialized, but the password is marked as transient and is not serialized.
 *
 * @author Kotohiko
 * @since 00:54 Oct 10, 2024
 */
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The username of the user.
     */
    private final String username;

    /**
     * The password of the user. Marked as transient to prevent serialization.
     */
    private final transient String password;

    /**
     * Constructs a new User with the specified username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns a string representation of the User object.
     *
     * @return A string representation of the User object.
     */
    @Override
    public String toString() {
        return "User{username='" + username + "', password='" + password + "'}";
    }
}

