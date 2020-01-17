package com.spamalot.dolt.cli;

import com.spamalot.dolt.world.DoltWorld;
import com.spamalot.dolt.world.WorldTile;
import com.spamalot.dolt.world.WorldTileType;

public class DoLTCLI {
  public static void main(String[] args) {
    final DoltWorld world = new DoltWorld(10, 10);
    WorldTile t = world.getMapTile(5, 5);
    t.setType(WorldTileType.LAND);

    System.out.println(world.toString());
  }
}
