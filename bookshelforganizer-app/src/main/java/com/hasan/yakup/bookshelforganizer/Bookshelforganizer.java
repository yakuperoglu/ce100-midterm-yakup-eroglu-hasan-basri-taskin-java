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
    
    /**
     * @brief Waits for user input to continue the program.
     * @details Displays a message prompting the user to press any key to continue.
     */
    public boolean enterToContinue() {
      out.println("Press enter to continue...");
      if (!isTestMode) {
          scanner.nextLine();
      }
      return true;
  }

  /**
   * @brief Loads the prices of books owned by the logged-in user.
   * @param pathFileBooks The file path to the book data.
   * @return A 2D array containing the prices of owned books.
   * @throws FileNotFoundException  If the specified file is not found.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   * @throws IOException            If an I/O error occurs.
   */
  public int[][] loadOwnedBookPrices(String pathFileBooks)
          throws FileNotFoundException, ClassNotFoundException, IOException {
      List<Book> ownedBooks = loadOwnedBooks(pathFileBooks);
      int numBooks = ownedBooks.size();

      int[][] bookPrices = new int[1][numBooks]; // Assuming only one row for book prices

      // Populate book prices randomly
      for (int i = 0; i < numBooks; i++) {
          bookPrices[0][i] = ownedBooks.get(i).getPrice(); // Random price between 10 and 59
      }

      return bookPrices;
  }

  /**
   * @brief Loads the books owned by the logged-in user.
   * @param pathFileBooks The file path to the book data.
   * @return A list of books owned by the logged-in user.
   * @throws FileNotFoundException  If the specified file is not found.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public List<Book> loadOwnedBooks(String pathFileBooks)
          throws FileNotFoundException, IOException, ClassNotFoundException {

      return loadBooks(pathFileBooks, loggedUser.getId());
  }

  /**
   * @brief Loads the books wishlisted by the logged-in user.
   * @param pathFileWishlist The file path to the wishlist data.
   * @return A list of books wishlisted by the logged-in user.
   * @throws FileNotFoundException  If the specified file is not found.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public List<Book> loadWishlistedBooks(String pathFileWishlist)
          throws FileNotFoundException, IOException, ClassNotFoundException {

      return loadWishlist(pathFileWishlist, loggedUser.getId());
  }

  /**
   * @brief Loads the books excluding those owned by the logged-in user.
   * @param pathFileBooks The file path to the book data.
   * @return A list of books excluding those owned by the logged-in user.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public List<Book> loadBooksExcludingUser(String pathFileBooks)
          throws IOException, ClassNotFoundException {
      List<Book> allBooks = loadBooks(pathFileBooks);
      List<Book> filteredBooks = new ArrayList<>();

      for (Book book : allBooks) {
          if (!book.getOwner().getId().equals(loggedUser.getId())) {
              filteredBooks.add(book);
          }
      }

      return filteredBooks;
  }

  /**
   * @brief Loads the books owned by a specific user.
   * @param pathFileBooks The file path to the book data.
   * @param userId        The ID of the user whose books are to be loaded.
   * @return A list of books owned by the specified user.
   * @throws FileNotFoundException  If the specified file is not found.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public List<Book> loadBooks(String pathFileBooks, Integer userId)
          throws FileNotFoundException, IOException, ClassNotFoundException {
      List<Book> filteredBooks = new ArrayList<>();

      File file = new File(pathFileBooks);

      if (file.exists()) {
          try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathFileBooks))) {
              List<Book> allBooks = (List<Book>) ois.readObject();
              for (Book book : allBooks) {
                  if (book.getOwner().getId().equals(userId)) {
                      filteredBooks.add(book);
                  }
              }
          }
      }

      return filteredBooks;
  }

  /**
   * @brief Loads the books from the specified file.
   * @param pathFileBooks The file path to the book data.
   * @return A list of books loaded from the file, or an empty list if the file
   *         does not exist.
   * @throws FileNotFoundException  If the specified file is not found.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public List<Book> loadBooks(String pathFileBooks)
          throws FileNotFoundException, IOException, ClassNotFoundException {

      File file = new File(pathFileBooks);

      if (file.exists()) {
          try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathFileBooks))) {
              return (List<Book>) ois.readObject();
          }
      }
      return new ArrayList<>();
  }
  
    /**
     * @brief Loads the wishlist of books for the specified user.
     * @param pathFileWishlist The file path to the wishlist data.
     * @param userId           The ID of the user whose wishlist is to be loaded.
     * @return A list of books wishlisted by the specified user, or an empty list if
     *         the file does not exist.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public List<Book> loadWishlist(String pathFileWishlist, Integer userId)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        List<Book> filteredWishlist = new ArrayList<>();

        File file = new File(pathFileWishlist);

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathFileWishlist))) {
                List<Book> allWishlist = (List<Book>) ois.readObject();
                for (Book book : allWishlist) {
                    if (book.getOwner().getId().equals(userId)) {
                        filteredWishlist.add(book);
                    }
                }
            }
        }
        return filteredWishlist;
    }

    /**
     * @brief Loads the wishlist of books from the specified file.
     * @param pathFileWishlist The file path to the wishlist data.
     * @return A list of books wishlisted by users, or an empty list if the file
     *         does not exist.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public List<Book> loadWishlist(String pathFileWishlist)
            throws FileNotFoundException, IOException, ClassNotFoundException {

        File file = new File(pathFileWishlist);

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathFileWishlist))) {
                return (List<Book>) ois.readObject();
            }
        }
        return new ArrayList<>();
    }

    /**
     * @brief Loads the list of users from the specified file.
     * @param pathFileUsers The file path to the user data.
     * @return A list of users loaded from the file, or an empty list if the file
     *         does not exist.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public List<User> loadUsers(String pathFileUsers)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        List<User> users = new ArrayList<User>();

        // Checks if file path exists
        File file = new File(pathFileUsers);
        if (file.exists()) {
            // Read books from file
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathFileUsers))) {
                users = (List<User>) ois.readObject();
            }
        }

        return users;
    }

    /**
     * @brief Loads the loaned histories of borrowed books for the logged-in user.
     * @param pathFileHistories The file path to the loaned history data.
     * @return A list of loaned histories for borrowed books by the logged-in user,
     *         or an empty list if the file does not exist.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public List<LoanedHistory> loadLoanedHistoriesBorrowedBooks(String pathFileHistories)
            throws IOException, ClassNotFoundException {
        List<LoanedHistory> allHistories = new ArrayList<>();
        File file = new File(pathFileHistories);
        if (!file.exists()) {
            return new ArrayList<>(); // If file doesnt exist, return an empty list
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathFileHistories))) {
            // Read all loaned histories from the file
            allHistories = (List<LoanedHistory>) ois.readObject();
        }

        // Filter the loaned histories to only include the ones where the user is the
        // debtor
        List<LoanedHistory> filteredHistories = new ArrayList<>();
        for (LoanedHistory history : allHistories) {
            if (history.getDebtorUserId().equals(loggedUser.getId()) && history.getHasGivenBack() == false) {
                filteredHistories.add(history);
            }
        }

        return filteredHistories; // Return filtered list
    }

    /**
     * @brief Loads the loaned histories of given books for the logged-in user.
     * @param pathFileHistories The file path to the loaned history data.
     * @return A list of loaned histories for given books by the logged-in user, or
     *         an empty list if the file does not exist.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public List<LoanedHistory> loadLoanedHistoriesGivenBooks(String pathFileHistories)
            throws IOException, ClassNotFoundException {
        List<LoanedHistory> allHistories = new ArrayList<>();
        File file = new File(pathFileHistories);
        if (!file.exists()) {
            return new ArrayList<>(); // If file doesnt exist, return an empty list
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathFileHistories))) {
            // Read all loaned histories from the file
            allHistories = (List<LoanedHistory>) ois.readObject();
        }

        // Filter the loaned histories to only include the ones where the user is the
        // owner
        List<LoanedHistory> filteredHistories = new ArrayList<>();
        for (LoanedHistory history : allHistories) {
            if (history.getBookOwnerId().equals(loggedUser.getId()) && history.getHasGivenBack() == false) {
                filteredHistories.add(history);
            }
        }

        return filteredHistories; // Return filtered list
    }

    /**
     * @brief Loads all loaned histories from the specified file.
     * @param pathFileHistories The file path to the loaned history data.
     * @return A list of all loaned histories loaded from the file, or an empty list
     *         if the file does not exist.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public List<LoanedHistory> loadLoanedHistories(String pathFileHistories)
            throws IOException, ClassNotFoundException {
        File file = new File(pathFileHistories);

        if (!file.exists()) {
            return new ArrayList<>(); // If file doesnt exist, return an empty list
        }
        List<LoanedHistory> histories = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathFileHistories))) {
            // Read all loaned histories from the file
            histories = (List<LoanedHistory>) ois.readObject();
        }
        return histories; // Return list
    }

    /**
     * @brief Gets the ID for a new book.
     * @param pathFileBooks The file path to the book data.
     * @return The ID for a new book.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public int getNewBookId(String pathFileBooks) throws FileNotFoundException, IOException, ClassNotFoundException {
        List<Book> books = loadBooks(pathFileBooks);
        if (books.isEmpty())
            return 1;

        randomizedQuickSortBookIds(books, 0, books.size() - 1);

        return books.get(books.size() - 1).getId() + 1;
    }


}
