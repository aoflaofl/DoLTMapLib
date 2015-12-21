package com.spamalot.dolt.map;

import java.util.ArrayList;
import java.util.HashMap;
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

  /**
   * Type of this MapTile.
   * 
   */
  enum MapTileType {
    /**
     * This Tile is Land.
     */
    LAND,
    /**
     * This Tile is Water.
     */
    WATER
  }

  /**
   * Hold a static Random Number Generator so it won't be regenerated many
   * times.
   */
  private static final Random RNG = new Random();

  /**
   * The MapTiles that link to this one.
   */
  private final Map<Direction, MapTile> linkedTiles = new HashMap<>();

  /**
   * The MapTileType of this MapTile.
   */
  private MapTileType tileType;

  private Territory territory;

  /**
   * @param type
   *          What type of Tile this is.
   */
  MapTile(final MapTileType type) {
    this.tileType = type;
  }

  /**
   * Add a MapTile to this one's linked list.
   * 
   * @param dir
   *          The direction
   * @param tile
   *          The tile
   */
  private void add(final Direction dir, final MapTile tile) {
    linkedTiles.put(dir, tile);
  }

  /**
   * Add the surrounding MapTiles to the linked list of this one.
   * 
   * @param left
   *          The MapTile to the left
   * @param right
   *          The MapTile to the right
   * @param up
   *          The MapTile above
   * @param down
   *          The MapTile below
   */
  public void add(final MapTile left, final MapTile right, final MapTile up, final MapTile down) {
    add(Direction.LEFT, left);
    add(Direction.RIGHT, right);
    add(Direction.UP, up);
    add(Direction.DOWN, down);

  }

  /**
   * List of Adjacent MapTiles that are water.
   * 
   * @return List of adjacent MapTiles that are water
   */
  public List<MapTile> getAdjacentWaterTiles() {
    final List<MapTile> waterList = new ArrayList<>();
    for (final MapTile linkedTile : linkedTiles.values()) {
      if (linkedTile != null && linkedTile.getType() == MapTileType.WATER) {
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

  /**
   * Get this MapTile's Type.
   * 
   * @return The MapTileType
   */
  public MapTileType getType() {
    return tileType;
  }

  /**
   * Set this MapTile's type.
   * 
   * @param type
   *          The type to set this Tile to
   */
  public void setType(final MapTileType type) {
    tileType = type;
  }

  @Override
  public String toString() {
    String str;
    if (tileType == MapTileType.LAND) {
      str = "#";
    } else {
      str = ".";
    }
    return str;
  }

  public MapTile getDown() {
    return linkedTiles.get(Direction.DOWN);
  }

  public boolean isInSameTerritory(MapTile down) {

    return territory != null && territory.containsTile(down);
  }

  public void setTerritory(Territory ter) {
    territory = ter;

  }

  public MapTile getRight() {
    return linkedTiles.get(Direction.RIGHT);
  }
}
