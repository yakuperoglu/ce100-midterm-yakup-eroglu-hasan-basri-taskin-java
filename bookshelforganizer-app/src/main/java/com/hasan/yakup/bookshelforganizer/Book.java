package com.hasan.yakup.bookshelforganizer;

import java.io.Serializable;
/**
 * Represents a book in the bookshelf organizer system.
 * Each book has a unique ID, title, author, genre, owner, borrowing status, and cost.
 */
public class Book implements Serializable {
    /**
     * The unique ID of the book.
     */
    private Integer id;
    
    /**
     * The title of the book.
     */
    private String title;
    
    /**
     * The author of the book.
     */
    private String author;
    
    /**
     * The genre of the book.
     */
    private String genre;
    
    /**
     * The owner of the book.
     */
    private User owner;
    
    /**
     * The borrowing status of the book.
     */
    private boolean isBorrowed;
    
    /**
     * The cost of the book.
     */
    private Integer cost;

    /**
     * Constructs a new Book object.
     */
    public Book() {
    }

    /**
     * Constructs a new Book object with specified parameters.
     *
     * @param id     The unique ID of the book.
     * @param title  The title of the book.
     * @param author The author of the book.
     * @param genre  The genre of the book.
     * @param owner  The owner of the book.
     * @param cost   The cost of the book.
     */
    public Book(Integer id, String title, String author, String genre, User owner, Integer cost) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.owner = owner;
        this.cost = cost;
        this.isBorrowed = false;
    }

    /**
     * Gets the title of the book.
     *
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title The title of the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the ID of the book.
     *
     * @return The ID of the book.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the book.
     *
     * @param id The ID of the book.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the author of the book.
     *
     * @return The author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     *
     * @param author The author of the book.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the genre of the book.
     *
     * @return The genre of the book.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the book.
     *
     * @param genre The genre of the book.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the owner of the book.
     *
     * @return The owner of the book.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the book.
     *
     * @param owner The owner of the book.
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Checks if the book is borrowed.
     *
     * @return True if the book is borrowed, false otherwise.
     */
    public Boolean isBorrowed() {
        return this.isBorrowed;
    }

    /**
     * Sets the borrowing status of the book.
     *
     * @param status The borrowing status of the book.
     */
    public void setIsBorrowed(Boolean status) {
        this.isBorrowed = status;
    }

    /**
     * Sets the price of the book.
     *
     * @param cost The price of the book.
     */
    public void setPrice(Integer cost) {
        this.cost = cost;
    }

    /**
     * Gets the price of the book.
     *
     * @return The price of the book.
     */
    public Integer getPrice() {
        return cost;
    }
}
