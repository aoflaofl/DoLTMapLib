package com.spamalot.dolt.world;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author gej
 *
 */
public class NewWorldTileImplTest {
  WorldTile testTile;

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    testTile = new WorldTile();
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for
   * {@link com.spamalot.dolt.world.WorldTile#getAdjacentWaterTiles()}.
   */
  @Test
  public void testGetAdjacentWaterTiles() {
    testTile.getAdjacentWaterTiles();
  }

  /**
   * Test method for
   * {@link com.spamalot.dolt.world.WorldTile#getRandomAdjacentWaterTile()}.
   */
  @Test
  public void testGetRandomAdjacentWaterTile() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link com.spamalot.dolt.world.WorldTile#getType()}.
   */
  @Test
  public void testGetType() {
    fail("Not yet implemented");
  }

  /**
   * Test method for
   * {@link com.spamalot.dolt.world.WorldTile#setType(com.spamalot.dolt.world.WorldTileType)}.
   */
  @Test
  public void testSetType() {
    testTile.setType(WorldTileType.LAND);
  }

}
