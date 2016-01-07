package com.spamalot.dolt.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contain the Map and Territories.
 * 
 * @author johannsg
 *
 */
public class DoltWorld {
  /**
   * Default minimum size of a Territory.
   */
  private static final int DEFAULT_MIN_TERRITORY_SIZE = 10;

  /**
   * Default maximum size of a territory.
   */
  private static final int DEFAULT_MAX_TERRITORY_SIZE = 20;

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
  }

  private void addTerritories(final int numTerritories, final int minTerritorySize, final int maxTerritorySize) {
    final Territory territory = new Territory();

    territory.buildArea(gameMap.getMapTiles()[0][0], minTerritorySize, maxTerritorySize);
    territories.add(territory);
    for (int i = 0; i < numTerritories; i++) {
      generateTerritory(minTerritorySize, maxTerritorySize);
    }
  }

  private void generateTerritory(final int minTerritorySize, final int maxTerritorySize) {
    MapTile startTile = getRandomWaterTile();
    final Territory newTerritory = new Territory();
    newTerritory.buildArea(startTile, minTerritorySize, maxTerritorySize);
    territories.add(newTerritory);
  }

  private MapTile getRandomWaterTile() { // TODO: What if want an island?
    MapTile result = null;
    while (result == null) {
      final Territory randomTerritory = getRandomTerritory();

      result = TerritoryBuilder.getRandomAdjacentWaterTile(randomTerritory.getTerritoryTiles());
    }
    return result;
  }

  private Territory getRandomTerritory() {
    return territories.get(RNG.nextInt(territories.size()));
  }

  @Override
  public final String toString() {
    return "DoltWorld [gameMap=" + gameMap + "]";
  }

}
