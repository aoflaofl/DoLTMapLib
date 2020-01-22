/**
 * 
 */
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
  /** The MapTiles that link to this one. Only orthogonal MapTiles apply. */
  private final Map<Direction, T> linkedTiles = new EnumMap<>(Direction.class);

  /**
   * Add a MapTile to this one's linked list.
   * 
   * @param dir  The direction
   * @param tile The tile
   */
  void linkTileInDirection(final Direction dir, final T tile) {
    if (isGoodDir(dir)) {
      this.linkedTiles.put(dir, tile);
    }
  }

  /**
   * Get a Cell in a Direction.
   * 
   * @param y a Direction
   * @return Cell in that Direction.
   */
  public final T get(final Direction y) {
    return this.linkedTiles.get(y);
  }

  protected final Collection<T> getNeighborGridCells() {
    return this.linkedTiles.values();
  }

  abstract boolean isGoodDir(Direction dir);
}
