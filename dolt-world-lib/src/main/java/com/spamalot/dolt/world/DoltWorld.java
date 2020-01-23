package com.spamalot.dolt.world;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Range;
import com.spamalot.dolt.world.grid.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hold the map. Idea is a World is just the terrain, not the political
 * boundaries.
 *
 * @author gej
 * 
 * @param <T> A tile type
 *
 */
public class DoltWorld<T extends NewWorldTileImpl> {
  /** Loggit. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DoltWorld.class);

  /** A range check object for the height of the map. */
  private final Range<Integer> heightRange;
  /** Actual height of the Map. */
  private final int mapHeight;

  /** The MapTiles that make up this Map. */
  private T[][] mapTiles;
  /** Actual width of the Map. */
  private final int mapWidth;

  /** A range check object for the width of the map. */
  private final Range<Integer> widthRange;

  /**
   * Construct a Map Object.
   *
   * @param width  Width of the Map
   * @param height Height of the Map
   * @param clazz  Class of the tile
   */
  @SuppressWarnings("unchecked")
  public DoltWorld(final int width, final int height, final Class<T> clazz) {
    this.mapWidth = width;
    this.mapHeight = height;

    this.widthRange = Range.closedOpen(0, this.mapWidth);
    this.heightRange = Range.closedOpen(0, this.mapHeight);

    this.mapTiles = (T[][]) new NewWorldTileImpl[width][height];
    // Initialize all the tiles to be water tiles.
    initMapTiles(width, height, WorldTileType.WATER, clazz);
    // Link the tiles orthogonally to their neighbors
    linkTiles();
  }

  /**
   * Get the map tile.
   *
   * @param i Horizontal coordinate
   * @param j Vertical coordinate
   * @return The map tile at those coordinates
   */
  public final T getMapTile(final int i, final int j) {
    T ret = null;
    if (isOnMap(i, j)) {
      ret = this.mapTiles[i][j];
    }
    return ret;
  }

  /**
   * Get a map tile in a direction.
   *
   * @param i   Horizontal coordinate
   * @param j   Vertical coordinate
   * @param dir Direction to get map tile
   * @return The map tile in that direction
   */
  private T getMapTileInDirection(final int i, final int j, final Direction dir) {
    checkNotNull(dir);

    return getMapTile(i + dir.gethDiff(), j + dir.getvDiff());
  }

  /**
   * Create the map, with all the tiles being of type.
   *
   * @param width  Width of the map
   * @param height Height of the map
   * @param type   Type of tile
   * @param clazz  Class of tile
   */
  private void initMapTiles(final int width, final int height, final WorldTileType type, final Class<T> clazz) {
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        try {
          this.mapTiles[i][j] = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
          LOGGER.info("Creating an object failed.", e);
        }
        this.mapTiles[i][j].setType(type);
      }
    }
  }

  /**
   * Check if coordinate is on map.
   *
   * @param x The X ordinate
   * @param y The Y ordinate
   * @return true if this Coordinate is on the Map
   */
  private boolean isOnMap(final int x, final int y) {
    return this.widthRange.contains(x) && this.heightRange.contains(y);
  }

  /**
   * Link the tiles together.
   */
  private void linkTiles() {
    for (int i = 0; i < this.mapWidth; i++) {
      for (int j = 0; j < this.mapHeight; j++) {
        NewWorldTileImpl cur = getMapTile(i, j);
        if (cur != null) {
          cur.linkTileInDirection(Direction.LEFT, getMapTileInDirection(i, j, Direction.LEFT));
          cur.linkTileInDirection(Direction.RIGHT, getMapTileInDirection(i, j, Direction.RIGHT));
          cur.linkTileInDirection(Direction.UP, getMapTileInDirection(i, j, Direction.UP));
          cur.linkTileInDirection(Direction.DOWN, getMapTileInDirection(i, j, Direction.DOWN));
        }
      }
    }
  }

  @Override
  public final String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append('+');
    for (int x = 0; x < this.mapWidth; x++) {
      sb.append("-+");
    }
    sb.append('\n');
    for (int y = 0; y < this.mapHeight; y++) {
      sb.append('|');
      for (int x = 0; x < this.mapWidth; x++) {
        sb.append(this.mapTiles[x][y]);
        sb.append('|');
      }
      sb.append("\n+");
      for (int x = 0; x < this.mapWidth; x++) {
        sb.append("-+");
      }
      sb.append('\n');
    }
    return sb.toString();
  }

}
