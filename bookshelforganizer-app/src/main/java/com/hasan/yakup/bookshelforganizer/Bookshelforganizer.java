package com.hasan.yakup.bookshelforganizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * @brief Bookshelforganizer class for managing bookshelf operations.
 */
public class Bookshelforganizer {
    /** Scanner object for user input. */
    public Scanner scanner;
    /** PrintStream object for output. */
    public PrintStream out;
    /** Currently logged-in user. */
    public User loggedUser;
    /** Indicates whether the program is in test mode or not. */
    public boolean isTestMode = false;

    /**
     * @brief Sets the logged-in user.
     * @param user The user to be logged in.
     * @return Always returns true to indicate successful user login.
     */
    public Boolean setLoggedUser(User user) {
        this.loggedUser = user;
        return true;
    }

    /**
     * @brief Constructor for Bookshelforganizer.
     * @param scanner Scanner object for user input.
     * @param out     PrintStream object for output.
     */
    public Bookshelforganizer(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    /**
     * @brief Clears the console screen.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws IOException          If an I/O error occurs.
     */
    public void clearScreen() throws InterruptedException, IOException {
        String operatingSystem = System.getProperty("os.name");
        if (operatingSystem.contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            out.print("\033[H\033[2J");
            out.flush();
        }
    }

    /**
     * @brief Handles input errors by displaying a message to the user.
     * @return Always returns false to indicate an input error.
     */
    public boolean handleInputError() {
        out.println("Only enter numerical value");
        return false;
    }

    /**
     * @brief Attempts to parse the given string into an integer.
     * @param value The string to be parsed.
     * @return The parsed integer if successful, or -1 if parsing fails.
     */
    public int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
