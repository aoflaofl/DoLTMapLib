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
final class Territory {
  /**
   * Random Number Generator.
   */
  private static final Random RNG = new Random();

  /**
   * The MapTiles that make up this Territory. Make it a SetUniqueList because
   * at some point we will need to pick a random MapTile.
   */
  private final SetUniqueList<MapTile> territoryTiles = SetUniqueList.setUniqueList(new ArrayList<MapTile>());

  private boolean offLimits;

  public boolean isOffLimits() {
    return offLimits;
  }

  public void setOffLimits(boolean offLimits) {
    this.offLimits = offLimits;
  }

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
      clearTerritoryTiles();
      result = null;
    }

    return result;
  }

  private void generateRandomArea(final MapTile startTile, final int targetSize) {

    addTileToTerritory(startTile);

    MapTile tile = startTile;
    int size = 1;
    for (; size < targetSize; size++) {
      tile = getNextTile(tile);
      if (tile == null) {
        setOffLimits(true);
        break;
      }
      addTileToTerritory(tile);

    }
  }

  private void addTileToTerritory(MapTile tile) {
    tile.setType(MapTileType.LAND);
    tile.setTerritory(this);
    territoryTiles.add(tile);
  }

  private MapTile getNextTile(final MapTile tile) {
    MapTile result = tile.getRandomAdjacentWaterTile();
    if (result == null) {
      result = TerritoryBuilder.getRandomAdjacentWaterTile(territoryTiles);
    }
    return result;
  }

  /**
   * Mark the MapTiles assigned to this Territory as water, but since there
   * failed to be enough of them in a cluster to make a Territory also mark them
   * as off limits so they won't be used in the future.
   */
  private void clearTerritoryTiles() {
    for (MapTile tile : territoryTiles) {
      tile.setType(MapTileType.WATER);
      tile.setTerritory(null);
      tile.setOffLimits();
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

  public boolean containsTile(final MapTile tile) {
    return territoryTiles.contains(tile);
  }

  public static class Builder {
    private MapTile startTile;
    private int minSize;
    private int maxSize;

    Builder(final MapTile startTile, final int minSize, final int maxSize) {
      this.startTile = startTile;
      this.minSize = minSize;
      this.maxSize = maxSize;
    }

    public Territory build() {
      Territory result = new Territory();

      return result.buildArea(startTile, minSize, maxSize);
    }
  }
}
