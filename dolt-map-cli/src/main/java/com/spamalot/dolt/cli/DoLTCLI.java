package com.spamalot.dolt.cli;

import com.spamalot.dolt.map.DoltMap;
import com.spamalot.dolt.map.MapTile;
import com.spamalot.dolt.world.DoltWorld;
import com.spamalot.dolt.world.NewWorldTileImpl;
import com.spamalot.dolt.world.WorldTileType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DoLTCLI {
  private DoLTCLI() {
  }

  /** Loggit. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DoLTCLI.class);
  /** Number of Territories in a default Map. */
  private static final int DEFAULT_NUM_TERRITORIES = 10;

  /** Height of a default Map. */
  private static final int DEFAULT_MAP_HEIGHT = 20;

  /** Width of a default Map. */
  private static final int DEFAULT_MAP_WIDTH = 40;

  public static void main(final String[] args) {
    final DoltWorld<MapTile> world = new DoltWorld<>(10, 10, MapTile.class);
    NewWorldTileImpl t = world.getMapTile(5, 5);
    t.setType(WorldTileType.LAND);

    LOGGER.info("This is the world {}", world);

    LOGGER.info("Something will happen here!");
    final DoltMap world1 = new DoltMap(DEFAULT_MAP_WIDTH, DEFAULT_MAP_HEIGHT, DEFAULT_NUM_TERRITORIES);

    LOGGER.info("World: {}", world1);
  }
}
