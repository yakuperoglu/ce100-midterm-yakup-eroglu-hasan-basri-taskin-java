/**

@file BookshelforganizerTest.java
@brief This file contains the test cases for the Bookshelforganizer class.
@details This file includes test methods to validate the functionality of the Bookshelforganizer class. It uses JUnit for unit testing.
*/
package com.hasan.yakup.bookshelforganizer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BookshelforganizerTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private String testFilePathBooks = "test_books.bin";
  private String testFilePathUsers = "test_users.bin";
  private String testFilePathWishlist = "test_wishlists.bin";
  private String testFilePathHistories = "test_lendinghistories.bin";
  // Define Bookshelforganizer object as null at first
  private Bookshelforganizer library = null;

  /**
   * @brief This method is executed once before all test methods.
   * @throws Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * @brief This method is executed once after all test methods.
   * @throws Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * @brief This method is executed before each test method.
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    System.setOut(new PrintStream(outContent));
    library = new Bookshelforganizer(new Scanner(""), new PrintStream(outContent));
    library.isTestMode = true;
    setLoggedUser();
  }

  /**
   * @brief This method is executed after each test method.
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
    cleanupTestDataBook();
    cleanupTestDataUser();
    cleanupTestDataWishlist();
    deleteFile(testFilePathHistories);
    System.setOut(null);
    System.setIn(null);
  }

  // /**
  // * @brief Test method to validate the addition operation.
  // *
  // * @details This method creates an instance of the Bookshelforganizer class
  // and calls the `add` method with two integers. It asserts the expected result
  // of the addition operation.
  // */
  // @Test
  // public void testAddition() {
  // Bookshelforganizer bookshelforganizer = new Bookshelforganizer();
  // int result = bookshelforganizer.add(2, 3);
  // assertEquals(5, result);
  // }

  @Test
  public void testRegisterMenu() throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "John\nDoe\njohn@example.com\npassword123\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    boolean res = library.registerMenu(testFilePathUsers);
    assertTrue(res);
  }

  @Test
  public void TestRegisterUser_ShouldRegisterUsers() throws FileNotFoundException, ClassNotFoundException, IOException {
    User testUser = new User(1, "Hasan", "Taşkın", "test@gmail.com", "Qwe123!");
    User testUser2 = new User(2, "Hasan1", "Taşkın", "test1@gmail.com", "Qwe123!");

    Boolean result = library.registerUser(testUser, testFilePathUsers);
    Boolean result2 = library.registerUser(testUser2, testFilePathUsers);

    assertTrue(result);
    assertTrue(result2);
  }

  @Test
  public void TestRegisterUser_ShouldntRegisterUsers()
      throws FileNotFoundException, ClassNotFoundException, IOException {
    User testUser = new User(1, "", "", "", "");

    Boolean result = library.registerUser(testUser, testFilePathUsers);

    assertFalse(result);
  }

  @Test
  public void TestRegisterUser_ShouldntRegisterExistingUser()
      throws FileNotFoundException, ClassNotFoundException, IOException {
    createTestFileUsers();
    User testUser = new User(1, "Hasan", "Taşkın", "hasan@gmail.com", "Qwe123!");

    Boolean result = library.registerUser(testUser, testFilePathUsers);

    assertFalse(result);
  }

  @Test
  public void testLoginUserMenu() throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams
    createTestFileUsers();

    String inputString = "hasan@gmail.com\nQwe123!\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    boolean res = library.loginUserMenu(testFilePathUsers);
    assertTrue(res);
  }

  @Test
  public void testLoginUser_SuccessfulLogin() throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams
    createTestFileUsers();

    boolean result = library.loginUser(new User(1, "Hasan", "Taşkın", "hasan@gmail.com", "Qwe123!"), testFilePathUsers);

    // Check if the login was successful
    assertTrue(result);
  }

  @Test
  public void testLoginUser_UnsuccessfulLogin() throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams
    createTestFileUsers();

    boolean result = library.loginUser(new User(1, "Hasan", "Taşkın", "hasan@gmail.com", "WRONGPASSWORD!"),
        testFilePathUsers);

    // Check if the login was successful
    assertFalse(result);
  }

  @Test
  public void testHandleInputError() {

    // Call handleInputError method
    boolean result = library.handleInputError();

    // Check if the result is false
    assertFalse(result);
  }

  @Test
  public void testTryParseInt_ValidInput() {
    // Test with a valid integer string
    String validInput = "123";
    int result = library.tryParseInt(validInput);
    assertEquals(123, result);
  }

  @Test
  public void testTryParseInt_InvalidInput() {
    // Test with an invalid integer string
    String invalidInput = "abc";
    int result = library.tryParseInt(invalidInput);
    assertEquals(-1, result);
  }

  @Test
  public void testTryParseInt_EmptyInput() {
    // Test with an empty string
    String emptyInput = "";
    int result = library.tryParseInt(emptyInput);
    assertEquals(-1, result);
  }

  @Test
  public void testEnterToContinue() {
    // Simulate user pressing enter
    boolean result = library.enterToContinue();

    // Check if the message is printed
    assertTrue(outContent.toString().contains("Press enter to continue..."));

    // Check if the result is true
    assertTrue(result);
  }

  @Test
  public void testEnterToContinue_NotTestMode() throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());

    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));

    // Call the method that handles register menu
    boolean res = library.enterToContinue();
    assertTrue(res);
  }

  @Test
  public void testClearScreen_NonWindowsOS() throws IOException, InterruptedException {
    // Prepare
    System.setProperty("os.name", "Linux"); // Simulating a non-Windows OS

    // Mock System.out

    // Call the method
    library.clearScreen();
  }

  @Test
  public void testLoadOwnedBooks() throws FileNotFoundException, IOException, ClassNotFoundException {
    createTestFileBooks();
    // Call loadOwnedBooks method
    List<Book> result = library.loadOwnedBooks(testFilePathBooks);

    // Check if only books owned by user with id 1 are loaded
    assertEquals(1, result.size());
  }

  @Test
  public void testLoadBooks_FileNotFound() throws FileNotFoundException, ClassNotFoundException, IOException {
    List<Book> result = library.loadBooks("nonexistent_file.bin", 1);
    assertEquals(0, result.size());
  }

  @Test
  public void testLoadWishlist_FileNotFound() throws FileNotFoundException, ClassNotFoundException, IOException {
    // Call loadWishlist method with non-existent file
    List<Book> result = library.loadWishlist("nonexistent_file.bin", 1);
    assertEquals(0, result.size());

  }

  @Test
  public void testLoadWishlistedBooks() throws FileNotFoundException, IOException, ClassNotFoundException {
    createTestFileWishlists();

    List<Book> result = library.loadWishlistedBooks(testFilePathWishlist);

    // Check if only books in the wishlist of user with id 1 are loaded
    assertEquals(1, result.size());
  }

  @Test
  public void testLoadBooksExcludingUser_FileNotFound() throws ClassNotFoundException, IOException {
    // Call loadBooksExcludingUser method with non-existent file
    List<Book> result = library.loadBooksExcludingUser("nonexistent_file.bin");
    assertEquals(0, result.size());
  }

  @Test
  public void testLoadBooksExcludingUser() throws ClassNotFoundException, IOException {
    createTestFileBooks();

    // Call loadBooksExcludingUser method
    List<Book> result = library.loadBooksExcludingUser(testFilePathBooks);

    // Check if books not owned by user with id 1 are loaded
    assertEquals(3, result.size());
  }

  @Test
  public void testLoadLoanedHistoriesBorrowedBooks_FileNotFound() throws ClassNotFoundException, IOException {
    List<LoanedHistory> result = library.loadLoanedHistoriesBorrowedBooks("nonexistent_file.bin");
    assertEquals(0, result.size());
  }

  @Test
  public void testLoadLoanedHistoriesBorrowedBooks() throws FileNotFoundException, IOException, ClassNotFoundException {
    // Prepare test data
    List<LoanedHistory> testHistories = new ArrayList<>();
    testHistories.add(new LoanedHistory(1, "Book1", 1, "Owner1", 1, "User1", false, true));
    testHistories.add(new LoanedHistory(2, "Book2", 2, "Owner2", 2, "User2", false, true));
    testHistories.add(new LoanedHistory(3, "Book3", 3, "Owner3", 1, "User1", true, true));
    testHistories.add(new LoanedHistory(4, "Book4", 4, "Owner4", 1, "User1", false, true));

    // Write test data to file
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(testFilePathHistories))) {
      oos.writeObject(testHistories);
    }

    List<LoanedHistory> result = library.loadLoanedHistoriesBorrowedBooks(testFilePathHistories);

    // Check if only loaned histories where user is the debtor and has not given
    // back are loaded
    assertEquals(2, result.size());
  }

  @Test
  public void testFindLCS_WhenBothStringsAreEqual() {

    double result = library.findLCS("abcd", "abcd");

    assertEquals(1.0, result, 0.001);
  }

  @Test
  public void testLoadLoanedHistoriesGivenBooks_FileNotFound() throws ClassNotFoundException, IOException {

    // Call loadLoanedHistoriesGivenBooks method with non-existent file
    List<LoanedHistory> result = library.loadLoanedHistoriesGivenBooks("nonexistent_file.bin");
    assertEquals(0, result.size());

  }

  @Test
  public void testLoadLoanedHistoriesGivenBooks() throws FileNotFoundException, IOException, ClassNotFoundException {

    // Prepare test data
    List<LoanedHistory> testHistories = new ArrayList<>();
    testHistories.add(new LoanedHistory(1, "Book1", 1, "Owner1", 1, "User1", false, true));
    testHistories.add(new LoanedHistory(2, "Book2", 2, "Owner2", 2, "User2", false, true));
    testHistories.add(new LoanedHistory(3, "Book3", 1, "Owner1", 2, "User2", false, true));
    testHistories.add(new LoanedHistory(4, "Book4", 2, "Owner2", 1, "User1", false, true));

    // Write test data to file
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(testFilePathHistories))) {
      oos.writeObject(testHistories);
    }

    // Call loadLoanedHistoriesGivenBooks method
    List<LoanedHistory> result = library.loadLoanedHistoriesGivenBooks(testFilePathHistories);

    // Check if only loaned histories where user is the owner and has not given back
    // are loaded
    assertEquals(2, result.size());
  }

  @Test
  public void testLoadLoanedHistories_FileNotFound() throws ClassNotFoundException, IOException {

    // Call loadLoanedHistories method with non-existent file
    List<LoanedHistory> result = library.loadLoanedHistories("nonexistent_file.bin");
    assertEquals(0, result.size());

  }

  @Test
  public void testLoadLoanedHistories() throws FileNotFoundException, IOException, ClassNotFoundException {

    // Prepare test data
    List<LoanedHistory> testHistories = new ArrayList<>();
    testHistories.add(new LoanedHistory(1, "Book1", 1, "Owner1", 1, "User1", false, true));
    testHistories.add(new LoanedHistory(2, "Book2", 2, "Owner2", 2, "User2", false, true));
    testHistories.add(new LoanedHistory(3, "Book3", 1, "Owner1", 2, "User2", false, true));

    // Write test data to file
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(testFilePathHistories))) {
      oos.writeObject(testHistories);
    }

    // Call loadLoanedHistories method
    List<LoanedHistory> result = library.loadLoanedHistories(testFilePathHistories);

    // Check if all loaned histories are loaded
    assertEquals(testHistories.size(), result.size());
  }

  @Test
  public void testGetNewBookId_NoBooksInFile() throws FileNotFoundException, ClassNotFoundException, IOException {
    // Call getNewBookId method with empty file
    int result = library.getNewBookId("empty_file.bin");
    assertEquals(1, result); // Expect new book id to be 1 when no books exist

  }

  @Test
  public void testGetNewBookId_ExistingBooksInFile() throws FileNotFoundException, IOException, ClassNotFoundException {
    createTestFileBooks();

    // Call getNewBookId method
    int result = library.getNewBookId(testFilePathBooks);

    // Check if new book id is one greater than the maximum existing book id
    assertEquals(5, result);
  }

  @Test
  public void testGetNewWishlistId_NoBooksInWishlist()
      throws FileNotFoundException, ClassNotFoundException, IOException {
    // Call getNewWishlistId method with empty wishlist
    int result = library.getNewWishlistId("empty_wishlist.bin");
    assertEquals(1, result); // Expect new wishlist id to be 1 when no books exist in wishlist
  }

  @Test
  public void testGetNewWishlistId_ExistingBooksInWishlist()
      throws FileNotFoundException, ClassNotFoundException, IOException {

    // Prepare test data
    createTestFileWishlists();

    // Call getNewWishlistId method
    int result = library.getNewWishlistId(testFilePathWishlist);
    // Check if new wishlist id is one greater than the maximum existing book id in
    // wishlist
    assertEquals(5, result);

  }

  @Test
  public void testRandomizedQuickSortBookIds() {

    // Prepare test data
    List<Book> testBooks = new ArrayList<>();
    testBooks
        .add(new Book(2, "Book2", "Author2", "Genre2", new User(2, "User2", "User2", "example.com", "Qwe123!"), 13));
    testBooks
        .add(new Book(1, "Book1", "Author1", "Genre1", new User(2, "User2", "User2", "example.com", "Qwe123!"), 123));
    testBooks
        .add(new Book(4, "Book4", "Author4", "Genre4", new User(2, "User2", "User2", "example.com", "Qwe123!"), 153));
    testBooks
        .add(new Book(3, "Book3", "Author3", "Genre3", new User(2, "User2", "User2", "example.com", "Qwe123!"), 1));

    // Sort the testBooks list using randomizedQuickSortBookIds method
    library.randomizedQuickSortBookIds(testBooks, 0, testBooks.size() - 1);

    // Check if the testBooks list is sorted in ascending order of book ids
    for (int i = 0; i < testBooks.size() - 1; i++) {
      assertTrue(testBooks.get(i).getId() <= testBooks.get(i + 1).getId());
    }
  }

  @Test
  public void testGetNewUserId_NoUsersInFile() throws FileNotFoundException, ClassNotFoundException, IOException {
    int result = library.getNewUserId("empty_users.bin");
    assertEquals(1, result); // Expect new user id to be 1 when no users exist

  }

  @Test
  public void testGetNewUserId_ExistingUsersInFile() throws FileNotFoundException, ClassNotFoundException, IOException {
    createTestFileUsers();

    // Call getNewUserId method
    int result = library.getNewUserId(testFilePathUsers);
    // Check if new user id is one greater than the maximum existing user id
    assertEquals(4, result);

  }

  @Test
  public void testHeapify() {
    // Parent value is smaller than child value
    int[] arr2 = { 3, 5, 4 };
    assertTrue(library.heapify(arr2, arr2.length, 0)); // Should return true
  }

  @Test
  public void testHeapSort() {

    // Test 1: Sorting an array in ascending order
    int[] arr1 = { 12, 11, 13, 5, 6, 7 };
    int[] expected1 = { 5, 6, 7, 11, 12, 13 };
    library.heapSort(arr1);
    assertArrayEquals(expected1, arr1);

    // Test 2: Sorting an array in descending order
    int[] arr2 = { 9, 8, 7, 6, 5, 4 };
    int[] expected2 = { 4, 5, 6, 7, 8, 9 };
    library.heapSort(arr2);
    assertArrayEquals(expected2, arr2);
  }

  @Test
  public void testPrintMainMenu() {
    // Call the method that prints the main menu
    boolean res = library.printMainMenu();

    assertTrue(res);
  }

  @Test
  public void testUserMenu() throws InterruptedException, IOException {
    boolean res = library.userMenu();

    assertTrue(res);
  }

  @Test
  public void testSuggestBooksToBorrow_NoCommonBooks()
      throws ClassNotFoundException, IOException, InterruptedException {

    List<Book> result = library.suggestBooksToBorrow("test_books.bin");

    assertEquals(0, result.size());
  }

  @Test
  public void testSuggestBooksToBorrow_CommonBooksExist()
      throws ClassNotFoundException, IOException, InterruptedException {
    createTestFileBooks();

    List<Book> result = library.suggestBooksToBorrow(testFilePathBooks);

    assertEquals(3, result.size());
  }

  @Test
  public void testFindCommonBooks_NoCommonBooks() throws ClassNotFoundException, IOException {

    List<Book> result = library.findCommonBooks("test_books.bin");

    assertEquals(0, result.size());
  }

  @Test
  public void testFindCommonBooks_CommonBooksExist() throws ClassNotFoundException, IOException {
    createTestFileBooks();
    List<Book> result = library.findCommonBooks(testFilePathBooks);

    assertEquals(3, result.size());
  }

  @Test
  public void testFindLCS_WhenOneStringIsSubsetOfTheOther() {

    double result = library.findLCS("abc", "abcd");

    assertEquals(1, result, 0.001);
  }

  @Test
  public void testFindLCS_WhenNoCommonSubsequence() {

    double result = library.findLCS("abc", "def");

    assertEquals(0.0, result, 0.001);
  }

  @Test
  public void testWriteBooksToConsole_BooksNotEmpty() throws ClassNotFoundException, IOException {
    createTestFileBooks();

    boolean result = library.writeBooksToConsole(testFilePathBooks);

    assertTrue(result);
  }

  @Test
  public void testWriteWishlistToConsole_WishlistNotEmpty() throws ClassNotFoundException, IOException {
    createTestFileWishlists();

    List<Book> result = library.loadWishlistedBooks(testFilePathWishlist);
    assertEquals(1, result.size());
  }

  @Test
  public void testWriteWishlistToConsole_WishlistEmpty() throws ClassNotFoundException, IOException {
    createTestFileWishlists();
    boolean result = library.writeWishlistToConsole("test_empty_wishlist.bin");

    assertFalse(result);
  }

  @Test
  public void testWriteBorrowedBooksToConsole_NoBooksBorrowed() throws IOException, ClassNotFoundException {

    boolean result = library.writeBorrowedBooksToConsole("test_no_borrowed_books.bin");

    assertFalse(result);
  }

  @Test
  public void testWriteGivenBooksToConsole_NoBooksGiven_ReturnFalse()
      throws FileNotFoundException, IOException, ClassNotFoundException {

    boolean result = library.writeGivenBooksToConsole("test_no_given_books.bin");

    assertFalse(result);
  }

  @Test
  public void testWriteGivenBooksToConsole_HasGivenBooks_ReturnTrue()
      throws FileNotFoundException, IOException, ClassNotFoundException {
    // Prepare test data
    List<LoanedHistory> testHistories = new ArrayList<>();
    testHistories.add(new LoanedHistory(1, "Book1", 1, "Owner1", 2, "User1", false, true));
    // Write test data to file
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(testFilePathHistories))) {
      oos.writeObject(testHistories);
    }
    boolean result = library.writeGivenBooksToConsole(testFilePathHistories);

    assertTrue(result);
  }

  @Test
  public void testWriteBooksExcludingUser_NoBooksToBorrow_ReturnFalse()
      throws FileNotFoundException, IOException, ClassNotFoundException {

    // Prepare test data
    List<Book> testBooks = new ArrayList<>();
    testBooks.add(new Book(1, "Book1", "Author1", "Genre1", new User(21, "User21", "User1", "example.com", "Qwe123!"),
        123));
    testBooks.get(0).setIsBorrowed(true);

    // Write test data to file
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(testFilePathBooks))) {
      oos.writeObject(testBooks);
    }

    List<LoanedHistory> testHistories = new ArrayList<>();
    testHistories.add(new LoanedHistory(1, "Book1", 2, "Owner1", 1, "User1", false, true));
    // Write test data to file
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(testFilePathHistories))) {
      oos.writeObject(testHistories);
    }

    boolean result = library.writeBooksExcludingUser(testFilePathBooks, testFilePathHistories);

    assertFalse(result);
  }

  @Test
  public void testWriteBooksExcludingUser_BooksAvailable() throws ClassNotFoundException, IOException {
    createTestFileBooks();

    boolean result = library.writeBooksExcludingUser(testFilePathBooks, testFilePathHistories);

    assertTrue(result);
  }

  @Test
  public void testBookCatalogingMenuOutput() throws InterruptedException, IOException {
    boolean res = library.bookCatalogingMenu();
    assertTrue(res);
  }

  @Test
  public void testuserOperations_InvalidInputShouldReturnFalse()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "qwe\n4\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    boolean res = library.userOperations(testFilePathBooks, testFilePathHistories, testFilePathWishlist);
    assertFalse(res);
  }

  @Test
  public void testuserOperations_InvalidChoiceShouldReturnFalse()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "5\n4\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    boolean res = library.userOperations(testFilePathBooks, testFilePathHistories, testFilePathWishlist);
    assertFalse(res);
  }

  @Test
  public void testuserOperations_ShouldEnterEveryFunctionAndReturnTrue()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "1\n8\n\n2\n6\n3\n7\n4\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    boolean res = library.userOperations(testFilePathBooks, testFilePathHistories, testFilePathWishlist);
    assertFalse(res);
  }

  @Test
  public void testMainMenu_InvalidInputShouldReturnZero()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "qwe\n3\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    int res = library.mainMenu(testFilePathUsers, testFilePathBooks, testFilePathHistories, testFilePathWishlist);
    assertEquals(res, 0);
  }

  @Test
  public void testMainMenu_InvalidChoiceShouldReturnZero()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streamsqweqwe

    String inputString = "5\n3\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    int res = library.mainMenu(testFilePathUsers, testFilePathBooks, testFilePathHistories, testFilePathWishlist);
    assertEquals(res, 0);
  }

  @Test
  public void testMainMenu_ShouldEnterEveryFunctionAndReturnZero()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "2\nqwe\nqwe\nqwe\nqwe\n1\nqwe\nqwe\n4\n3\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    int res = library.mainMenu(testFilePathUsers, testFilePathBooks, testFilePathHistories, testFilePathWishlist);
    assertEquals(res, 0);
  }

  @Test
  public void testBookCataloging_InvalidInputShouldReturnFalse()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "qwe\n8\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    boolean res = library.bookCataloging(testFilePathBooks);
    assertFalse(res);
  }

  @Test
  public void testBookCataloging_InvalidChoiceShouldReturnFalse()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "53\n8\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    boolean res = library.bookCataloging(testFilePathBooks);
    assertFalse(res);
  }

  @Test
  public void testBookCataloging_ShouldEnterEveryFunctionAndReturnFalse()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams
    createTestFileBooks();
    String inputString = "1\nqwe\nqwe\nqwe\n13\n3\n1\nqwe1\nqwe1\nqwe1\n14\n2\n1\n4\n5\n13\n6\n\n7\n\n8\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;
    library.setLoggedUser(new User(1, "Hasan", "Taşkın", "hasan@gmail.com", "Qwe123!"));

    // Call the method that handles register menu
    boolean res = library.bookCataloging(testFilePathBooks);
    assertFalse(res);
  }

  @Test
  public void testWishList_InvalidInputShouldReturnFalse()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "qwe\n7\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    boolean res = library.wishList(testFilePathBooks, testFilePathWishlist);
    assertFalse(res);
  }

  @Test
  public void testWishList_InvalidChoiceShouldReturnFalse()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "53\n7\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles
    boolean res = library.wishList(testFilePathBooks, testFilePathWishlist);
    assertFalse(res);
  }

  @Test
  public void testWishlist_ShouldEnterEveryFunctionAndReturnFalse()
      throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams
    createTestFileBooks();
    String inputString = "1\nqwe\nqwe\nqwe\n13\n3\n1\n1\nqwe1\nqwe1\nqwe1\n14\n2\n1\n4\n5\n123\n6\n\n7\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;
    library.setLoggedUser(new User(1, "Hasan", "Taşkın", "hasan@gmail.com",
        "Qwe123!"));

    // Call the method that handles
    boolean res = library.wishList(testFilePathBooks, testFilePathWishlist);
    assertFalse(res);
  }

  @Test
  public void testAddBookMenu_ShouldAddBook() throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "Example\nExample\nExample\n123\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    boolean res = library.addBookMenu(testFilePathBooks);
    assertTrue(res);
  }

  @Test
  public void testAddBookMenu_ShouldntAddBook() throws IOException, InterruptedException, ClassNotFoundException {
    // Prepare input and output streams

    String inputString = "\n\n\n\n";
    InputStream in = new ByteArrayInputStream(inputString.getBytes());
    Scanner testScanner = new Scanner(in);
    library = new Bookshelforganizer(testScanner, new PrintStream(outContent));
    library.isTestMode = true;

    // Call the method that handles register menu
    boolean res = library.addBookMenu(testFilePathBooks);
    assertFalse(res);
  }

}
