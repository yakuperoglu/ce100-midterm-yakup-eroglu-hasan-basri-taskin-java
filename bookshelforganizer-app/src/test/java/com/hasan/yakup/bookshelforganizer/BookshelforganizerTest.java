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

}
