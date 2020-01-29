package com.spamalot.dolt.world;

import com.spamalot.dolt.world.grid.QuadGridCell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Base Tile.
 * 
 * @author gejohann
 *
 */
public class WorldTile<T extends WorldTile<T>> extends QuadGridCell<T> {
  /** Hold a static Random Number Generator for efficiency. */
  private static final Random RNG = new Random();

  /** The MapTileType of this MapTile. */
  private WorldTileType tileType;

  /**
   * List of Adjacent MapTiles that are water.
   * 
   * @return List of adjacent MapTiles that are water
   */
  public List<T> getAdjacentWaterTiles() {
    List<T> p = new ArrayList<>();
    for (T x : this.getNeighborGridCells()) {
      if (x.getType() == WorldTileType.WATER) {
        p.add(x);
      }
    }

    return p;
  }

  public WorldTile(WorldTileType tileType) {
    super();
    this.tileType = tileType;
  }

  public WorldTile() {
    this(WorldTileType.WATER);
  }

  /**
   * Return a random water MapTile.
   * 
   * @return a MapTile with water, or null if there is none
   */
  public T getRandomAdjacentWaterTile() {
    T ret = null;
    List<T> waterList = getAdjacentWaterTiles();
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

  /**
   * Need to override this if you extend this class.
   */
  @Override
  public String toString() {
    return tileType.toString();
  }
}
