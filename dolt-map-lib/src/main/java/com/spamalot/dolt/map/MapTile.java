package com.spamalot.dolt.map;

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
class MapTile {

  /** Hold a static Random Number Generator for efficiency. */
  private static final Random RNG = new Random();

  /** The MapTiles that link to this one. Only orthogonal MapTiles apply. */
  private final Map<Direction, MapTile> linkedTiles = new EnumMap<>(Direction.class);

  private boolean offLimits;

  /** The Territory this Map Tile is in. */
  private Territory territory;

  /** The MapTileType of this MapTile. */
  private MapTileType tileType;

  /**
   * Construct a MapTile Object.
   * 
   * @param type What type of Tile this is.
   */
  MapTile(final MapTileType type) {
    this.tileType = type;
  }

  /**
   * Add a MapTile to this one's linked list.
   * 
   * @param dir  The direction
   * @param tile The tile
   */
  void linkTileInDirection(final Direction dir, final MapTile tile) {
    this.linkedTiles.put(dir, tile);
  }

  public MapTile get(final Direction y) {
    return this.linkedTiles.get(y);
  }

  /**
   * List of Adjacent MapTiles that are water.
   * 
   * @return List of adjacent MapTiles that are water
   */
  public List<MapTile> getAdjacentWaterTiles() {
    final List<MapTile> waterList = new ArrayList<>();
    for (final MapTile linkedTile : this.linkedTiles.values()) {
      if (linkedTile != null && linkedTile.getType() == MapTileType.WATER && !linkedTile.isOffLimits()) {
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
  public MapTile getRandomAdjacentWaterTile() {
    MapTile ret = null;
    final List<MapTile> waterList = getAdjacentWaterTiles();
    if (!waterList.isEmpty()) {

      final int index = RNG.nextInt(waterList.size());
      ret = waterList.get(index);
    }
    return ret;
  }

  public Territory getTerritory() {
    return this.territory;
  }

  /**
   * Get this MapTile's Type.
   * 
   * @return The MapTileType
   */
  public MapTileType getType() {
    return this.tileType;
  }

  public boolean isInSameTerritory(final MapTile down) {

    return this.territory != null && this.territory.containsTile(down);
  }

  private boolean isOffLimits() {
    return this.offLimits;
  }

  public void setOffLimits(final boolean flag) {
    this.offLimits = flag;
  }

  public void setTerritory(final Territory ter) {
    this.territory = ter;

  }

  /**
   * Set this MapTile's type.
   * 
   * @param type The type to set this Tile to
   */
  public void setType(final MapTileType type) {
    this.tileType = type;
  }

  @Override
  public String toString() {
    String str;
    if (this.tileType == MapTileType.LAND) {
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

}
