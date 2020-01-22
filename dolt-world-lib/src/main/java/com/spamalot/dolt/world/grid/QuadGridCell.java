/**
 * 
 */
package com.spamalot.dolt.world.grid;

import com.google.common.collect.Sets;

import java.util.HashSet;

/**
 * @author gejohann
 *
 */
public class QuadGridCell extends GridCell {
  /** Set of allowed directions. */
  private HashSet<Direction> allowedDirs;

  /**
   * Handle a cell that links Orthogonally.
   */
  public QuadGridCell() {
    super();
    allowedDirs = Sets.newHashSet(Direction.values());
  }

  @Override
  final boolean isGoodDir(final Direction dir) {

    if (allowedDirs.contains(dir)) {
      return true;
    }

    return false;
  }
}
