package com.spamalot.dolt;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
  /**
   * Create the test case.
   *
   * @param testName
   *          name of the test case
   */
  public AppTest(final String testName) {
    super(testName);
  }

  /**
   * Something.
   * 
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  /**
   * Rigourous Test :-).
   */
  public static final void testApp() {
    assertTrue(true);
  }
}
