package com.spamalot.dolt.world;

import com.spamalot.dolt.world.grid.QuadGridCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewWorldTileImpl extends QuadGridCell<NewWorldTileImpl> {

  /** The MapTileType of this MapTile. */
  private WorldTileType tileType;

  /** Hold a static Random Number Generator for efficiency. */
  private static final Random RNG = new Random();

  /**
   * List of Adjacent MapTiles that are water.
   * 
   * @return List of adjacent MapTiles that are water
   */
  public List<NewWorldTileImpl> getAdjacentWaterTiles() {
    List<NewWorldTileImpl> p = new ArrayList<>();
    for (NewWorldTileImpl x : this.getNeighborGridCells()) {
      if (x.getType() == WorldTileType.WATER) {
        p.add(x);
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
    NewWorldTileImpl ret = null;
    List<NewWorldTileImpl> waterList = getAdjacentWaterTiles();
    if (!waterList.isEmpty()) {

      final int index = RNG.nextInt(waterList.size());
      ret = waterList.get(index);
    }
    return ret;
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

  @Override
  public String toString() {
    return tileType.toString();
  }
}