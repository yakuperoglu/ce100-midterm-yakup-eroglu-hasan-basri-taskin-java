/**

@file BookshelforganizerAppTest.java
@brief This file contains the test cases for the BookshelforganizerApp class.
@details This file includes test methods to validate the functionality of the BookshelforganizerApp class. It uses JUnit for unit testing.
*/
package com.hasan.yakup.bookshelforganizer;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @class BookshelforganizerAppTest
 * @brief This class represents the test class for the BookshelforganizerApp
 *        class.
 * @details The BookshelforganizerAppTest class provides test methods to verify
 *          the behavior of the BookshelforganizerApp class. It includes test
 *          methods for successful execution, object creation, and error
 *          handling scenarios.
 * @author ugur.coruh
 */
public class BookshelforganizerAppTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final ByteArrayInputStream inContent = new ByteArrayInputStream("3".getBytes());
  BookshelforganizerApp librarysystemApp = new BookshelforganizerApp();

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setIn(inContent);
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setIn(System.in);
  }

  @Test
  public void testLibrarysystemAppMain() throws IOException, InterruptedException, ClassNotFoundException {
    String[] args = null;
    BookshelforganizerApp.main(args);

    String expectedOutputStartsWith = "Welcome To Virtual Bookshelf Organizer";
    String actualOutput = outContent.toString();

    assertEquals(true, actualOutput.startsWith(expectedOutputStartsWith));
  }
}
