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

}
