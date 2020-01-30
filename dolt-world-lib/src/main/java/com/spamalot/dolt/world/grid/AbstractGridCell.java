package com.spamalot.dolt.world.grid;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * Link logic for cells.
 * 
 * @author gej
 *
 * @param <T> Cell class
 */
abstract class AbstractGridCell<T> {
  /** Cells that link to this one. */
  private final Map<Direction, T> neighborCells = new EnumMap<>(Direction.class);

  /**
   * Link a Cell to this one.
   * 
   * @param dir  The direction
   * @param cell The cell to link
   */
  public void linkCellInDirection(final Direction dir, final T cell) {
    if (cell != null && isValidDirection(dir)) {
      this.neighborCells.put(dir, cell);
    }
  }

  /**
   * Get a Cell in a Direction.
   * 
   * @param dir a Direction
   * @return Cell in that Direction.
   */
  public final T get(final Direction dir) {
    return this.neighborCells.get(dir);
  }

  protected final Collection<T> getAllNeighborCells() {
    return this.neighborCells.values();
  }

  abstract boolean isValidDirection(Direction dir);
}
