package com.spamalot.dolt.map;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Range;

import com.spamalot.dolt.map.MapTile.MapTileType;

/**
 * Hold the map.
 * 
 * @author gej
 *
 */
class DoltMap {

  /**
   * The MapTiles that make up this Map.
   */
  private MapTile[][] mapTiles;

  /**
   * Return the Map Tiles Array.
   * 
   * @return The Map Tiles Array
   */
  public final MapTile[][] getMapTiles() {
    return mapTiles;
  }

  /**
   * Actual width of the Map.
   */
  private final int mapWidth;

  /**
   * Actual height of the Map.
   */
  private final int mapHeight;

  private Range<Integer> widthRange;

  private Range<Integer> heightRange;

  /**
   * Construct a Map Object.
   * 
   * @param width
   *          Width of the Map
   * @param height
   *          Height of the Map
   */
  DoltMap(final int width, final int height) {
    mapWidth = width;
    mapHeight = height;

    widthRange = Range.closedOpen(Integer.valueOf(0), Integer.valueOf(mapWidth));
    heightRange = Range.closedOpen(Integer.valueOf(0), Integer.valueOf(mapHeight));

    // mapTiles = new MapTile[width][height];
    mapTiles = initMapTiles(width, height);
    linkTiles();
  }

  /**
   * Create links between neighboring map tiles.
   * 
   * @param width
   *          Width of the map
   * @param height
   *          Height of the map
   * @return A 2D array of MapTiles
   */
  private static MapTile[][] initMapTiles(final int width, final int height) {
    MapTile[][] spackle = new MapTile[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        spackle[i][j] = new MapTile(MapTileType.WATER);
      }
    }
    return spackle;
  }

  private void linkTiles() {
    for (int i = 0; i < mapWidth; i++) {
      for (int j = 0; j < mapHeight; j++) {
        MapTile left = getMapTileInDirection(i, j, Direction.LEFT);
        MapTile right = getMapTileInDirection(i, j, Direction.RIGHT);
        MapTile up = getMapTileInDirection(i, j, Direction.UP);
        MapTile down = getMapTileInDirection(i, j, Direction.DOWN);

        mapTiles[i][j].add(left, right, up, down);
      }
    }
  }

  /**
   * Get a map tile in a direction.
   * 
   * @param i
   *          Horizontal coordinate
   * @param j
   *          Vertical coordinate
   * @param dir
   *          Direction to get map tile
   * @return The map tile in that direction
   */
  private MapTile getMapTileInDirection(final int i, final int j, final Direction dir) {
    checkNotNull(dir);

    return getMapTile(i + dir.gethDiff(), j + dir.getvDiff());
  }

  /**
   * Get the map tile.
   * 
   * @param i
   *          Horizontal coordinate
   * @param j
   *          Vertical coordinate
   * @return The map tile at those coordinates
   */
  private MapTile getMapTile(final int i, final int j) {
    MapTile result = null;
    if (isOnMap(i, j)) {
      result = mapTiles[i][j];
    }
    return result;
  }

  /**
   * Check if coordinate is on map.
   * 
   * @param x
   *          The X ordinate
   * @param y
   *          The Y ordinate
   * @return true if this Coordinate is on the Map
   */
  private boolean isOnMap(final int x, final int y) {
    return widthRange.contains(x) && heightRange.contains(y);
  }

  @Override
  public final String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append('+');
    for (int x = 0; x < mapWidth; x++) {
      sb.append("-+");
    }
    sb.append('\n');
    for (int y = 0; y < mapHeight; y++) {
      sb.append('|');
      for (int x = 0; x < mapWidth; x++) {
        sb.append(mapTiles[x][y]);
        if (mapTiles[x][y].isInSameTerritory(mapTiles[x][y].getRight())) {
          sb.append(' ');
        } else {
          sb.append('|');
        }
      }
      sb.append('\n');
      sb.append('+');
      for (int x = 0; x < mapWidth; x++) {
        if (mapTiles[x][y].isInSameTerritory(mapTiles[x][y].getDown())) {
          sb.append(" +");
        } else {
          sb.append("-+");
        }
      }
      sb.append('\n');
    }
    return sb.toString();
  }

}
