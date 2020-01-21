/**
 * 
 */
package com.spamalot.dolt.world.grid;

/**
 * @author gejohann
 *
 */
public class QuadGridCell extends GridCell {

  @Override
  boolean isGoodDir(final Direction dir) {
    if (dir.equals(Direction.DOWN)) {
      return true;
    }

    return false;
  }
}
