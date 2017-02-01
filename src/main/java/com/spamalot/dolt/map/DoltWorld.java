package com.spamalot.dolt.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/*
 * Generating a new Territory, arguments are minimum size and maximum size for territory.
 * 
 * 1. Generate a random size for the territory between minimum size and maximum size.
 * 
 * 2. Get a Random Territory that is not land locked.
 * 
 * 2.5 If no territory is found, world building is over.
 * 
 * 3. If Territory has no water tiles or all water tiles are marked off limits, mark as land locked. Goto 2.
 * 
 * 4. Get Random Water tile that isn't marked off limits.
 * 
 * 4.5 if no water file is found, or all are off limits, mark territory as land locked, goto 2.
 *  
 * 5. Check how many water tiles are available.  If >= minimum size and <= size from step 1 then return water tile from step 4 to be used for start tile of new Territory. 
 * 
 * 6. If available water tiles < minimum size, mark all as off limits.
 * 
 * 7. Goto 4.
 */
/**
 * Contain the Map and Territories.
 * 
 * @author johannsg
 *
 */
public class DoltWorld {
  /**
   * When creating a Territory it can fail if there are not enough water tiles
   * for the given size. This is the number of times to attempt to build.
   */
  private static final int TERRITORY_BUILD_ATTEMPTS = 100;

  /**
   * Default minimum size of a Territory.
   */
  private static final int DEFAULT_MIN_TERRITORY_SIZE = 50;

  /**
   * Default maximum size of a territory.
   */
  private static final int DEFAULT_MAX_TERRITORY_SIZE = 60;

  /**
   * The territories in this Map.
   */
  private final List<Territory> territories = new ArrayList<>();
  private final DoltMap gameMap;
  /**
   * A Random Number Generator.
   */
  private static final Random RNG = new Random();

  /**
   * The DoltWorld is a DoltMap and the list of Territories in it.
   * 
   * @param mapWidth
   *          the map's width
   * @param mapHeight
   *          the map's height
   * @param numTerritories
   *          the number of territories to put in the map
   */
  public DoltWorld(final int mapWidth, final int mapHeight, final int numTerritories) {
    gameMap = new DoltMap(mapWidth, mapHeight);

    addTerritories(numTerritories, DEFAULT_MIN_TERRITORY_SIZE, DEFAULT_MAX_TERRITORY_SIZE);

    for (Territory dkhgd : territories) {
      dkhgd.findNeighbors();
    }
  }

  /**
   * Add territories to the map.
   * 
   * @param numTerritories
   *          Number of territories to add
   * @param minTerritorySize
   *          minimum size of territories
   * @param maxTerritorySize
   *          maximum size of territories
   */
  private void addTerritories(final int numTerritories, final int minTerritorySize, final int maxTerritorySize) {
    // Make the first territory. TODO: be more random in initial placement.
    final Territory territory = new Territory.Builder(gameMap.getMapTiles()[0][0], minTerritorySize, maxTerritorySize).build();
    territories.add(territory);

    int count = 1;
    Territory rndTerritory = getRandomTerritoryNotLandLocked();
    while (rndTerritory != null && count <= numTerritories) {

      MapTile tile = rndTerritory.getRandomAdjacentWaterTile();
      if (tile == null) {
        rndTerritory.setLandLocked();
        rndTerritory = getRandomTerritoryNotLandLocked();
        continue;
      }
      int rndSize = getRandomTargetSize(minTerritorySize, maxTerritorySize);
      Set<MapTile> cnt = Territory.countWaterTilesAvailableWithMax(tile, rndSize);

      if (cnt.size() < minTerritorySize) {
        for (MapTile gjkdf : cnt) {
          gjkdf.setOffLimits(true);
        }
        rndTerritory = getRandomTerritoryNotLandLocked();
        continue;
      }

      int max = maxTerritorySize;
      if (cnt.size() <= maxTerritorySize) {
        max = cnt.size();
      }
      generateTerritory(tile, minTerritorySize, max);
      count++;
      rndTerritory = getRandomTerritoryNotLandLocked();
    }

    // for (int i = 1; i < numTerritories; i++) {
    // generateTerritory(minTerritorySize, maxTerritorySize);
    // }
  }

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

  private void generateTerritory(final MapTile tile, final int minTerritorySize, final int maxTerritorySize) {

    Territory newTerritory = null;
    int attempts = TERRITORY_BUILD_ATTEMPTS;
    while (newTerritory == null && attempts-- > 0) {
      MapTile startTile = tile; // getRandomWaterTileNextToLand();
      newTerritory = new Territory.Builder(startTile, minTerritorySize, maxTerritorySize).build();
    }

    if (newTerritory != null) {
      territories.add(newTerritory);
    }
  }

  // private MapTile getRandomWaterTileNextToLand() { // TODO: What if want an
  // // island?
  // MapTile result = null;
  // while (result == null) {
  // final Territory randomTerritory = getRandomTerritoryNotLandLocked();
  // if (randomTerritory == null) {
  // break;
  // }
  // result = randomTerritory.getRandomAdjacentWaterTile();
  // }
  // return result;
  // }

  /**
   * Get a random Territory that is not land locked.
   * 
   * @return a Territory.
   */
  private Territory getRandomTerritoryNotLandLocked() {
    /*
     * TODO: When adding islands to the game, will need to modify this.
     * 
     * TODO: Only check non land locked Territories.
     */
    int count = 0;
    Territory result = null;
    for (Territory t : territories) {
      if (t.isLandLocked()) {
        continue;
      }

      count++;
      if (RNG.nextInt(count) == 0) {
        result = t;
      }
    }

    return result;
  }

  @Override
  public final String toString() {
    return "DoltWorld [gameMap=\n" + gameMap + "\nnumber of Territories: " + territories.size() + "]";
  }

}
