package com.spamalot.dolt.map;

import com.spamalot.dolt.world.DoltWorld;
import com.spamalot.dolt.world.WorldTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * 5. Check how many water tiles are available.  If >= minimum size and <= size from step 1 then return water tile 
 * from step 4 to be used for start tile of new Territory. 
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
public class DoltMap {
  private static final Logger LOGGER = LoggerFactory.getLogger(DoltMap.class);
  /**
   * When creating a Territory it can fail if there are not enough water tiles for
   * the given size. This is the number of times to attempt to build.
   */
  private static final int TERRITORY_BUILD_ATTEMPTS = 100;

  /**
   * Default minimum size of a Territory.
   */
  private static final int DEFAULT_MIN_TERRITORY_SIZE = 10;

  /**
   * Default maximum size of a territory.
   */
  private static final int DEFAULT_MAX_TERRITORY_SIZE = 30;

  /**
   * The territories in this Map.
   */
  private final List<Territory> territories = new ArrayList<>();

  /**
   * The Game Map for this World.
   */
  private final DoltWorld<MapFeatures> gameMap;

  /**
   * A Random Number Generator.
   */
  private static final Random RNG = new Random();

  /**
   * The DoltWorld is a DoltMap and the list of Territories in it.
   * 
   * @param mapWidth       the map's width
   * @param mapHeight      the map's height
   * @param numTerritories the number of territories to put in the map
   */
  public DoltMap(final int mapWidth, final int mapHeight, final int numTerritories) {
    this.gameMap = new DoltWorld<>(mapWidth, mapHeight);

    addTerritories(numTerritories, DEFAULT_MIN_TERRITORY_SIZE, DEFAULT_MAX_TERRITORY_SIZE);

    for (Territory territory : this.territories) {
      territory.findNeighbors();
    }
  }

  /**
   * Add territories to the map.
   * 
   * @param numTerritories   Number of territories to add
   * @param minTerritorySize minimum size of territories
   * @param maxTerritorySize maximum size of territories
   */
  private void addTerritories(final int numTerritories, final int minTerritorySize, final int maxTerritorySize) {
    // Make the first territory. TODO: be more random in initial placement.
    final Territory territory = new Territory.Builder(this.gameMap.getMapTile(0, 0), minTerritorySize, maxTerritorySize)
        .build();
    this.territories.add(territory);

    int count = 1;
    Territory rndTerritory = getRandomTerritoryNotLandLocked();
    while (rndTerritory != null && count < numTerritories) {

      WorldTile<MapFeatures> tile = Territory.getRandomAdjacentWaterTile(rndTerritory);
      if (tile == null) {
        rndTerritory.setLandLocked();
        rndTerritory = getRandomTerritoryNotLandLocked();
        continue;
      }
      int rndSize = getRandomTargetSize(minTerritorySize, maxTerritorySize);
      Set<WorldTile> cnt = Territory.countWaterTilesAvailableWithMax(tile, rndSize);

      if (cnt.size() < minTerritorySize) {
        for (WorldTile<MapFeatures> gjkdf : cnt) {
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

      LOGGER.info("Intermediate result: {}", this);
    }
  }

  /**
   * Determine a random size to make a Territory.
   * 
   * @param minSize minimum size to make a Territory
   * @param maxSize maximum size to make a Territory
   * @return A size to make the Territory.
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

  /**
   * Generate a new Territory using the water tile given as a starting point. It
   * is assumed that there is enough space to construct the territory.
   * 
   * @param tile             Water tile to start making territory
   * @param minTerritorySize Minimum size to make this territory
   * @param maxTerritorySize Maximum size to make this territory
   */
  private void generateTerritory(final WorldTile<MapFeatures> tile, final int minTerritorySize, final int maxTerritorySize) {
    // TODO: Move space checking into here.
    // TODO: Maybe determine size outside of this method.
    Territory newTerritory = null;
    int attempts = TERRITORY_BUILD_ATTEMPTS;
    while (newTerritory == null && attempts-- > 0) {
      WorldTile<MapFeatures> startTile = tile;
      newTerritory = new Territory.Builder(startTile, minTerritorySize, maxTerritorySize).build();
    }

    if (newTerritory != null) {
      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("Used {} attempts.", TERRITORY_BUILD_ATTEMPTS - attempts);
      }
      this.territories.add(newTerritory);
    }
  }

  /**
   * Get a random Territory that is not land locked.
   * 
   * @return a Territory.
   */
  private Territory getRandomTerritoryNotLandLocked() {
    // TODO: When adding islands to the game, will need to modify this.
    int count = 0;
    Territory result = null;
    for (Territory t : this.territories) {
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
    return "DoltWorld [gameMap=\n" + this.gameMap + "\nnumber of Territories: " + this.territories.size() + "]";
  }

}
