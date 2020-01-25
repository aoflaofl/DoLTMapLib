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

  /** Visible representation. */
  private String stringValue;

  WorldTileType(final String value) {
    stringValue = value;
  }

  @Override
  public String toString() {
    return stringValue;
  }
}
