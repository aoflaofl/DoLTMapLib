package com.spamalot.dolt.world;

import com.spamalot.dolt.world.grid.Direction;

import java.util.List;

public interface NewWorldTile<T extends MapTileFeatures> {

  NewWorldTile<T> get(Direction y);


  List<NewWorldTile<T>> getAdjacentWaterTiles();

  /**
   * Return a random water MapTile.
   * 
   * @return a MapTile with water, or null if there is none
   */
  NewWorldTile<T> getRandomAdjacentWaterTile();

  /**
   * Get this MapTile's Type.
   * 
   * @return The MapTileType
   */
  WorldTileType getType();

  /**
   * Set this MapTile's type.
   * 
   * @param type The type to set this Tile to
   */
  void setType(WorldTileType type);
}
