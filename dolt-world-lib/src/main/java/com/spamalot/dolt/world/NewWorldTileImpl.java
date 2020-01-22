package com.spamalot.dolt.world;

import com.spamalot.dolt.world.grid.GridCell;
import com.spamalot.dolt.world.grid.QuadGridCell;

import java.util.ArrayList;
import java.util.List;

public class NewWorldTileImpl extends QuadGridCell {

  /** The MapTileType of this MapTile. */
  private WorldTileType tileType;

  /**
   * List of Adjacent MapTiles that are water.
   * 
   * @return List of adjacent MapTiles that are water
   */
  public List<NewWorldTileImpl> getAdjacentWaterTiles() {
    List<NewWorldTileImpl> p = new ArrayList<>();
    for (GridCell x : this.getNeighborGridCells()) {
      if (((NewWorldTileImpl) x).getType() == WorldTileType.WATER) {
        p.add((NewWorldTileImpl) x);
      }
    }

    return p;
  }

  /**
   * Return a random water MapTile.
   * 
   * @return a MapTile with water, or null if there is none
   */
  public NewWorldTileImpl getRandomAdjacentWaterTile() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Get this MapTile's Type.
   * 
   * @return The MapTileType
   */
  public WorldTileType getType() {
    return tileType;
  }

  /**
   * Set this MapTile's type.
   * 
   * @param type The type to set this Tile to
   */
  public void setType(final WorldTileType type) {
    tileType = type;
  }
}
