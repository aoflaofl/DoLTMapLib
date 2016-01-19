package com.spamalot.dolt.map;

/**
 * Ye Olde Directions Enum.
 * 
 * @author gej
 *
 */
enum Direction {
  /**
   * Down.
   */
  DOWN(0, 1),
  /**
   * Left.
   */
  LEFT(-1, 0),
  /**
   * Lef --- uh, I mean Right.
   */
  RIGHT(1, 0),
  /**
   * Up.
   */
  UP(0, -1);

  /**
   * Differential for moving in the horizontal direction.
   */
  private int horizontalDifferential;

  /**
   * Differential for moving in the vertical direction.
   */
  private int verticalDifferential;

  /**
   * Construct Direction ENUM.
   * 
   * @param hDiff
   *          horizontal difference
   * @param vDiff
   *          vertical difference
   */
  Direction(final int hDiff, final int vDiff) {
    sethDiff(hDiff);
    setvDiff(vDiff);
  }

  /**
   * Get Horizontal Differential.
   * 
   * @return the horizontal Differential.
   */
  public int gethDiff() {
    return horizontalDifferential;
  }

  /**
   * Get Vertical Differential.
   * 
   * @return the Vertical Differential.
   */
  public int getvDiff() {
    return verticalDifferential;
  }

  /**
   * Set horizontal difference.
   * 
   * @param hDiff
   *          horizontal difference
   */
  private void sethDiff(final int hDiff) {
    this.horizontalDifferential = hDiff;
  }

  /**
   * Set vertical difference.
   * 
   * @param vDiff
   *          vertical difference
   */
  private void setvDiff(final int vDiff) {
    this.verticalDifferential = vDiff;
  }
}