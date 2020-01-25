package com.spamalot.dolt.map;

import com.spamalot.dolt.world.WorldTile;

/**
 * @author gejohann
 *
 */
public class MapTile extends WorldTile {
  private Territory territory;
  private boolean offLimits;

  public void setOffLimits(boolean b) {
    offLimits = b;
  }

  public boolean isOffLimits() {
    return offLimits;
  }

  public void setTerritory(Territory object) {
    territory = object;
  }

  public Territory getTerritory() {
    return territory;
  }
}
