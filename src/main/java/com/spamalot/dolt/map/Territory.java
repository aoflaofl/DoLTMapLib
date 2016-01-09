package com.spamalot.dolt.map;

import com.google.common.collect.Range;

import com.spamalot.dolt.map.MapTile.MapTileType;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

  private final Set<Territory> neighbors = new HashSet<>();

  private boolean offLimits;

  public boolean isOffLimits() {
    return offLimits;
  }

  public void setOffLimits(final boolean offLimits) {
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
  private Territory() {
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
  private Territory buildArea(final MapTile startTile, final int minSize, final int maxSize) {
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

  private void addTileToTerritory(final MapTile tile) {
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
      tile.setOffLimits(true);
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

  public void findNeighbors() {

    List<Direction> x = Arrays.asList(Direction.DOWN, Direction.UP, Direction.RIGHT, Direction.LEFT);

    for (MapTile p : territoryTiles) {

      for (Direction y : x) {

        MapTile nd = p.get(y);
        checkNeighbor(p, nd);

      }
    }
  }

  private void checkNeighbor(MapTile p, MapTile nd) {
    if (nd != null) {
      if (!this.equals(nd.getTerritory())) {
        neighbors.add(p.getTerritory());
      }
    }
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
