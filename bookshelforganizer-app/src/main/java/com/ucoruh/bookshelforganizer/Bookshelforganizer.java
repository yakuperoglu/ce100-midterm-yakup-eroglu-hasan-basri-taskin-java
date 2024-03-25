/**

@file Bookshelforganizer.java
@brief This file serves as a demonstration file for the Bookshelforganizer class.
@details This file contains the implementation of the Bookshelforganizer class, which provides various mathematical operations.
*/

/**

@package com.ucoruh.bookshelforganizer
@brief The com.ucoruh.bookshelforganizer package contains all the classes and files related to the Bookshelforganizer App.
*/
package com.ucoruh.bookshelforganizer;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
/**

@class Bookshelforganizer
@brief This class represents a Bookshelforganizer that performs mathematical operations.
@details The Bookshelforganizer class provides methods to perform mathematical operations such as addition, subtraction, multiplication, and division. It also supports logging functionality using the logger object.
@author ugur.coruh
*/
public class Bookshelforganizer {

  /**
   * @brief Logger for the Bookshelforganizer class.
   */
  private static final Logger logger = (Logger) LoggerFactory.getLogger(Bookshelforganizer.class);

  /**
   * @brief Calculates the sum of two integers.
   *
   * @details This function takes two integer values, `a` and `b`, and returns their sum. It also logs a message using the logger object.
   *
   * @param a The first integer value.
   * @param b The second integer value.
   * @return The sum of `a` and `b`.
   */
  public int add(int a, int b) {
    // Logging an informational message
    logger.info("Logging message");
    // Logging an error message
    logger.error("Error message");
    // Returning the sum of `a` and `b`
    return a + b;
  }
}
