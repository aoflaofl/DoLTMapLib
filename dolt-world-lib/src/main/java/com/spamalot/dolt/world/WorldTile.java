package com.spamalot.dolt.world;

//import com.spamalot.dolt.map.Territory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A tile on the Map.
 * 
 * @author gej
 *
 */
public class WorldTile<T extends MapTileFeatures> {

  /** Hold a static Random Number Generator for efficiency. */
  private static final Random RNG = new Random();

  /** The MapTiles that link to this one. Only orthogonal MapTiles apply. */
  private final Map<Direction, WorldTile<T>> linkedTiles = new EnumMap<>(Direction.class);

  private boolean offLimits;

  /** The MapTileType of this MapTile. */
  private WorldTileType tileType;

  /**
   * Add a MapTile to this one's linked list.
   * 
   * @param dir  The direction
   * @param tile The tile
   */
  void linkTileInDirection(final Direction dir, final WorldTile<T> tile) {
    this.linkedTiles.put(dir, tile);
  }

  public final WorldTile<T> get(final Direction y) {
    return this.linkedTiles.get(y);
  }

  /**
   * List of Adjacent MapTiles that are water.
   * 
   * @return List of adjacent MapTiles that are water
   */
  public List<WorldTile<T>> getAdjacentWaterTiles() {
    final List<WorldTile<T>> waterList = new ArrayList<>();
    for (final WorldTile<T> linkedTile : this.linkedTiles.values()) {
      if (linkedTile != null && linkedTile.getType() == WorldTileType.WATER && !linkedTile.isOffLimits()) {
        waterList.add(linkedTile);
      }
    }
    return waterList;
  }

  /**
   * Return a random water MapTile.
   * 
   * @return a MapTile with water, or null if there is none
   */
  public WorldTile<T> getRandomAdjacentWaterTile() {
    WorldTile<T> ret = null;
    final List<WorldTile<T>> waterList = getAdjacentWaterTiles();
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
    return this.tileType;
  }

  private boolean isOffLimits() {
    return this.offLimits;
  }

  public final void setOffLimits(final boolean flag) {
    this.offLimits = flag;
  }

  /**
   * Set this MapTile's type.
   * 
   * @param type The type to set this Tile to
   */
  private void setType(final WorldTileType type) {
    this.tileType = type;
  }

  @Override
  public final String toString() {
    String str;
    if (this.tileType == WorldTileType.LAND) {
      str = "#";
    } else {
      if (isOffLimits()) {
        str = ",";
      } else {
        str = ".";
      }
    }
    return str;
  }

  private T features;

  public void setFeatures(T f) {
    this.features = f;
  }

  public T getFeatures() {
    return this.features;
  }

}
