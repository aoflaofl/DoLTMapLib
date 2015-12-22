package com.spamalot.dolt.map;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Range;

import com.spamalot.dolt.map.MapTile.MapTileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Hold the map.
 * 
 * @author gej
 *
 */
public class DoltMap {
  /**
   * Default minimum size of a Territory.
   */
  private static final int DEFAULT_MIN_TERRITORY_SIZE = 10;

  /**
   * Default maximum size of a territory.
   */
  private static final int DEFAULT_MAX_TERRITORY_SIZE = 20;

  /**
   * The MapTiles that make up this Map.
   */
  private MapTile[][] mapTiles;

  /**
   * A Random Number Generator.
   */
  private static final Random RNG = new Random();

  /**
   * Actual width of the Map.
   */
  private final int mapWidth;

  /**
   * Actual height of the Map.
   */
  private final int mapHeight;

  /**
   * The territories in this Map.
   */
  private final List<Territory> territories = new ArrayList<>();

  /**
   * Construct a Map Object.
   * 
   * @param width
   *          Width of the Map
   * @param height
   *          Height of the Map
   * @param numTerritories
   *          Number of Territories to put in the Map.
   */
  public DoltMap(final int width, final int height, final int numTerritories) {
    mapWidth = width;
    mapHeight = height;

    mapTiles = new MapTile[width][height];
    initMapTiles();

    addTerritories(numTerritories, DEFAULT_MIN_TERRITORY_SIZE, DEFAULT_MAX_TERRITORY_SIZE);
  }

  private void addTerritories(final int numTerritories, final int minTerritorySize, final int maxTerritorySize) {
    final Territory territory = new Territory();

    territory.buildArea(mapTiles[0][0], minTerritorySize, maxTerritorySize);
    territories.add(territory);
    for (int i = 0; i < numTerritories; i++) {
      MapTile pdio = null;
      while (pdio == null) {
        final int index = RNG.nextInt(territories.size());
        final Territory neq = territories.get(index);

        pdio = neq.getRandomAdjacentWaterTile();
      }
      final Territory ddfg = new Territory();
      ddfg.buildArea(pdio, minTerritorySize, maxTerritorySize);
      territories.add(ddfg);
    }
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
        left = getMapTile(i - 1, j, Direction.LEFT);
        right = getMapTile(i + 1, j);
        up = getMapTile(i, j - 1);
        down = getMapTile(i, j + 1);

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
