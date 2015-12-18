package com.spamalot.dolt.map;

/**
 * Ye Olde Directions Enum.
 * 
 * @author gej
 *
 */
enum Direction {
  /**
   * Up.
   */
  UP(0, -1),
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
  RIGHT(1, 0);

  public int gethDiff() {
    return hDiff;
  }

  private void sethDiff(final int hDiff) {
    this.hDiff = hDiff;
  }

  public int getvDiff() {
    return vDiff;
  }

  private void setvDiff(final int vDiff) {
    this.vDiff = vDiff;
  }

  /**
   * Differential for moving in the horizontal direction.
   */
  private int hDiff;
  /**
   * Differential for moving in the vertical direction.
   */
  private int vDiff;

  Direction(final int hDiff, final int vDiff) {
    sethDiff(hDiff);
    setvDiff(vDiff);
  }
}