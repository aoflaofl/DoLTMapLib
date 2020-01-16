package com.spamalot.dolt.world;

/**
 * Ye Olde Directions Enum.
 * 
 * @author gej
 *
 */
public enum Direction {
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
  private int horizDiff;

  /**
   * Differential for moving in the vertical direction.
   */
  private int verticalDifferential;

  /**
   * Construct Direction ENUM.
   * 
   * @param horizontalDiff
   *          horizontal difference
   * @param verticalDiff
   *          vertical difference
   */
  Direction(final int horizontalDiff, final int verticalDiff) {
    sethDiff(horizontalDiff);
    setvDiff(verticalDiff);
  }

  /**
   * Get Horizontal Differential.
   * 
   * @return the horizontal Differential.
   */
  public int gethDiff() {
    return this.horizDiff;
  }

  /**
   * Get Vertical Differential.
   * 
   * @return the Vertical Differential.
   */
  public int getvDiff() {
    return this.verticalDifferential;
  }

  /**
   * Set horizontal difference.
   * 
   * @param horizontalDiff
   *          horizontal difference
   */
  private void sethDiff(final int horizontalDiff) {
    this.horizDiff = horizontalDiff;
  }

  /**
   * Set vertical difference.
   * 
   * @param verticalDiff
   *          vertical difference
   */
  private void setvDiff(final int verticalDiff) {
    this.verticalDifferential = verticalDiff;
  }
}
