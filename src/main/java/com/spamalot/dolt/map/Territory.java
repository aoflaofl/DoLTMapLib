package com.spamalot.dolt.map;

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
   */
  public void buildArea(final MapTile startTile, final int minSize, final int maxSize) {
    if (startTile.getType() != MapTileType.WATER) {
      throw new IllegalArgumentException("Start tile must be water.");
    }

    final int targetSize = getTargetSize(minSize, maxSize);

    int size = 1;
    startTile.setType(MapTileType.LAND);
    territoryTiles.add(startTile);

    MapTile tile = startTile;
    while (size < targetSize) {
      tile = tile.getRandomAdjacentWaterTile();
      if (tile == null) {
        break;
      }
      tile.setType(MapTileType.LAND);
      territoryTiles.add(tile);
      size++;
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
  private int getTargetSize(final int minSize, final int maxSize) {
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
}
