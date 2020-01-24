package com.spamalot.dolt.map;

import com.spamalot.dolt.world.WorldTile;

public class MapTile extends WorldTile {
  private Territory territory;

  public void setOffLimits(boolean b) {
    // TODO Auto-generated method stub

  }

  public void setTerritory(Territory object) {
    territory = object;
  }

  public Territory getTerritory() {
    return territory;
  }

}
