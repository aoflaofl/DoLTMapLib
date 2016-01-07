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

    mapTiles = new MapTile[width][height];
    initMapTiles();

  }

  private void initMapTiles() {
    for (int i = 0; i < mapWidth; i++) {
      for (int j = 0; j < mapHeight; j++) {
        mapTiles[i][j] = new MapTile(MapTileType.WATER);
      }
    }

    MapTile up;
    MapTile down;
    MapTile left;
    MapTile right;
    for (int i = 0; i < mapWidth; i++) {
      for (int j = 0; j < mapHeight; j++) {
        left = getMapTile(i, j, Direction.LEFT);
        right = getMapTile(i, j, Direction.RIGHT);
        up = getMapTile(i, j, Direction.UP);
        down = getMapTile(i, j, Direction.DOWN);

        mapTiles[i][j].add(left, right, up, down);
      }
    }
  }

  private MapTile getMapTile(final int i, final int j, final Direction dir) {
    checkNotNull(dir);

    return getMapTile(i + dir.gethDiff(), j + dir.getvDiff());
  }

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
    return Range.closedOpen(0, mapWidth).contains(x) && Range.closedOpen(0, mapHeight).contains(y);
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
