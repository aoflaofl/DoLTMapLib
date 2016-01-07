package com.spamalot.dolt;

import com.spamalot.dolt.map.DoltWorld;

/**
 * Mainly just for a framework.
 * 
 * @author gej
 *
 */
public final class Main {
  /**
   * Number of Territories in a defualt Map.
   */
  private static final int DEFAULT_NUM_TERRITORIES = 40;

  /**
   * Height of a default Map.
   */
  private static final int DEFAULT_MAP_HEIGHT = 20;

  /**
   * Width of a default Map.
   */
  private static final int DEFAULT_MAP_WIDTH = 40;

  /**
   * I regret nothing.
   */
  private Main() {
  }

  /**
   * Start the damn program here.
   * 
   * @param args
   *          Command line arguments to the damn program.
   */
  public static void main(final String... args) {
    System.out.println("Something will happen here!");
    final DoltWorld world = new DoltWorld(DEFAULT_MAP_WIDTH, DEFAULT_MAP_HEIGHT, DEFAULT_NUM_TERRITORIES);

    System.out.println(world);
  }

}
