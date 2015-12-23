package com.spamalot.dolt.map;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.Random;

final class TerritoryBuilder {

  /**
   * Instantiate Nothing.
   */
  private TerritoryBuilder() {
  }

  /**
   * Random Number Generator.
   */
  private static final Random RNG = new Random();

  /**
   * Find a random water tile adjacent to this Territory.
   * 
   * @return a random water tile or null if there is no water tile adjacent to
   *         this Territory
   */
  static MapTile getRandomAdjacentWaterTile(final SetUniqueList<MapTile> territoryTiles) {
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

}
