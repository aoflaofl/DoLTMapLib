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
public abstract class GridCell<T> {
  /** Cells that link to this one. */
  private final Map<Direction, T> linkedTiles = new EnumMap<>(Direction.class);

  /**
   * Link a Cell to this one.
   * 
   * @param dir  The direction
   * @param cell The cell to link
   */
  public void linkTileInDirection(final Direction dir, final T cell) {
    if (isValidDir(dir) && cell != null) {
      this.linkedTiles.put(dir, cell);
    }
  }

  /**
   * Get a Cell in a Direction.
   * 
   * @param dir a Direction
   * @return Cell in that Direction.
   */
  public final T get(final Direction dir) {
    return this.linkedTiles.get(dir);
  }

  protected final Collection<T> getNeighborGridCells() {
    return this.linkedTiles.values();
  }

  abstract boolean isValidDir(Direction dir);
}
