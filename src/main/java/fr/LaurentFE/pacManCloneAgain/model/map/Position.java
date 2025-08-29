package fr.LaurentFE.pacManCloneAgain.model.map;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;

public class Position {

  public int x;
  public int y;

  public Position(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  public TileIndex toTileIndex() {
    return new TileIndex(x / GameConfig.TILE_SIZE,
        y / GameConfig.TILE_SIZE);
  }

  public String toString() {
    return "Position[" + x + ", " + y + "]";
  }

  public Position add(final Position p) {
    x += p.x;
    y += p.y;
    return this;
  }
}
