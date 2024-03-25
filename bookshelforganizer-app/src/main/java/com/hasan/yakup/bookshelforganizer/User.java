package com.hasan.yakup.bookshelforganizer;

import java.io.Serializable;

/**
 * Represents a user in the bookshelf organizer system.
 * Each user has a unique ID, email, password, name, and surname.
 */
public class User implements Serializable {
    /**
     * The unique ID of the user.
     */
    private Integer id;

    /**
     * The email of the user.
     */
    private String email;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The surname of the user.
     */
    private String surname;

    /**
     * Constructs a new User object.
     */
    public User() {
    }

    /**
     * Constructs a new User object with specified parameters.
     *
     * @param id       The unique ID of the user.
     * @param name     The name of the user.
     * @param surname  The surname of the user.
     * @param email    The email of the user.
     * @param password The password of the user.
     */
    public User(Integer id, String name, String surname, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    /**
     * Gets the ID of the user.
     *
     * @return The ID of the user.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the user.
     *
     * @param id The ID of the user.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email of the user.
     *
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email The email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the surname of the user.
     *
     * @return The surname of the user.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname of the user.
     *
     * @param surname The surname of the user.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
}
