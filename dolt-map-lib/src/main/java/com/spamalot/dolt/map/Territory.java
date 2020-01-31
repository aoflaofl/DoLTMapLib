package com.spamalot.dolt.map;

import com.google.common.collect.Range;
import com.spamalot.dolt.world.WorldTileType;
import com.spamalot.dolt.world.grid.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections4.list.SetUniqueList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Territory is a collection of land cells in a map.
 *
 * @author gej
 *
 */
final class Territory {
  /** Whether this territory has access to water. */
  private boolean landlocked;

  /** Territories bordering this one. */
  private final Set<Territory> neighborTerritories = new HashSet<>();

  private boolean offLimits;

  /** The MapTiles that make up this Territory. */
  private final Set<MapTile> territoryTiles = new HashSet<>(new ArrayList<MapTile>());

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(Territory.class);

  /** Random Number Generator. */
  private static final Random RNG = new Random();

  static Set<MapTile> countWaterTilesAvailableWithMax(final MapTile startTile, final int max) {

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

  static Logger getLogger() {
    return LOGGER;
  }

  /**
   * Find a random water tile adjacent to this Territory.
   *
   * @param territory The territory to check for adjacent water.
   * @return a random water tile or null if there is no water tile adjacent to
   *         this Territory
   */
  static MapTile getRandomAdjacentWaterTile(final Territory territory) {

    SetUniqueList<MapTile> waterTiles = SetUniqueList.setUniqueList(new ArrayList<MapTile>());

    for (MapTile landTile : territory.territoryTiles) {
      for (MapTile water : landTile.getAdjacentWaterTiles()) {
        // Ignore water tiles marked as not to be used.
        if (!water.isOffLimits()) {
          waterTiles.add(water);
        }
      }
    }

    MapTile result = null;
    if (waterTiles.isEmpty()) {
      territory.setLandLocked();
    } else {
      final int index = RNG.nextInt(waterTiles.size());
      result = waterTiles.get(index);
    }

    return result;
  }

  static Random getRng() {
    return RNG;
  }

  public boolean containsTile(final MapTile tile) {
    return this.territoryTiles.contains(tile);
  }

  public void findNeighbors() {

    List<Direction> directions = Arrays.asList(Direction.DOWN, Direction.UP, Direction.RIGHT, Direction.LEFT);

    for (MapTile p : this.territoryTiles) {

      for (final Direction y : directions) {

        MapTile nd = p.get(y);
        if (isNeighbor(nd)) {

          this.neighborTerritories.add(p.getTerritory());

        }

      }
    }
  }

  public Set<MapTile> getTerritoryTiles() {
    return territoryTiles;
  }

  public boolean isLandLocked() {
    return this.landlocked;
  }

  private boolean isNeighbor(final MapTile nd) {
    return nd != null && !this.equals(nd.getTerritory());
  }

  public boolean isOffLimits() {
    return this.offLimits;
  }

  void setLandLocked() {
    this.landlocked = true;

  }

  public void setOffLimits(final boolean flag) {
    this.offLimits = flag;
  }

  /**
   * Build a territory in the map.
   * 
   * @author gej
   *
   */
  public static class Builder {
    private int maximumSize;

    private int minimumSize;

    private Range<Integer> sizeRange;

    private MapTile startTile;

    Builder() {

    }

    Builder setStartTile(final MapTile initTile) {
      this.startTile = initTile;
      return this;
    }

    Builder setMinSize(final int minSize) {
      this.minimumSize = minSize;
      return this;
    }

    Builder setMaxSize(final int maxSize) {
      this.maximumSize = maxSize;
      return this;
    }

    /**
     * Mark the MapTiles assigned to this Territory as water, but since there failed
     * to be enough of them in a cluster to make a Territory also mark them as off
     * limits so they won't be used in the future.
     *
     * @param t The territory.
     */
    private static void clearTerritoryTiles(final Territory t) {
      for (MapTile waterTile : t.getTerritoryTiles()) {
        waterTile.setType(WorldTileType.WATER);
        waterTile.setTerritory(null);
        waterTile.setOffLimits(true);
      }
    }

    private static void generateRandomArea(final MapTile startTile, final int targetSize, final Territory t) {

      markAsLandAndAddTileToTerritory(startTile, t);

      MapTile tile = startTile;
      int size = 1;
      for (; size < targetSize; size++) {
        tile = getNextTile(tile, t);
        if (tile == null) {
          t.setOffLimits(true);
          break;
        }
        markAsLandAndAddTileToTerritory(tile, t);

      }
    }

    private static MapTile getNextTile(final MapTile tile, final Territory t) {
      MapTile result = tile.getRandomAdjacentWaterTile();
      if (result == null) {
        result = getRandomAdjacentWaterTile(t);
      }
      return result;
    }

    /**
     * Get a random target size between minSize and maxSize inclusive.
     *
     * @param minSize minimum size
     * @param maxSize maximum size
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
        targetSize = minSize + getRng().nextInt(maxSize - minSize + 1);
      }

      return targetSize;
    }

    private static void markAsLandAndAddTileToTerritory(final MapTile tile, final Territory t) {
      tile.setType(WorldTileType.LAND);
      tile.setTerritory(t);
      t.getTerritoryTiles().add(tile);
    }

    public Territory build() {
      this.sizeRange = Range.closed(this.minimumSize, this.maximumSize);
      return buildArea(this.minimumSize, this.maximumSize);
    }

    /**
     * Allocate MapTiles to a Territory.
     *
     * @param minSize Minimum size of the Territory
     * @param maxSize Maximum size of the Territory.
     * @return a Territory.
     */
    private Territory buildArea(final int minSize, final int maxSize) {
      if (this.startTile.getType() != WorldTileType.WATER) {
        throw new IllegalArgumentException("Start tile must be water.");
      }

      int h20avail = countWaterTilesAvailableWithMax(this.startTile, maxSize).size();
      getLogger().info("There are {} water tiles available.", h20avail);

      if (h20avail < minSize) {
        return null;
      }

      int targetSize = getRandomTargetSize(minSize, maxSize);

      if (h20avail < targetSize) {
        targetSize = h20avail;
      }
      Territory result = new Territory();
      generateRandomArea(this.startTile, targetSize, result);

      int size = result.getTerritoryTiles().size();

      if (!this.sizeRange.contains(size)) {
        clearTerritoryTiles(result);
      }

      return result;
    }
  }

}
