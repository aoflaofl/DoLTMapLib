package com.spamalot.dolt.world;

/**
 * Type of this MapTile. Only two values: Land and Water.
 * 
 */
public enum WorldTileType {
  /** This Tile is Land. */
  LAND("#"),
  /** This Tile is Water. */
  WATER(".");

  private String stringValue;

  WorldTileType(String value) {
    stringValue = value;
  }

  @Override
  public String toString() {
    return stringValue;
  }
}
