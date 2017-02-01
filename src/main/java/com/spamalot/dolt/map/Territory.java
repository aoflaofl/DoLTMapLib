package com.spamalot.dolt.map;

import com.google.common.collect.Range;

import com.spamalot.dolt.map.MapTile.MapTileType;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * A Territory goes into a map.
 * 
 * @author gej
 *
 */
final class Territory {
  public static class Builder {
    private int maximumSize;
    private int minimumSize;
    private Range<Integer> sizeRange;
    private MapTile startTile;

    Builder(final MapTile initTile, final int minSize, final int maxSize) {
      this.startTile = initTile;
      this.minimumSize = minSize;
      this.maximumSize = maxSize;
      sizeRange = Range.closed(Integer.valueOf(minSize), Integer.valueOf(maxSize));
    }

    public Territory build() {
      Territory result = new Territory();

      return result.buildArea(startTile, minimumSize, maximumSize, sizeRange);
    }
  }

  /**
   * Random Number Generator.
   */
  private static final Random RNG = new Random();

  /**
   * Get a random target size between minSize and maxSize inclusive.
   * 
   * @param minSize
   *          minimum size
   * @param maxSize
   *          maximum size
   * @return a random size between minSize and maxSize inclusive
   */
  private static int getRandomTargetSize(final int minSize, final int maxSize) {
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

  private final Set<Territory> neighbors = new HashSet<>();

  private boolean offLimits;

  /**
   * The MapTiles that make up this Territory. Make it a SetUniqueList because
   * at some point we will need to pick a random MapTile.
   */
  private final SetUniqueList<MapTile> territoryTiles = SetUniqueList.setUniqueList(new ArrayList<MapTile>());

  private boolean landlocked;

  /**
   * Construct a Territory.
   * 
   * 
   */
  Territory() {
    // Empty... for now.
  }

  private void markAsLandAndAddTileToTerritory(final MapTile tile) {
    tile.setType(MapTileType.LAND);
    tile.setTerritory(this);
    territoryTiles.add(tile);
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
   * @param sizeRange
   *          A Range object to check a Territory's size
   * @return a Territory.
   */
  private Territory buildArea(final MapTile startTile, final int minSize, final int maxSize, final Range<Integer> sizeRange) {
    if (startTile.getType() != MapTileType.WATER) {
      throw new IllegalArgumentException("Start tile must be water.");
    }

    int targetSize = getRandomTargetSize(minSize, maxSize);

    int h20avail = countWaterTilesAvailableWithMax(startTile, maxSize).size();
    System.out.println("There are " + h20avail + " water tiles available.");

    if (h20avail < minSize) {
      return null;
    }
    if (h20avail < targetSize) {
      targetSize = h20avail;
    }

    generateRandomArea(startTile, targetSize);

    int size = territoryTiles.size();

    Territory result = null;
    if (sizeRange.contains(Integer.valueOf(size))) {
      result = this;
    } else {
      clearTerritoryTiles();
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
      tile.setOffLimits(true);
    }
  }

  public boolean containsTile(final MapTile tile) {
    return territoryTiles.contains(tile);
  }

  public void findNeighbors() {

    List<Direction> directions = Arrays.asList(Direction.DOWN, Direction.UP, Direction.RIGHT, Direction.LEFT);

    for (MapTile p : territoryTiles) {

      for (Direction y : directions) {

        MapTile nd = p.get(y);
        if (isNeighbor(nd)) {

          neighbors.add(p.getTerritory());

        }

      }
    }
  }

  static Set<MapTile> countWaterTilesAvailableWithMax(final MapTile startTile, final int max) {
    // if (!startTile.getType().equals(MapTile.MapTileType.WATER)) {
    // }

    Queue<MapTile> tileQueue = new LinkedList<>();
    tileQueue.add(startTile);

    Set<MapTile> seenTiles = new HashSet<>();
    seenTiles.add(startTile);
    int count = 0;
    while (!tileQueue.isEmpty()) {
      MapTile waterTile = tileQueue.remove();
      for (MapTile adjacentWaterTile : waterTile.getAdjacentWaterTiles()) {
        if (!seenTiles.contains(adjacentWaterTile)) {
          count++;

          tileQueue.add(adjacentWaterTile);
          seenTiles.add(adjacentWaterTile);

          if (count == max) {
            return seenTiles;
          }
        }
      }
    }

    return seenTiles;
  }

  private void generateRandomArea(final MapTile startTile, final int targetSize) {

    markAsLandAndAddTileToTerritory(startTile);

    MapTile tile = startTile;
    int size = 1;
    for (; size < targetSize; size++) {
      tile = getNextTile(tile);
      if (tile == null) {
        setOffLimits(true);
        break;
      }
      markAsLandAndAddTileToTerritory(tile);

    }
  }

  private MapTile getNextTile(final MapTile tile) {
    MapTile result = tile.getRandomAdjacentWaterTile();
    if (result == null) {
      result = getRandomAdjacentWaterTile();
    }
    return result;
  }

  // SetUniqueList<MapTile> getTerritoryTiles() {
  // return territoryTiles;
  // }

  private boolean isNeighbor(final MapTile nd) {
    if (nd != null) {
      if (!this.equals(nd.getTerritory())) {
        return true;
      }
    }
    return false;
  }

  public boolean isOffLimits() {
    return offLimits;
  }

  public void setOffLimits(final boolean flag) {
    this.offLimits = flag;
  }

  /**
   * Find a random water tile adjacent to this Territory.
   * 
   * @param territory
   *          The territory to check for adjacent water.
   * @return a random water tile or null if there is no water tile adjacent to
   *         this Territory
   */
  MapTile getRandomAdjacentWaterTile() {

    SetUniqueList<MapTile> waterTiles = SetUniqueList.setUniqueList(new ArrayList<MapTile>());

    for (MapTile landTile : territoryTiles) {
      waterTiles.addAll(landTile.getAdjacentWaterTiles());
    }

    MapTile result;
    if (waterTiles.isEmpty()) {
      result = null;
      this.setLandLocked();
    } else {
      final int index = RNG.nextInt(waterTiles.size());
      result = waterTiles.get(index);
    }

    return result;
  }

  void setLandLocked() {
    this.landlocked = true;

  }

  public boolean isLandLocked() {
    return landlocked;
  }
}
