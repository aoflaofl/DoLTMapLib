package com.spamalot.dolt.map;

import com.spamalot.dolt.world.WorldTile;
import com.spamalot.dolt.world.WorldTileType;
import java.util.List;

public class MapTile extends WorldTile<MapFeatures> {

  @Override
  public List<WorldTile<MapFeatures>> getAdjacentWaterTiles() {
    // TODO Auto-generated method stub
    return super.getAdjacentWaterTiles();
  }

  @Override
  public WorldTile<MapFeatures> getRandomAdjacentWaterTile() {
    // TODO Auto-generated method stub
    return super.getRandomAdjacentWaterTile();
  }

  @Override
  public WorldTileType getType() {
    // TODO Auto-generated method stub
    return super.getType();
  }

//  @Override
//  public void setType(WorldTileType type) {
//    // TODO Auto-generated method stub
//    super.setType(type);
//  }

  @Override
  public void setFeatures(MapFeatures f) {
    // TODO Auto-generated method stub
    super.setFeatures(f);
  }

  @Override
  public MapFeatures getFeatures() {
    // TODO Auto-generated method stub
    return super.getFeatures();
  }

}
