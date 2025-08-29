package fr.LaurentFE.pacManCloneAgain.model.map;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;

public class TileIndex {

  public int x;
  public int y;

  public TileIndex(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  public Position toPosition() {
    return new Position(x * GameConfig.TILE_SIZE,
        y * GameConfig.TILE_SIZE);
  }

  public String toString() {
    return "TileIndex[" + x + ", " + y + "]";
  }

  public TileIndex add(final TileIndex ti) {
    x += ti.x;
    y += ti.y;
    return this;
  }
}
