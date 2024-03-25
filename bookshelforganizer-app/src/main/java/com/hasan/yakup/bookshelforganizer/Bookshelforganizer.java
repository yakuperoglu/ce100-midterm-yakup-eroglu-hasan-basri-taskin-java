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
    
    /**
     * @brief Gets the ID for a new wishlist.
     * @param pathFileWishlist The file path to the wishlist data.
     * @return The ID for a new wishlist.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public int getNewWishlistId(String pathFileWishlist)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        List<Book> wishlist = loadWishlist(pathFileWishlist);
        if (wishlist.isEmpty())
            return 1;

        randomizedQuickSortBookIds(wishlist, 0, wishlist.size() - 1);

        return wishlist.get(wishlist.size() - 1).getId() + 1;
    }

    /**
     * @brief Sorts the book IDs using randomized quicksort algorithm.
     * @param books The list of books to be sorted.
     * @param low   The index of the lower bound.
     * @param high  The index of the upper bound.
     */
    public void randomizedQuickSortBookIds(List<Book> books, int low, int high) {
        if (low < high) {
            int pivotIndex = randomizedPartitionBookIds(books, low, high);
            randomizedQuickSortBookIds(books, low, pivotIndex); // Hoare Partitioning'de pivotIndex dahil edilir.
            randomizedQuickSortBookIds(books, pivotIndex + 1, high);
        }
    }

    /**
     * @brief Partitions the book IDs randomly.
     * @param books The list of books to be partitioned.
     * @param low   The index of the lower bound.
     * @param high  The index of the upper bound.
     * @return The index of the pivot element.
     */
    public int randomizedPartitionBookIds(List<Book> books, int low, int high) {
        int pivotIndex = low + (int) (Math.random() * (high - low + 1));
        Book pivot = books.get(pivotIndex);
        int i = low - 1;
        int j = high + 1;
        while (true) {
            do {
                i++;
            } while (books.get(i).getId() < pivot.getId());
            do {
                j--;
            } while (books.get(j).getId() > pivot.getId());
            if (i >= j) {
                return j;
            }
            swapBooks(books, i, j);
        }
    }

    /**
     * @brief Swaps two books in the list.
     * @param books The list of books.
     * @param i     The index of the first book.
     * @param j     The index of the second book.
     */
    public void swapBooks(List<Book> books, int i, int j) {
        Book temp = books.get(i);
        books.set(i, books.get(j));
        books.set(j, temp);
    }

    /**
     * @brief Gets the ID for a new user.
     * @param pathFileUsers The file path to the user data.
     * @return The ID for a new user.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public int getNewUserId(String pathFileUsers) throws FileNotFoundException, IOException, ClassNotFoundException {
        // Load the list of users from the given file path.
        List<User> users = loadUsers(pathFileUsers);

        // Convert user IDs into an array for sorting
        int[] userIds = new int[users.size()];
        for (int i = 0; i < users.size(); i++) {
            userIds[i] = users.get(i).getId();
        }

        // Use HeapSort to sort the array
        heapSort(userIds);

        // If the array is empty, return 1 as the new user ID
        if (userIds.length == 0) {
            return 1;
        }

        // Otherwise, return the highest user ID plus one
        return userIds[userIds.length - 1] + 1;
    }

    /**
     * @brief Sorts the array using the heap sort algorithm.
     * @param arr The array to be sorted.
     */
    // HeapSort implementation
    public void heapSort(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0);
        }
    }

    /**
     * @brief Heapifies the array.
     * @param arr The array to be heapified.
     * @param n   The size of the heap.
     * @param i   The index of the root node.
     * @return Always returns true.
     */
    public boolean heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest])
            largest = left;

        if (right < n && arr[right] > arr[largest])
            largest = right;

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            heapify(arr, n, largest);
        }
        return true;
    }
    
    /**
     * @brief Displays the main menu.
     * @return Always returns true to indicate successful execution.
     */
    public boolean printMainMenu() {
      out.println("Welcome To Virtual Bookshelf Organizer\n\n");
      out.println("1. Login");
      out.println("2. Register");
      out.println("3. Exit Program");
      out.print("Please enter a number to select: ");
      return true;
  }

  /**
   * @brief Displays the main menu and manages user input.
   * @param pathFileUsers            The file path for user data.
   * @param pathFileBooks            The file path for book data.
   * @param pathFileLendingHistories The file path for lending histories.
   * @param pathFileWishlist         The file path for wishlisted books.
   * @return 0 if the user chooses to exit the program.
   * @throws InterruptedException   If the thread is interrupted while waiting.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public int mainMenu(String pathFileUsers, String pathFileBooks, String pathFileLendingHistories,
          String pathFileWishlist)
          throws InterruptedException, IOException, ClassNotFoundException {
      int choice;

      while (true) {
          clearScreen();
          printMainMenu();
          choice = tryParseInt(scanner.nextLine());

          if (choice == -1) {
              handleInputError();
              enterToContinue();
              continue;
          }

          switch (choice) {
              case 1:
                  clearScreen();
                  if (loginUserMenu(pathFileUsers))
                      userOperations(pathFileBooks, pathFileLendingHistories, pathFileWishlist);
                  break;

              case 2:
                  clearScreen();
                  registerMenu(pathFileUsers);
                  break;

              case 3:
                  out.println("Exit Program");
                  return 0;

              default:
                  clearScreen();
                  out.println("Invalid choice. Please try again.");
                  enterToContinue();
                  break;
          }
      }
  }

  /**
   * @brief Displays the register menu and registers a new user.
   * @param pathFileUsers The file path for user data.
   * @return true if the user is successfully registered, false otherwise.
   * @throws InterruptedException   If the thread is interrupted while waiting.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public boolean registerMenu(String pathFileUsers) throws InterruptedException, IOException, ClassNotFoundException {
      clearScreen();
      User newUser = new User();

      out.print("Enter Name: ");
      newUser.setName(scanner.nextLine());

      out.print("Enter Surname: ");
      newUser.setSurname(scanner.nextLine());

      out.print("Enter email: ");
      newUser.setEmail(scanner.nextLine());

      out.print("Enter password: ");
      newUser.setPassword(scanner.nextLine());

      return registerUser(newUser, pathFileUsers);
  }

  /**
   * @brief Registers a new user.
   * @param user         The user object to register.
   * @param pathFileUser The file path for user data.
   * @return true if the user is successfully registered, false otherwise.
   * @throws FileNotFoundException  If the file cannot be found.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public boolean registerUser(User user, String pathFileUser)
          throws FileNotFoundException, IOException, ClassNotFoundException {
      if (user.getName().isEmpty() || user.getSurname().isEmpty() || user.getEmail().isEmpty()
              || user.getPassword().isEmpty()) {
          out.println("Please fill all the fields..");
          enterToContinue();
          return false;
      }
      File file = new File(pathFileUser);
      ArrayList<User> users = new ArrayList<>();

      // If file exist, first read all users and write "users"
      if (file.exists()) {
          try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathFileUser))) {
              users = (ArrayList<User>) ois.readObject();
          }
          // Check user already exist
          for (User u : users) {
              if (u.getEmail().equals(user.getEmail())) {
                  out.println("User already exist..");
                  enterToContinue();
                  return false;
              }
          }
      }
      user.setId(getNewUserId(pathFileUser));
      // Add new user to user list
      users.add(user);

      // Write updated user list to array
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
          oos.writeObject(users);
          out.println("User were registered: Welcome " + user.getName() + " " + user.getSurname());
      }
      enterToContinue();
      return true;
  }

  /**
   * @brief Displays the login menu and logs in the user.
   * @param pathFileUsers The file path for user data.
   * @return true if the user is successfully logged in, false otherwise.
   * @throws InterruptedException   If the thread is interrupted while waiting.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public boolean loginUserMenu(String pathFileUsers)
          throws InterruptedException, IOException, ClassNotFoundException {
      clearScreen();
      User loginUser = new User();

      out.print("Enter email: ");
      loginUser.setEmail(scanner.nextLine());

      out.print("Enter password: ");
      loginUser.setPassword(scanner.nextLine());

      return loginUser(loginUser, pathFileUsers);
  }
  
    /**
     * @brief Logs in the user.
     * @param data          The user data for login.
     * @param pathFileUsers The file path for user data.
     * @return true if the user is successfully logged in, false otherwise.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     * @throws FileNotFoundException  If the file cannot be found.
     * @throws IOException            If an I/O error occurs.
     */
    public boolean loginUser(User data, String pathFileUsers)
            throws ClassNotFoundException, FileNotFoundException, IOException {
        boolean isAuthenticated = false;

        // Checks if file path exists
        File file = new File(pathFileUsers);
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(pathFileUsers);
                    ObjectInputStream in = new ObjectInputStream(fileIn)) {
                ArrayList<User> users = (ArrayList<User>) in.readObject(); // Read as array
                for (User user : users) {
                    if (user.getEmail().equals(data.getEmail()) && user.getPassword().equals(data.getPassword())) {
                        setLoggedUser(user);
                        isAuthenticated = true;
                        out.println(("You logged succesfully.."));
                        return true;
                    }
                }
            }
        }
        if (!isAuthenticated)
            out.println("There are no users in this information..");
        enterToContinue();
        return isAuthenticated;
    }

    /**
     * @brief Displays the user menu.
     * @return true to indicate successful display of the user menu.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws IOException          If an I/O error occurs.
     */
    public boolean userMenu() throws InterruptedException, IOException {
        clearScreen();
        out.println("Welcome to User Operations\n\n");
        out.println("1. Book Cataloging");
        out.println("2. Loan Management");
        out.println("3. WishList Management");
        out.println("4. Return to Main Menu");
        out.print("Please enter a number to select: ");
        return true;
    }

    /**
     * @brief Handles user operations menu.
     * @param pathFileBooks     The file path for book data.
     * @param pathFileHistories The file path for loan histories.
     * @param pathFileWishlist  The file path for wish list data.
     * @return true if the user chooses to return to the main menu, false otherwise.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean userOperations(String pathFileBooks, String pathFileHistories, String pathFileWishlist)
            throws InterruptedException, IOException, ClassNotFoundException {
        int choice;

        enterToContinue();
        while (true) {
            clearScreen();
            userMenu();

            choice = tryParseInt(scanner.nextLine());

            if (choice == -1) {
                handleInputError();
                enterToContinue();
                continue;
            }

            switch (choice) {
                case 1:
                    bookCataloging(pathFileBooks);
                    break;

                case 2:
                    loanManagement(pathFileBooks, pathFileHistories);
                    break;

                case 3:
                    wishList(pathFileBooks, pathFileWishlist);
                    break;

                case 4:
                    return false;

                default:
                    clearScreen();
                    out.println("Invalid choice. Please try again.");
                    enterToContinue();
                    break;
            }
        }
    }

    /**
     * @brief Displays the book cataloging menu.
     * @return true to indicate successful display of the book cataloging menu.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws IOException          If an I/O error occurs.
     */
    public boolean bookCatalogingMenu() throws InterruptedException, IOException {
        clearScreen();
        out.println("Welcome to Book Operations\n\n");
        out.println("1. Add Book");
        out.println("2. Delete Book");
        out.println("3. Update Book");
        out.println("4. View Catalog");
        out.println("5. View Books by Price");
        out.println("6. View Total Price of All Books");
        out.println("7. Minimum cost of arranging books");
        out.println("8. Return to User Operations Menu");
        out.print("Please enter a number to select: ");

        return true;
    }

    /**
     * @brief Manages book cataloging operations.
     * @param pathFileBooks The file path for book data.
     * @return false to indicate the user has chosen to return to the user
     *         operations menu.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean bookCataloging(String pathFileBooks)
            throws InterruptedException, IOException, ClassNotFoundException {
        int choice;
        while (true) {
            bookCatalogingMenu();

            choice = tryParseInt(scanner.nextLine());

            if (choice == -1) {
                handleInputError();
                enterToContinue();
                continue;
            }

            switch (choice) {
                case 1:
                    addBookMenu(pathFileBooks);
                    break;

                case 2:
                    deleteBookMenu(pathFileBooks);
                    break;

                case 3:
                    updateBookMenu(pathFileBooks);
                    break;

                case 4:
                    viewCatalog(pathFileBooks);
                    break;

                case 5:
                    selectBooksByPriceMenu(pathFileBooks);
                    break;
                case 6:
                    calculateTotalPrice(pathFileBooks);
                    break;
                case 7:
                    minCostArrangingBooks(pathFileBooks);
                    break;
                case 8:
                    return false;

                default:
                    clearScreen();
                    out.println("Invalid choice. Please try again.");
                    enterToContinue();
                    break;
            }
        }
    }
    
    /**
     * @brief Calculates the minimum cost of arranging books.
     * @param pathToBooksFile The file path for book data.
     * @return true to indicate successful calculation of the minimum cost of
     *         arranging books.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     * @throws IOException            If an I/O error occurs.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     */
    public boolean minCostArrangingBooks(String pathToBooksFile)
            throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException {
        clearScreen();
        ArrayList<Book> ownedBooks = (ArrayList<Book>) loadOwnedBooks(pathToBooksFile);
        Book[] books = ownedBooks.toArray(new Book[ownedBooks.size()]);

        int numBooks = books.length;
        int[][] costMatrix = new int[numBooks][numBooks];

        // Fill cost matrix with maximum values
        for (int[] row : costMatrix)
            Arrays.fill(row, Integer.MAX_VALUE);

        // Fill diagonal with 0 as single book multiplication cost is 0
        for (int i = 0; i < numBooks; i++)
            costMatrix[i][i] = 0;

        // Perform Matrix Chain Multiplication Order Dynamic Programming
        for (int chainLen = 2; chainLen < numBooks; chainLen++) {
            for (int i = 1; i < numBooks - chainLen + 1; i++) {
                int j = i + chainLen - 1;
                for (int k = i; k <= j - 1; k++) {
                    int cost = costMatrix[i][k] + costMatrix[k + 1][j]
                            + books[i - 1].getPrice() * books[k].getPrice() * books[j].getPrice();
                    if (cost < costMatrix[i][j])
                        costMatrix[i][j] = cost;
                }
            }
        }

        // Reconstruct optimal ordering
        // This part depends on how you want to handle the ordering information

        // For simplicity, let's just print the minimum cost
        out.println("Minimum cost of arranging books: " + costMatrix[1][numBooks - 1]);
        enterToContinue();
        return true;
    }

    /**
     * @brief Calculates the total price of all books.
     * @param pathFileBooks The file path for book data.
     * @return true to indicate successful calculation of the total price.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     * @throws IOException            If an I/O error occurs.
     * @throws InterruptedException   If the thread is interrupted while waiting.
     */
    public boolean calculateTotalPrice(String pathFileBooks)
            throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException {
        clearScreen();
        int[][] bookPrices = loadOwnedBookPrices(pathFileBooks);
        // Check if the matrix is valid for multiplication
        if (!isValidMatrix(bookPrices)) {
            out.println("Total price of all books: 0TL");
            enterToContinue();
            return false;
        }

        // Get the number of columns of the matrix
        int cols = bookPrices[0].length;

        // Create a sequence of matrices where each matrix is a single book price
        int[][][] bookMatrixSequence = new int[cols][1][1];
        for (int i = 0; i < cols; i++) {
            bookMatrixSequence[i] = new int[][] { { bookPrices[0][i] } };
        }

        // Create an optimal split table (s) for the bookMatrixSequence
        int[][] s = createOptimalSplitTable(bookMatrixSequence.length);

        // Perform matrix chain multiplication to calculate the total price
        int[][] totalPriceMatrix = matrixChainMultiply(bookMatrixSequence, s, 1,
                bookMatrixSequence.length);

        // The total price is located at the top left corner of the totalPriceMatrix
        out.println("Total price of all books: " + totalPriceMatrix[0][0] + " TL");
        enterToContinue();
        return true;
    }

    /**
     * @brief Checks if the given matrix is valid for multiplication.
     * @param matrix The matrix to be checked.
     * @return true if the matrix is valid for multiplication, false otherwise.
     */
    public boolean isValidMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        int cols = matrix[0].length;
        for (int[] row : matrix) {
            if (row.length != cols) {
                return false;
            }
        }
        return true;
    }

    /**
     * @brief Creates the optimal split table for matrix chain multiplication.
     * @param numMatrices The number of matrices in the chain.
     * @return The optimal split table.
     */
    public int[][] createOptimalSplitTable(int numMatrices) {
        // Dummy implementation: Assuming a linear split for demonstration
        int[][] s = new int[numMatrices - 1][numMatrices];
        for (int i = 0; i < numMatrices - 1; i++) {
            for (int j = 0; j < numMatrices; j++) {
                s[i][j] = Math.min(i + 1, numMatrices - 1);
            }
        }
        return s;
    }

    /**
     * @brief Performs matrix chain multiplication.
     * @param A The array of matrices to be multiplied.
     * @param s The optimal split table.
     * @param i The starting index of the chain.
     * @param j The ending index of the chain.
     * @return The result of the matrix chain multiplication.
     */
    public int[][] matrixChainMultiply(int[][][] A, int[][] s, int i, int j) {
        if (j > i) {
            int split = s[i - 1][j - 2]; // Retrieve the split point from the s table
            int[][] X = matrixChainMultiply(A, s, i, split); // Multiply the left subchain
            int[][] Y = matrixChainMultiply(A, s, split + 1, j); // Multiply the right subchain
            return matrixMultiply(X, Y); // Combine the results of the subchains
        } else {
            // Base case: when the chain has only one matrix, return it directly
            return A[i - 1]; // Adjust for 0-based indexing
        }
    }

    /**
     * @brief Performs matrix multiplication.
     * @param A The first matrix.
     * @param B The second matrix.
     * @return The result of the matrix multiplication.
     */
    public int[][] matrixMultiply(int[][] A, int[][] B) {
        int rowsA = A.length, colsA = A[0].length; // Dimensions of matrix A
        int rowsB = B.length, colsB = B[0].length; // Dimensions of matrix B
        // Check if matrices are compatible for multiplication
        if (colsA != rowsB) {
            return new int[0][0]; // Return an empty matrix if not compatible
        }
        int[][] result = new int[rowsA][colsB]; // The resulting matrix of the multiplication
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += A[i][k] * B[k][j]; // Calculate each element of the result matrix
                }
            }
        }
        return result;
    }
    
    /**
     * Displays the menu for adding a new book, prompts the user for book details,
     * and adds the book to the library system.
     * 
     * @param pathFileBooks The file path of the book data.
     * @return True if the book was added successfully, false otherwise.
     * @throws InterruptedException   If the operation is interrupted.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean addBookMenu(String pathFileBooks) throws InterruptedException, IOException, ClassNotFoundException {
      clearScreen();
      Book newBook = new Book();

      out.print("Enter book name: ");
      newBook.setTitle(scanner.nextLine());

      out.print("Enter author: ");
      newBook.setAuthor(scanner.nextLine());

      out.print("Enter Genre: ");
      newBook.setGenre(scanner.nextLine());

      out.print("Enter Cost: ");
      int cost = tryParseInt(scanner.nextLine());
      if (cost == -1) {
          handleInputError();
          enterToContinue();
          return false;
      }
      newBook.setPrice(cost);

      boolean result = addBook(newBook, pathFileBooks);
      if (result)
          out.println("Book added successfully.");
      enterToContinue();
      return result;
  }

  /**
   * Adds a new book to the library system.
   * 
   * @param newBook       The book to be added.
   * @param pathFileBooks The file path of the book data.
   * @return True if the book was added successfully, false otherwise.
   * @throws FileNotFoundException  If the specified file is not found.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public boolean addBook(Book newBook, String pathFileBooks)
          throws FileNotFoundException, IOException, ClassNotFoundException {
      List<Book> books = loadBooks(pathFileBooks);

      newBook.setId(getNewBookId(pathFileBooks));
      newBook.setOwner(loggedUser);
      newBook.setIsBorrowed(false);

      books.add(newBook);

      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathFileBooks))) {
          oos.writeObject(books);
      }

      return true;
  }

  /**
   * Displays the menu for deleting a book, prompts the user to select a book
   * for deletion, and deletes the selected book from the library system.
   * 
   * @param pathFileBooks The file path of the book data.
   * @return True if the book was deleted successfully, false otherwise.
   * @throws FileNotFoundException  If the specified file is not found.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   * @throws InterruptedException   If the operation is interrupted.
   */
  public boolean deleteBookMenu(String pathFileBooks)
          throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException {
      clearScreen();
      boolean isUserHasBooks = writeBooksToConsole(pathFileBooks);
      if (!isUserHasBooks) {
          enterToContinue();
          return false;
      }
      out.print("Enter a number to delete book: ");

      int bookId = tryParseInt(scanner.nextLine());

      if (bookId == -1) {
          handleInputError();
          enterToContinue();
          return false;
      }

      return deleteBook(bookId, pathFileBooks);
  }

  /**
   * Deletes a book from the library system based on the provided book ID.
   * 
   * @param bookId        The ID of the book to be deleted.
   * @param pathFileBooks The file path of the book data.
   * @return True if the book was deleted successfully, false otherwise.
   * @throws FileNotFoundException  If the specified file is not found.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public boolean deleteBook(int bookId, String pathFileBooks)
          throws FileNotFoundException, IOException, ClassNotFoundException {
      List<Book> books = loadBooks(pathFileBooks);
      boolean isFound = false;

      for (Book book : books) {
          if (book.getId() == bookId) {
              isFound = true;
              books.remove(book);
              break;
          }
      }

      if (isFound) {
          try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathFileBooks))) {
              oos.writeObject(books);
          }
          out.println("Book with ID '" + bookId + "' has been deleted successfully.");
          enterToContinue();
          return true;
      }

      out.println("There is no book with ID '" + bookId + "'.");
      enterToContinue();
      return false;
  }

  /**
   * Displays the menu for updating a book, prompts the user to select a book
   * for updating, and allows the user to modify the book's details.
   * 
   * @param pathFileBooks The file path of the book data.
   * @return True if the book was updated successfully, false otherwise.
   * @throws IOException            If an I/O error occurs.
   * @throws InterruptedException   If the operation is interrupted.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public boolean updateBookMenu(String pathFileBooks)
          throws IOException, InterruptedException, ClassNotFoundException {
      clearScreen();
      boolean isUserHasBooks = writeBooksToConsole(pathFileBooks);
      if (!isUserHasBooks) {
          enterToContinue();
          return false;
      }
      out.print("Enter a number to update book: ");

      int bookId = tryParseInt(scanner.nextLine());

      if (bookId == -1) {
          handleInputError();
          enterToContinue();
          return false;
      }

      Book newBook = new Book();

      newBook.setId(bookId);

      out.print("Enter the new name for the book: ");
      newBook.setTitle(scanner.nextLine());

      out.print("Enter author name: ");
      newBook.setAuthor(scanner.nextLine());

      out.print("Enter genre: ");
      newBook.setGenre(scanner.nextLine());

      out.print("Enter Cost: ");
      int cost = tryParseInt(scanner.nextLine());
      if (cost == -1) {
          handleInputError();
          enterToContinue();
          return false;
      }
      newBook.setPrice(cost);

      updateBook(newBook, pathFileBooks);
      return true;
  }

  /**
   * Updates the details of a book in the library system.
   * 
   * @param newBook       The updated book object.
   * @param pathFileBooks The file path of the book data.
   * @return True if the book was updated successfully, false otherwise.
   * @throws FileNotFoundException  If the specified file is not found.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   */
  public boolean updateBook(Book newBook, String pathFileBooks)
          throws FileNotFoundException, IOException, ClassNotFoundException {
      List<Book> books = loadBooks(pathFileBooks);
      boolean isFound = false;

      for (Book book : books) {
          if (book.getId() == newBook.getId()) {
              book.setTitle(newBook.getTitle());
              book.setAuthor(newBook.getAuthor());
              book.setGenre(newBook.getGenre());
              book.setPrice(newBook.getPrice());
              isFound = true;
              break;
          }
      }

      if (isFound) {
          // Save book list to binary file
          try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathFileBooks))) {
              oos.writeObject(books);
          }
          out.println("Book with ID '" + newBook.getId() + "' has been updated successfully.");
      }

      if (!isFound)
          out.println("There is no book with the specified ID.");

      enterToContinue();
      return isFound;
  }
  
    /**
     * Displays the catalog of books stored in the system.
     * 
     * @param filePathBooks The file path of the book data.
     * @return True if the catalog was displayed successfully, false otherwise.
     * @throws InterruptedException   If the operation is interrupted.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean viewCatalog(String filePathBooks) throws InterruptedException, IOException, ClassNotFoundException {
      clearScreen();
      writeBooksToConsole(filePathBooks);
      enterToContinue();
      return true;
  }

  /**
   * Displays the menu for selecting books based on a specified budget,
   * prompts the user for their budget, and lists the books within the budget.
   * 
   * @param pathFileBooks The file path of the book data.
   * @return True if the book selection was successful, false otherwise.
   * @throws FileNotFoundException  If the specified file is not found.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   * @throws IOException            If an I/O error occurs.
   * @throws InterruptedException   If the operation is interrupted.
   */
  public boolean selectBooksByPriceMenu(String pathFileBooks)
          throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException {
      clearScreen();
      List<Book> books = loadOwnedBooks(pathFileBooks);

      out.print("Enter your budget: ");
      int budget = tryParseInt(scanner.nextLine());

      if (budget == -1) {
          handleInputError();
          enterToContinue();
          return false;
      }
      List<Book> selectedBooks = selectBooksByPrice(books, budget);

      out.println("Selected books within your budget:");
      for (Book book : selectedBooks) {
          out.println(book.getTitle() + " by " + book.getAuthor() + " - " + book.getPrice() + "TL");
      }
      enterToContinue();
      return true;
  }

  /**
   * Selects books based on a specified budget using dynamic programming.
   * 
   * @param books  The list of available books.
   * @param budget The budget for book selection.
   * @return A list of selected books within the given budget.
   */
  public List<Book> selectBooksByPrice(List<Book> books, int budget) {
      int totalBooks = books.size();

      // This DP (Dynamic Programming) table stores the maximum total price
      // considering different numbers of
      // books and budgets.
      // It helps in efficiently calculating the optimal selection of books within the
      // given budget.

      int[][] dpTable = new int[totalBooks + 1][budget + 1];

      // Fill in the DP table
      for (int bookIndex = 0; bookIndex <= totalBooks; bookIndex++) {
          for (int currentBudget = 0; currentBudget <= budget; currentBudget++) {
              // Base case: If there are no books or the budget is zero,
              // the total price we can afford is zero.
              if (bookIndex == 0 || currentBudget == 0)
                  dpTable[bookIndex][currentBudget] = 0;
              else {
                  Book currentBook = books.get(bookIndex - 1);
                  // If the current book's price is less than or equal to the current budget,
                  // we consider including it in the selection.
                  if (currentBook.getPrice() <= currentBudget) {
                      // Decide whether to include the current book or not,
                      // maximizing the total price within the budget.
                      dpTable[bookIndex][currentBudget] = Math.max(
                              currentBook.getPrice() + dpTable[bookIndex - 1][currentBudget - currentBook.getPrice()],
                              dpTable[bookIndex - 1][currentBudget]);
                  } else {
                      // If the current book's price exceeds the current budget,
                      // we cannot include it in the selection, so we keep the previous best price.
                      dpTable[bookIndex][currentBudget] = dpTable[bookIndex - 1][currentBudget];
                  }
              }
          }
      }

      // Trace back to find the selected books
      List<Book> selectedBooks = new ArrayList<>();
      int remainingBudget = budget;
      int totalPrice = dpTable[totalBooks][budget];

      for (int bookIndex = totalBooks; bookIndex > 0 && totalPrice > 0; bookIndex--) {
          if (totalPrice != dpTable[bookIndex - 1][remainingBudget]) {
              // If the current book was selected, add it to the list of selected books.
              selectedBooks.add(books.get(bookIndex - 1));
              // Update the remaining budget and total price
              totalPrice -= books.get(bookIndex - 1).getPrice();
              remainingBudget -= books.get(bookIndex - 1).getPrice();
          }
      }

      return selectedBooks;
  }

  /**
   * Displays the menu for loan management and prompts the user to select
   * an action related to loaning or borrowing books.
   * 
   * @return True if the loan management menu is displayed successfully, false
   *         otherwise.
   * @throws InterruptedException If the operation is interrupted.
   * @throws IOException          If an I/O error occurs.
   */
  public boolean loanManagementMenu() throws InterruptedException, IOException {
      clearScreen();
      out.println("Loan Management Menu\n\n");
      out.println("1. Give Book");
      out.println("2. Borrow Book");
      out.println("3. Show borrowed books");
      out.println("4. Show given books");
      out.println("5. Show suggestions for books to borrow");
      out.println("6. Return to User Operations Menu");
      out.print("Please enter a number to select: ");
      return true;
  }
  /**
     * Manages the loaning and borrowing of books by providing a menu
     * and handling user input to perform corresponding actions.
     * 
     * @param pathFileBooks     The file path of the book data.
     * @param pathFileHistories The file path of loan history data.
     * @return True if the loan management operation is successful, false otherwise.
     * @throws InterruptedException   If the operation is interrupted.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean loanManagement(String pathFileBooks, String pathFileHistories)
            throws InterruptedException, IOException, ClassNotFoundException {
        int choice;

        while (true) {
            loanManagementMenu();
            choice = tryParseInt(scanner.nextLine());

            if (choice == -1) {
                handleInputError();
                enterToContinue();
                continue;
            }

            switch (choice) {
                case 1:
                    giveBackBookMenu(pathFileBooks, pathFileHistories);
                    break;

                case 2:
                    borrowBookMenu(pathFileBooks, pathFileHistories);
                    break;

                case 3:
                    viewBorrowedBooks(pathFileHistories);
                    break;
                case 4:
                    viewGivenBooks(pathFileHistories);
                    break;
                case 5:
                    suggestBooksToBorrow(pathFileBooks);
                    break;

                case 6:
                    return false;

                default:
                    out.println("Invalid choice. Please try again.");
                    enterToContinue();
                    break;
            }
        }
    }

    /**
     * Displays the menu for giving back a borrowed book and handles user input to
     * facilitate the book return process.
     *
     * @param pathFileBooks     The file path of the book data.
     * @param pathFileHistories The file path of loan history data.
     * @return True if the give-back book menu is displayed successfully, false
     *         otherwise.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws InterruptedException   If the operation is interrupted.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean giveBackBookMenu(String pathFileBooks, String pathFileHistories)
            throws FileNotFoundException, IOException, InterruptedException, ClassNotFoundException {
        clearScreen();
        boolean result = writeBorrowedBooksToConsole(pathFileHistories);
        if (!result) {
            enterToContinue();
            return false;
        }

        out.print("Enter the ID of the book you want to give back: ");
        int bookId = tryParseInt(scanner.nextLine());

        if (bookId == -1) {
            handleInputError();
            enterToContinue();
            return false;
        }

        return giveBackBook(bookId, pathFileBooks, pathFileHistories);
    }

    /**
     * Facilitates the process of giving back a borrowed book by updating the book's
     * status in the book data file and loan history file.
     *
     * @param bookId            The ID of the book to be given back.
     * @param pathFileBooks     The file path of the book data.
     * @param pathFileHistories The file path of loan history data.
     * @return True if the book is successfully given back, false otherwise.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean giveBackBook(Integer bookId, String pathFileBooks, String pathFileHistories)

            throws FileNotFoundException, IOException, ClassNotFoundException {
        List<Book> books = loadBooks(pathFileBooks);

        boolean isFound = false;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathFileBooks))) {
            for (Book book : books) {
                if (book.getId() == bookId && !book.getOwner().getId().equals(loggedUser.getId())
                        && book.isBorrowed()) {
                    book.setIsBorrowed(false);
                    isFound = true;
                    break;
                }
            }
            oos.writeObject(books);
        }

        if (!isFound) {
            out.println("There is no book with the specified ID.");
            enterToContinue();
            return false;
        }

        List<LoanedHistory> histories = loadLoanedHistories(pathFileHistories);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathFileHistories))) {
            for (LoanedHistory history : histories) {
                if (history.getBookId().equals(bookId)) {
                    history.setHasGivenBack(true);
                }
            }
            oos.writeObject(histories);
        }

        out.println("The book was returned successfully.");
        enterToContinue();
        return true;
    }

    /**
     * Displays the menu for borrowing a book and handles user input to facilitate
     * the book borrowing process.
     *
     * @param pathFileBooks     The file path of the book data.
     * @param pathFileHistories The file path of loan history data.
     * @return True if the borrow book menu is displayed successfully, false
     *         otherwise.
     * @throws InterruptedException   If the operation is interrupted.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean borrowBookMenu(String pathFileBooks, String pathFileHistories)
            throws InterruptedException, IOException, ClassNotFoundException {
        clearScreen();
        boolean result = writeBooksExcludingUser(pathFileBooks, pathFileHistories);
        if (!result) {
            enterToContinue();
            return false;
        }

        out.print("Enter the ID of the book you want to borrow: ");
        int bookId = tryParseInt(scanner.nextLine());

        if (bookId == -1) {
            handleInputError();
            enterToContinue();
            return false;
        }

        return borrowBook(bookId, pathFileBooks, pathFileHistories);
    }
    /**
     * Facilitates the process of borrowing a book by updating the book's status in
     * the book data file and adding a loan history entry.
     *
     * @param bookId            The ID of the book to be borrowed.
     * @param pathFileBooks     The file path of the book data.
     * @param pathFileHistories The file path of loan history data.
     * @return True if the book is successfully borrowed, false otherwise.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean borrowBook(Integer bookId, String pathFileBooks, String pathFileHistories)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        List<Book> books = loadBooks(pathFileBooks);
        Book relatedBook = new Book();

        boolean isFound = false;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathFileBooks))) {
            for (Book book : books) {
                if (!book.isBorrowed() && book.getId() == bookId
                        && !book.getOwner().getId().equals(loggedUser.getId())) {
                    book.setIsBorrowed(true);
                    isFound = true;
                    relatedBook = book;
                    break;
                }
            }
            oos.writeObject(books);
        }

        if (!isFound) {
            out.println("The book is not available for borrowing or there is no book in this information.");
            enterToContinue();
            return false;
        }
        List<LoanedHistory> histories = loadLoanedHistories(pathFileHistories);

        LoanedHistory newHistory = new LoanedHistory();
        newHistory.setBookId(relatedBook.getId());
        newHistory.setBookOwner(relatedBook.getOwner().getName(), relatedBook.getOwner().getSurname());
        newHistory.setBookName(relatedBook.getTitle());
        newHistory.setDebtorUser(loggedUser.getName(), loggedUser.getSurname());
        newHistory.setBookOwnerId(relatedBook.getOwner().getId());
        newHistory.setDebtorUserId(loggedUser.getId());
        newHistory.setIsApproved(false);
        newHistory.setHasGivenBack(false);
        histories.add(newHistory);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathFileHistories))) {
            oos.writeObject(histories);
        }

        out.println("Book borrowed successfully.");
        enterToContinue();
        return true;
    }

    /**
     * Displays the borrowed books to the console.
     *
     * @param pathFileHistories The file path of loan history data.
     * @return True if the borrowed books are displayed successfully, false
     *         otherwise.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     * @throws InterruptedException   If the operation is interrupted.
     */
    public boolean viewBorrowedBooks(String pathFileHistories)
            throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException {
        clearScreen();
        writeBorrowedBooksToConsole(pathFileHistories);
        enterToContinue();
        return true;
    }

    /**
     * Displays the books given by the user to the console.
     *
     * @param pathFileHistories The file path of loan history data.
     * @return True if the given books are displayed successfully, false otherwise.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     * @throws InterruptedException   If the operation is interrupted.
     */
    public boolean viewGivenBooks(String pathFileHistories)
            throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException {
        clearScreen();
        writeGivenBooksToConsole(pathFileHistories);
        enterToContinue();
        return true;
    }

    /**
     * Displays the wishlist management menu and handles user input to perform
     * wishlist management operations.
     *
     * @return True if the wishlist management menu is displayed successfully, false
     *         otherwise.
     * @throws InterruptedException If the operation is interrupted.
     * @throws IOException          If an I/O error occurs.
     */
    public boolean wishListMenu()
            throws InterruptedException, IOException {
        clearScreen();
        out.println("WishList Management Menu\n\n");
        out.println("1. Add Book to Wishlist");
        out.println("2. Delete Book from Wishlist");
        out.println("3. Mark as Acquired");
        out.println("4. View Wishlist");
        out.println("5. Search Wishlist");
        out.println("6. Total Cost of Books in Wishlist");
        out.println("7. Return to User Operations Menu");
        out.print("Please enter a number to select: ");
        return true;
    }
    
    /**
     * Manages the wishlist operations such as adding, deleting, marking as
     * acquired, viewing, searching, and calculating the total cost.
     *
     * @param pathFileBooks    The file path of the book data.
     * @param pathFileWishlist The file path of the wishlist data.
     * @return False to return to the user operations menu.
     * @throws InterruptedException   If the operation is interrupted.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean wishList(String pathFileBooks, String pathFileWishlist)
            throws InterruptedException, IOException, ClassNotFoundException {
        int choice;

        while (true) {
            clearScreen();
            wishListMenu();

            choice = tryParseInt(scanner.nextLine());

            if (choice == -1) {
                handleInputError();
                enterToContinue();
                continue;
            }

            switch (choice) {
                case 1:
                    addBookToWishListMenu(pathFileWishlist);
                    break;

                case 2:
                    deleteBookFromWishListMenu(pathFileWishlist);
                    break;

                case 3:
                    markAsAcquiredMenu(pathFileBooks, pathFileWishlist);
                    break;
                case 4:
                    viewWishList(pathFileWishlist);
                    break;
                case 5:
                    searchWishlistMenu(pathFileWishlist);
                    break;
                case 6:
                    calculateTotalCost(pathFileWishlist);
                    break;

                case 7:
                    return false;

                default:
                    out.println("Invalid choice. Please try again.");
                    enterToContinue();
                    break;
            }
        }
    }

    /**
     * Calculates the minimum total cost for the books in the wishlist using the
     * Matrix Chain Multiplication Order algorithm.
     *
     * @param pathFileWishlist The file path of the wishlist data.
     * @return True if the total cost is calculated successfully, false otherwise.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     * @throws IOException            If an I/O error occurs.
     */
    public int matrixChainOrder(int p[], int i, int j) {
        if (i == j)
            return 0;

        int min = Integer.MAX_VALUE;

        // Matris zincirinin i ve j arasındaki her parçaya ayrılması ve minimum
        // maliyetin hesaplanması
        for (int k = i; k < j; k++) {
            int count = matrixChainOrder(p, i, k) + matrixChainOrder(p, k + 1, j) + p[i - 1] * p[k] * p[j];

            if (count < min)
                min = count;
        }

        // Minimum maliyetin döndürülmesi
        return min;
    }

    /**
     * Displays the menu for adding a book to the wishlist and handles user input to
     * perform the operation.
     *
     * @param pathFileWishlist The file path of the wishlist data.
     * @return True if the book is successfully added to the wishlist, false
     *         otherwise.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     * @throws IOException            If an I/O error occurs.
     * @throws InterruptedException   If the operation is interrupted.
     */
    public boolean calculateTotalCost(String pathFileWishlist)
            throws FileNotFoundException, ClassNotFoundException, IOException {
        List<Book> books = loadWishlist(pathFileWishlist, loggedUser.getId());
        int n = books.size();

        // Kitapların maliyetlerini içeren dizi
        int[] costs = new int[n + 1];
        for (int i = 0; i < n; i++) {
            costs[i] = books.get(i).getPrice();
        }

        // Matrix Zinciri Çarpım Sırası Hesaplama algoritmasını kullanarak toplam
        // maliyetin hesaplanması
        out.println("Total cost: " + matrixChainOrder(costs, 1, n - 1));
        enterToContinue();
        return true;
    }

    /**
     * Adds a book to the wishlist.
     *
     * @param newBook          The book to be added to the wishlist.
     * @param pathFileWishlist The file path of the wishlist data.
     * @return True if the book is successfully added to the wishlist, false
     *         otherwise.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     * @throws IOException            If an I/O error occurs.
     */
    public boolean addBookToWishListMenu(String pathFileWishlist)
            throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException {
        clearScreen();
        Book newBook = new Book();

        out.print("Enter book name: ");
        newBook.setTitle(scanner.nextLine());

        out.print("Enter author: ");
        newBook.setAuthor(scanner.nextLine());

        out.print("Enter Genre: ");
        newBook.setGenre(scanner.nextLine());

        out.print("Enter Cost: ");
        int cost = tryParseInt(scanner.nextLine());
        if (cost == -1) {
            handleInputError();
            enterToContinue();
            return false;
        }
        newBook.setPrice(cost);

        return addBookToWishList(newBook, pathFileWishlist);
    }

    /**
     * Adds a book to the wishlist.
     *
     * @param newBook          The book to be added to the wishlist.
     * @param pathFileWishlist The file path of the wishlist data.
     * @return True if the book is successfully added to the wishlist, false
     *         otherwise.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     * @throws IOException            If an I/O error occurs.
     */
    public boolean addBookToWishList(Book newBook, String pathFileWishlist)
            throws FileNotFoundException, ClassNotFoundException, IOException {
        List<Book> wishlists = loadWishlist(pathFileWishlist);

        newBook.setId(getNewWishlistId(pathFileWishlist));
        newBook.setOwner(loggedUser);
        newBook.setIsBorrowed(false);

        wishlists.add(newBook);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathFileWishlist))) {
            oos.writeObject(wishlists);
        }

        return true;
    }
    
    /**
     * Displays the menu for deleting a book from the wishlist and handles user
     * input to perform the operation.
     *
     * @param pathFileWishlist The file path of the wishlist data.
     * @return True if the book is successfully deleted from the wishlist, false
     *         otherwise.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     * @throws IOException            If an I/O error occurs.
     * @throws InterruptedException   If the operation is interrupted.
     */
    public boolean deleteBookFromWishListMenu(String pathFileWishlist)
            throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException {
        clearScreen();
        boolean isUserHasBooks = writeWishlistToConsole(pathFileWishlist);
        if (!isUserHasBooks) {
            enterToContinue();
            return false;
        }
        out.print("Enter a number to delete book from wishlist: ");

        int bookId = tryParseInt(scanner.nextLine());

        if (bookId == -1) {
            handleInputError();
            enterToContinue();
            return false;
        }

        return deleteBookFromWishlist(bookId, pathFileWishlist);
    }

    /**
     * Deletes a book from the wishlist.
     *
     * @param bookId           The ID of the book to be deleted from the wishlist.
     * @param pathFileWishlist The file path of the wishlist data.
     * @return True if the book is successfully deleted from the wishlist, false
     *         otherwise.
     * @throws FileNotFoundException  If the specified file is not found.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean deleteBookFromWishlist(int bookId, String pathFileWishlist)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        List<Book> books = loadWishlist(pathFileWishlist);
        boolean isFound = false;

        for (Book book : books) {
            if (book.getId() == bookId) {
                isFound = true;
                books.remove(book);
                break;
            }
        }

        if (isFound) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathFileWishlist))) {
                oos.writeObject(books);
            }
            out.println("Book with ID '" + bookId + "' has been deleted successfully from wishlist.");
            enterToContinue();
            return true;
        }

        out.println("There is no book with ID '" + bookId + "'.");
        enterToContinue();
        return false;
    }

    /**
     * Displays the menu for marking a book as acquired and handles user input to
     * perform the operation.
     *
     * @param pathFileBooks    The file path of the book data.
     * @param pathFileWishlist The file path of the wishlist data.
     * @return True if the book is successfully marked as acquired, false otherwise.
     * @throws InterruptedException   If the operation is interrupted.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be
     *                                found.
     */
    public boolean markAsAcquiredMenu(String pathFileBooks, String pathFileWishlist)
            throws InterruptedException, IOException, ClassNotFoundException {
        clearScreen();
        boolean result = writeWishlistToConsole(pathFileWishlist);
        if (!result) {
            enterToContinue();
            return false;
        }
        out.print("Enter the ID of the book you want to mark as acquired: ");
        int bookId = tryParseInt(scanner.nextLine());

        if (bookId == -1) {
            handleInputError();
            enterToContinue();
            return false;
        }

        return markAsAcquired(bookId, pathFileBooks, pathFileWishlist);
    }

}
