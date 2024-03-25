/**

@file BookshelforganizerTest.java
@brief This file contains the test cases for the Bookshelforganizer class.
@details This file includes test methods to validate the functionality of the Bookshelforganizer class. It uses JUnit for unit testing.
*/
package com.hasan.yakup.bookshelforganizer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hasan.yakup.bookshelforganizer.Bookshelforganizer;

/**

@class BookshelforganizerTest
@brief This class represents the test class for the Bookshelforganizer class.
@details The BookshelforganizerTest class provides test methods to verify the behavior of the Bookshelforganizer class. It includes test methods for addition, subtraction, multiplication, and division operations.
@author ugur.coruh
*/
public class BookshelforganizerTest {

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
  }

  /**
   * @brief This method is executed after each test method.
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * @brief Test method to validate the addition operation.
   *
   * @details This method creates an instance of the Bookshelforganizer class and calls the `add` method with two integers. It asserts the expected result of the addition operation.
   */
  @Test
  public void testAddition() {
    Bookshelforganizer bookshelforganizer = new Bookshelforganizer();
    int result = bookshelforganizer.add(2, 3);
    assertEquals(5, result);
  }

}
