package com.hasan.yakup.bookshelforganizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * The main class for the Bookshelf Organizer application.
 * This class initializes the application, creates necessary file paths,
 * and invokes the main menu of the Bookshelf Organizer system.
 * 
 * @author Hasan And Yakup
 * @version 1.0
 * @since 2024-03-21
 */
public class BookshelforganizerApp {
    /**
     * The main method of the application.
     * It initializes the necessary components, including scanner and bookshelf
     * organizer,
     * specifies file paths, and invokes the main menu of the Bookshelf Organizer
     * system.
     * 
     * @param args Command-line arguments (not used in this application).
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws InterruptedException   If a thread is interrupted.
     * @throws ClassNotFoundException If a class is not found during
     *                                deserialization.
     */
    public static void main(String[] args)
            throws FileNotFoundException, IOException, InterruptedException, ClassNotFoundException {
        Scanner inputScanner = new Scanner(System.in);
        Bookshelforganizer bookshelforganizer = new Bookshelforganizer(inputScanner, System.out);
        String pathFileBooks = "books.bin";
        String pathFileUsers = "users.bin";
        String pathFileWishlist = "wishlists.bin";
        String pathFileLendingHistories = "lendinghistories.bin";
        bookshelforganizer.mainMenu(pathFileUsers, pathFileBooks, pathFileLendingHistories, pathFileWishlist);
    }
}
