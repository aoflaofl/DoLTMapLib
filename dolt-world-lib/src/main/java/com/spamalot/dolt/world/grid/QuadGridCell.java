/**
 * 
 */
package com.spamalot.dolt.world.grid;

import com.google.common.collect.Sets;

import java.util.HashSet;

/**
 * Orthogonally linked cells.
 * 
 * @author gej
 *
 * @param <T> Cell class
 */
public class QuadGridCell<T extends GridCell<? super T>> extends GridCell<T> {
  /** Set of allowed directions. */
  private HashSet<Direction> allowedDirections;

  /**
   * Handle a cell that links Orthogonally.
   */
  public QuadGridCell() {
    super();
    allowedDirections = Sets.newHashSet(Direction.values());
  }

  @Override
  final boolean isGoodDir(final Direction dir) {
    return allowedDirections.contains(dir);
  }
}
