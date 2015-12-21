package com.spamalot.dolt.map;

import com.google.common.collect.Range;
import com.spamalot.dolt.map.MapTile.MapTileType;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.Random;

/**
 * A Territory goes into a map.
 * 
 * @author gej
 *
 */
class Territory {
  /**
   * Random Number Generator.
   */
  private static final Random RNG = new Random();

  /**
   * The MapTiles that make up this Territory.
   */
  private final SetUniqueList<MapTile> territoryTiles = SetUniqueList.setUniqueList(new ArrayList<MapTile>());

  /**
   * Construct a Territory.
   * 
   * 
   */
  Territory() {
    // Empty... for now.
  }

  /**
   * Allocate MapTiles to a Territory.
   * 
   * @param startTile
   *          Water MapTile to begin building Territory in
   * @param minSize
   *          Minimum size of the Territory
   * @param maxSize
   *          Maximum size of the Territory.
   * @return
   */
  public Territory buildArea(final MapTile startTile, final int minSize, final int maxSize) {
    if (startTile.getType() != MapTileType.WATER) {
      throw new IllegalArgumentException("Start tile must be water.");
    }

    final int targetSize = getRandomTargetSize(minSize, maxSize);

    int size = generateRandomArea(startTile, targetSize);

    Territory result = null;
    if (Range.closed(minSize, maxSize).contains(size)) {
      result = this;
    } else {
      removeArea();
      result = null;
    }

    return result;
  }

  private int generateRandomArea(final MapTile startTile, final int targetSize) {
    int size = 1;
    startTile.setType(MapTileType.LAND);
    territoryTiles.add(startTile);

    MapTile tile = startTile;
    while (size < targetSize) {
      tile = tile.getRandomAdjacentWaterTile();
      if (tile == null) {
        tile = getRandomAdjacentWaterTile();
        if (tile == null) {
          break;
        }
      }
      tile.setType(MapTileType.LAND);
      tile.setTerritory(this);
      territoryTiles.add(tile);
      size++;
    }
    return size;
  }

  private void removeArea() {
    for (MapTile a : territoryTiles) {
      a.setType(MapTileType.WATER);
    }
  }

  /**
   * Get a random target size between minSize and maxSize inclusive.
   * 
   * @param minSize
   *          minimum size
   * @param maxSize
   *          maximum size
   * @return a random size between minSize and maxSize inclusive
   */
  private int getRandomTargetSize(final int minSize, final int maxSize) {
    if (minSize > maxSize) {
      throw new IllegalArgumentException("Minimum Territory size must be less than or equal to maximum size.");
    }

    int targetSize;
    if (minSize == maxSize) {
      targetSize = minSize;
    } else {
      targetSize = minSize + RNG.nextInt(maxSize - minSize + 1);
    }

    return targetSize;
  }

  /**
   * Find a random water tile adjacent to this Territory.
   * 
   * @return a random water tile or null if there is no water tile adjacent to
   *         this Territory
   */
  public MapTile getRandomAdjacentWaterTile() {
    SetUniqueList<MapTile> waterTiles = SetUniqueList.setUniqueList(new ArrayList<MapTile>());

    for (MapTile a : territoryTiles) {
      waterTiles.addAll(a.getAdjacentWaterTiles());
    }

    MapTile result;
    if (waterTiles.isEmpty()) {
      result = null;
    } else {
      final int index = RNG.nextInt(waterTiles.size());
      result = waterTiles.get(index);
    }

    return result;
  }

  public boolean containsTile(MapTile down) {
    return territoryTiles.contains(down);
  }
}
