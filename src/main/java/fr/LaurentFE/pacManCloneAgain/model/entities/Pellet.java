package fr.LaurentFE.pacManCloneAgain.model.entities;

import fr.LaurentFE.pacManCloneAgain.model.GameConfig;
import fr.LaurentFE.pacManCloneAgain.model.map.TileIndex;
import java.awt.Rectangle;

public class Pellet {

  private final TileIndex tileIndex;
  private final int score;
  private final boolean isPowerPellet;

  public Pellet(final int x, final int y, final boolean isPowerPellet) {
    tileIndex = new TileIndex(x, y);
    score = (isPowerPellet) ? 50 : 10;
    this.isPowerPellet = isPowerPellet;
  }

  public TileIndex getTileIndex() {
    return tileIndex;
  }

  public int getScore() {
    return score;
  }

  public boolean isPowerPellet() {
    return isPowerPellet;
  }

  public Rectangle getHitBox() {
    if (isPowerPellet()) {
      return new Rectangle(
          tileIndex.toPosition().x + GameConfig.DEFAULT_POWER_PELLET_OFFSET,
          tileIndex.toPosition().y + GameConfig.DEFAULT_POWER_PELLET_OFFSET,
          GameConfig.DEFAULT_POWER_PELLET_SIZE,
          GameConfig.DEFAULT_POWER_PELLET_SIZE);
    } else {
      return new Rectangle(
          tileIndex.toPosition().x + GameConfig.DEFAULT_PELLET_OFFSET,
          tileIndex.toPosition().y + GameConfig.DEFAULT_PELLET_OFFSET,
          GameConfig.DEFAULT_PELLET_SIZE,
          GameConfig.DEFAULT_PELLET_SIZE);
    }
  }
}
