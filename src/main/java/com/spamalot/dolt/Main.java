package com.spamalot.dolt;

import com.spamalot.dolt.map.DoltWorld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mainly just for a framework.
 *
 * @author gej
 *
 */
public final class Main {
  /** Loggit. */
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  /** Number of Territories in a default Map. */
  private static final int DEFAULT_NUM_TERRITORIES = 10;

  /** Height of a default Map. */
  private static final int DEFAULT_MAP_HEIGHT = 20;

  /** Width of a default Map. */
  private static final int DEFAULT_MAP_WIDTH = 40;

  /** Instantiate nothing. */
  private Main() {
  }

  /**
   * Start here.
   * 
   * @param args Command line arguments.
   */
  public static void main(final String... args) {
    LOGGER.info("Something will happen here!");
    final DoltWorld world = new DoltWorld(DEFAULT_MAP_WIDTH, DEFAULT_MAP_HEIGHT, DEFAULT_NUM_TERRITORIES);

    LOGGER.info("World: {}", world);
  }
}
