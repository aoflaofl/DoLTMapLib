/**
 * 
 */
package com.spamalot.dolt.world.grid;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author gejohann
 *
 */
public abstract class GridCell {
  /** The MapTiles that link to this one. Only orthogonal MapTiles apply. */
  private final Map<Direction, GridCell> linkedTiles = new EnumMap<>(Direction.class);

  /**
   * Add a MapTile to this one's linked list.
   * 
   * @param dir  The direction
   * @param tile The tile
   */
  void linkTileInDirection(final Direction dir, final GridCell tile) {
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
  public final GridCell get(final Direction y) {
    return this.linkedTiles.get(y);
  }

  protected final Collection<GridCell> getNeighborGridCells() {
    return this.linkedTiles.values();
  }

  abstract boolean isGoodDir(Direction dir);
}
