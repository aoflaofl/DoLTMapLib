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
   * The MapTiles that make up this Territory. Make it a SetUniqueList because
   * at some point we will need to pick a random MapTile.
   */
  private final SetUniqueList<MapTile> territoryTiles = SetUniqueList.setUniqueList(new ArrayList<MapTile>());

  SetUniqueList<MapTile> getTerritoryTiles() {
    return territoryTiles;
  }

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
   * @return a Territory.
   */
  public Territory buildArea(final MapTile startTile, final int minSize, final int maxSize) {
    if (startTile.getType() != MapTileType.WATER) {
      throw new IllegalArgumentException("Start tile must be water.");
    }

    final int targetSize = getRandomTargetSize(minSize, maxSize);

    generateRandomArea(startTile, targetSize);

    int size = territoryTiles.size();

    Territory result = null;
    if (Range.closed(minSize, maxSize).contains(size)) {
      result = this;
    } else {
      // TODO: Also inform map not to use these water tiles.
      removeArea();
      result = null;
    }

    return result;
  }

  private void generateRandomArea(final MapTile startTile, final int targetSize) {

    startTile.setType(MapTileType.LAND);
    startTile.setTerritory(this);
    territoryTiles.add(startTile);

    MapTile tile = startTile;
    int size = 1;
    for (; size < targetSize; size++) {
      tile = tile.getRandomAdjacentWaterTile();
      if (tile == null) {
        tile = TerritoryBuilder.getRandomAdjacentWaterTile(territoryTiles);
        if (tile == null) {
          break;
        }
      }
      tile.setType(MapTileType.LAND);
      tile.setTerritory(this);
      territoryTiles.add(tile);

    }
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

  public boolean containsTile(final MapTile down) {
    return territoryTiles.contains(down);
  }
}
